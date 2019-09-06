using System;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ApplicationInsights;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Bot.Builder;
using Microsoft.Bot.Builder.Azure;
using Microsoft.Bot.Builder.BotFramework;
using Microsoft.Bot.Builder.Integration;
using Microsoft.Extensions.Caching.Memory;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Trask.Bot.Azure.Services;
using Trask.Bot.EventBot.Options;
using Trask.Bot.EventBot.Processors;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.EventBot.Recognition.Trie;
using Trask.Bot.Options;
using Trask.Bot.QnA;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Services;
using Trask.Bot.Services.Microsoft.BotMiddlewares;
using Trask.Bot.Services.Microsoft.Extensions;
using Trask.Bot.Services.Microsoft.Recognition;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot
{
    public static partial class ServiceCollectionExtensions
    {
        public static IServiceCollection AddEventBot<TBot>(this IServiceCollection services, Action<EventBotOptions> configureOptions = null)
            where TBot : class, IBot
        {
            if (services == null)
            {
                throw new ArgumentNullException(nameof(services));
            }

            if (configureOptions != null)
            {
                services.Configure(configureOptions);
            }

            Action<BotFrameworkOptions> configureBotFrameworkOptions = (options) => { };

            return services
                    .Configure(configureBotFrameworkOptions)
                    .AddEventBotServices()
                    .AddSingleton<IAdapterIntegration>(BotFrameworkAdapterIntegrationFactory)
                    .AddTransient<IBot, TBot>();
        }

        public static IServiceCollection AddEventBot<TBot>(this IServiceCollection services, Func<IServiceProvider, TBot> implementationFactory, Action<EventBotOptions> configureOptions = null)
            where TBot : class, IBot
        {
            if (services == null)
            {
                throw new ArgumentNullException(nameof(services));
            }

            if (implementationFactory == null)
            {
                throw new ArgumentNullException(nameof(implementationFactory));
            }

            if (configureOptions != null)
            {
                services.Configure(configureOptions);
            }

            Action<BotFrameworkOptions> configureBotFrameworkOptions = (options) => { };

            return services
                    .Configure(configureBotFrameworkOptions)
                    .AddEventBotServices()
                    .AddSingleton<IAdapterIntegration>(BotFrameworkAdapterIntegrationFactory)
                    .AddTransient<IBot>(implementationFactory);
        }

        private static IServiceCollection AddEventBotServices(this IServiceCollection services)
        {
            return services
                    .AddMemoryCache()
                    .AddBotIntentRecognitionServices()
                    .AddBotFeedbackServices()
                    .AddSingleton<IStorage>(BotStatesStorageFactory)
                    .AddBotIntentProcessingServices()
                    .AddSingleton<IResponseDefinitionProvider>(ResponseDefinitionProviderFactory)
                    .AddSingleton<ITranscriptLogger>(TranscriptLoggerFactory)
                    .AddSingleton<EventBotStateAccessors>(sp => new EventBotStateAccessors(new UserState(sp.GetRequiredService<IStorage>()), new ConversationState(sp.GetRequiredService<IStorage>())));
        }

        private static IServiceCollection AddBotIntentRecognitionServices(this IServiceCollection services)
        {
            return services
                    .AddSingleton<IQnAProvider, NlpQnAProvider>(sp =>
                    {
                        var eventBotOptions = sp.GetRequiredService<IOptions<EventBotOptions>>().Value;
                        return new NlpQnAProvider(eventBotOptions.NlpQnAProviderOptions);
                    })
                    .AddSingleton<IIntentRecognitionService, NlpIntentRecognitionService>()
                    .AddSingleton<IIntentRecognitionService, PayloadRecognitionService>()
                    .AddSingleton<IRecognitionOptionsBuilder, EventAgentIntentRecognitionOptionsBuilder>()
                    .AddSingleton<Trask.Bot.Recognition.IRecognizer, IntentRecognizer>();
        }

        private static IServiceCollection AddBotFeedbackServices(this IServiceCollection services)
        {
            return services.AddSingleton<IFeedbackProvider>(sp =>
            {
                var eventBotOptions = sp.GetRequiredService<IOptions<EventBotOptions>>().Value;
                switch (eventBotOptions.FeedbackStorageType)
                {
                    case BotStorageType.DocumentDb:
                        return new DocumentDbFeedbackProvider(sp.GetRequiredService<IDocumentRepository>());
                    case BotStorageType.StorageBlob:
                        return new AzureStorageBlobFeedbackProvider(eventBotOptions.AzureStorageOptions.DataConnectionString, eventBotOptions.AzureStorageOptions.FeedbackContainerName);
                    case BotStorageType.InMemory:
                        return new InMemoryFeedbackProvider();
                    default:
                        throw new NotSupportedException($"A feedback storage type '{eventBotOptions.FeedbackStorageType.ToString("G")}' is not supported.");
                }
            });
        }

        private static IServiceCollection AddBotIntentProcessingServices(this IServiceCollection services)
        {
            return services
                    .AddSingleton<IIntentProcessor, WelcomeIntentProcessor>()
                    .AddSingleton<IIntentProcessor, EventGuideProcessor>()
                    .AddSingleton<IIntentProcessor, GeneralProcessor>()
                    .AddSingleton<IIntentProcessor, FeedbackIntentProcessor>()
                    .AddSingleton<IIntentProcessor, UnrecognizedIntentProcessor>()
                    .AddSingleton<IIntentProcessor, MinorIntentProcessor>()
                    .AddSingleton<IntentProcessorFactory, IntentProcessorFactory>();
        }

        private static IResponseDefinitionProvider ResponseDefinitionProviderFactory(IServiceProvider serviceProvider)
        {
            var eventBotOptions = serviceProvider.GetRequiredService<IOptions<EventBotOptions>>().Value;
            switch (eventBotOptions.ResponseDefinitionStorageType)
            {
                case BotStorageType.StorageTable:
                    if (eventBotOptions.AzureStorageOptions == null) throw new ArgumentException($"Bot storage type is set to use AzureStorage, but configuration is not set. Use {nameof(BotAzureOptions)}.{nameof(BotAzureOptions.AzureStorageOptions)} and set AzureStorage options.");
                    var azureTableResponseDefinitionProvider = new AzureTableResponseDefinitionProvider(eventBotOptions.AzureStorageOptions.DataConnectionString, eventBotOptions.AzureStorageOptions.ResponseDefinitionsTableName);
                    return new CachedResponseDefinitionProvider<AzureTableResponseDefinitionProvider>(azureTableResponseDefinitionProvider, serviceProvider.GetRequiredService<IMemoryCache>(), new TimeSpan(0, eventBotOptions.CacheItemExpirationInMinutes, 0));
                case BotStorageType.DocumentDb:
                    if (eventBotOptions.DocumentDbOptions == null) throw new ArgumentException($"Bot storage type is set to use CosmosDB, but configuration is not set. Use {nameof(BotAzureOptions)}.{nameof(BotAzureOptions.DocumentDbOptions)} and set CosmosDB options.");
                    var documentDbResponseDefinitionProvider = new DocumentDbResponseDefinitionProvider(serviceProvider.GetRequiredService<IDocumentRepository>());
                    return new CachedResponseDefinitionProvider<DocumentDbResponseDefinitionProvider>(documentDbResponseDefinitionProvider, serviceProvider.GetRequiredService<IMemoryCache>(), new TimeSpan(0, eventBotOptions.CacheItemExpirationInMinutes, 0));
                case BotStorageType.InMemory:
                    return new InMemoryResponseDefinitionProvider();
                default:
                    throw new NotSupportedException($"A response definition storage type '{eventBotOptions.ResponseDefinitionStorageType.ToString("G")}' is not supported.");
            }
        }

        private static ITranscriptLogger TranscriptLoggerFactory(IServiceProvider serviceProvider)
        {
            var EventBotOptions = serviceProvider.GetRequiredService<IOptions<EventBotOptions>>().Value;
            switch (EventBotOptions.TranscriptStorageType)
            {
                case BotStorageType.InMemory:
                    return new MemoryTranscriptStore();
                case BotStorageType.StorageBlob:
                    if (EventBotOptions.AzureStorageOptions == null) throw new ArgumentException($"EventBot storage type is set to use AzureStorage, but configuration is not set. Use {nameof(EventBotOptions)}.{nameof(EventBotOptions.AzureStorageOptions)} and set AzureStorage options.");
                    return new AzureBlobTranscriptStore(EventBotOptions.AzureStorageOptions.DataConnectionString, EventBotOptions.AzureStorageOptions.TranscriptContainerName);
                default:
                    throw new NotSupportedException($"A transcript storage type '{EventBotOptions.TranscriptStorageType.ToString("G")}' is not supported.");
            }
        }

        private static IStorage BotStatesStorageFactory(IServiceProvider serviceProvider)
        {
            var EventBotOptions = serviceProvider.GetRequiredService<IOptions<EventBotOptions>>().Value;
            switch (EventBotOptions.BotStatesStorageType)
            {
                case BotStorageType.InMemory:
                    return new MemoryStorage();
                case BotStorageType.DocumentDb:
                    if (EventBotOptions.DocumentDbOptions == null) throw new ArgumentException($"EventBot storage type is set to use CosmosDB, but configuration is not set. Use {nameof(EventBotOptions)}.{nameof(EventBotOptions.DocumentDbOptions)} and set CosmosDB options.");
                    var EventBotCosmosDbStorageOptions = new CosmosDbStorageOptions
                    {
                        CosmosDBEndpoint = EventBotOptions.DocumentDbOptions.ServiceEndpoint,
                        AuthKey = EventBotOptions.DocumentDbOptions.ServiceAuthKey,
                        CollectionId = EventBotOptions.DocumentDbOptions.CollectionId,
                        DatabaseId = EventBotOptions.DocumentDbOptions.DatabaseId
                    };
                    return new CosmosDbStorage(EventBotCosmosDbStorageOptions);
                case BotStorageType.StorageBlob:
                    if (EventBotOptions.AzureStorageOptions == null) throw new ArgumentException($"EventBot storage type is set to use AzureStorage, but configuration is not set. Use {nameof(EventBotOptions)}.{nameof(EventBotOptions.AzureStorageOptions)} and set AzureStorage options.");
                    return new AzureBlobStorage(EventBotOptions.AzureStorageOptions.DataConnectionString, EventBotOptions.AzureStorageOptions.BotStatesStoreContainerName);
                default:
                    throw new NotSupportedException($"A bot states storage type '{EventBotOptions.BotStatesStorageType.ToString("G")}' is not supported.");
            }
        }

        private static IAdapterIntegration BotFrameworkAdapterIntegrationFactory(IServiceProvider serviceProvider)
        {
            var credentialProvider = new ConfigurationCredentialProvider(serviceProvider.GetService<IConfiguration>());
            var logger = serviceProvider.GetRequiredService<ILogger<IAdapterIntegration>>();

            var responseDefinitionProvider = serviceProvider.GetRequiredService<IResponseDefinitionProvider>();
            var eventBotStateAccessor = serviceProvider.GetRequiredService<EventBotStateAccessors>();

            var recognitionOptionsBuilder = serviceProvider.GetRequiredService<IRecognitionOptionsBuilder>();
            var recognizer = serviceProvider.GetRequiredService<Trask.Bot.Recognition.IRecognizer>();
            var intentRecognizerMiddleware = new IntentRecognizerMiddleware(recognitionOptionsBuilder, recognizer);

            var intentProcessorFactory = serviceProvider.GetRequiredService<IntentProcessorFactory>();
            var eventBotOptions = serviceProvider.GetRequiredService<IOptions<EventBotOptions>>().Value;
            var intentTrie = new Trie<TrieIntent>();
            intentTrie.BuildTrie(eventBotOptions.IntentTreeFile);
            var processRecognizedIntentMiddleware = new EventAgentProcessRecognizedIntentMiddleware(responseDefinitionProvider, intentProcessorFactory, eventBotStateAccessor, intentTrie);

            var transcriptLoggerMiddleware = new TranscriptLoggerMiddleware(serviceProvider.GetRequiredService<ITranscriptLogger>());

            return
                new BotFrameworkAdapter(credentialProvider, null, null, null, null, logger)
                {
                    OnTurnError = BotFrameworkExceptionCallbackFactory(serviceProvider)
                }
                .Use(new TypingMiddleware())
                .Use(transcriptLoggerMiddleware)
                .Use(intentRecognizerMiddleware)
                .Use(processRecognizedIntentMiddleware);
        }

        private static Func<ITurnContext, Exception, Task> BotFrameworkExceptionCallbackFactory(IServiceProvider serviceProvider)
        {
            Func<ITurnContext, Exception, Task> onExceptionCallback = async (turnContext, exception) =>
            {
                var telemetryClient = serviceProvider.GetRequiredService<TelemetryClient>();
                telemetryClient.TrackEvent(ApplicationInsightsEventTelemetryBuilder.BuildUnhandledErrorEvent(turnContext.Activity.Conversation.Id, turnContext.Activity.From.Id));

                var logger = serviceProvider.GetRequiredService<ILogger<IAdapterIntegration>>();
                logger.LogError($"Exception caught : {exception}");

                var responseDefinitionProvider = serviceProvider.GetRequiredService<IResponseDefinitionProvider>();
                var definitions = await responseDefinitionProvider.QueryAsync(AgentConstantNames.TopicName, AgentConstantNames.WelcomeIntentName, AgentConstantNames.WelcomeIntentStates.UnhandledError.ToString(), turnContext.Activity.ChannelId, true);
                await turnContext.SendActivitiesAsync(new ResponseTemplateParameters(), definitions.ToArray(), default(System.Threading.CancellationToken));

                if (exception is StackOverflowException)
                {
                    var applicationLifeTime = serviceProvider.GetRequiredService<IApplicationLifetime>();
                    if (applicationLifeTime != null)
                    {
                        applicationLifeTime.StopApplication();
                    }
                }
            };
            return onExceptionCallback;
        }
    }
}

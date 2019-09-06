using Microsoft.Bot.Builder;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Trask.Bot.EventBot.Models;
using Trask.Bot.EventBot.Recognition.Trie;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;
using Trask.Bot.Services;
using Trask.Bot.Services.Microsoft.BotMiddlewares;

namespace Trask.Bot.EventBot.Recognition
{
    public class EventAgentProcessRecognizedIntentMiddleware : ProcessRecognizedIntentMiddleware
    {
        //TODO move to event bot nlp qna options
        private const float Threshold = 0.25f;
        private readonly IStatePropertyAccessor<EventStateUserData> eventAgentUserStateAccessor;
        private readonly Trie<TrieIntent> trie;

        public EventAgentProcessRecognizedIntentMiddleware(IResponseDefinitionProvider responseDefinitionProvider, IntentProcessorFactory intentProcessorFactory, EventBotStateAccessors eventBotStateAccessors, Trie<TrieIntent> trie)
            : base(responseDefinitionProvider, intentProcessorFactory, eventBotStateAccessors.ConversationCustomDataState)
        {
            if (eventBotStateAccessors == null)
            {
                throw new ArgumentNullException(nameof(eventBotStateAccessors));
            }

            eventAgentUserStateAccessor = eventBotStateAccessors.EventAgentUserState ?? throw new ArgumentNullException(nameof(EventBotStateAccessors.EventAgentUserState));
            this.trie = trie ?? throw new ArgumentNullException(nameof(trie));

        }

        protected override async Task<IntentContext> EnsureProcessorIntentContextAsync(ITurnContext turnContext, IntentRecognizedResult intentRecognizedResult, CancellationToken cancellationToken)
        {
            var conversationCustomDataState = await conversationCustomDataStateAccessor.GetAsync(turnContext, () => new ConversationCustomDataState(), cancellationToken).ConfigureAwait(false);
            var intentContext = conversationCustomDataState.ActiveIntentContext ?? await EnsureDefaultIntentContextAsync(turnContext, cancellationToken);
            intentContext.HasChanges = true;

            if (turnContext.Activity.Type != Microsoft.Bot.Schema.ActivityTypes.ConversationUpdate)
            {
                var recognizedIntent = intentRecognizedResult.Intents.FirstOrDefault();
                if (recognizedIntent != null && recognizedIntent.Value.Split('.')[0] != AgentConstantNames.FeedbackIntentName)
                {
                    recognizedIntent.Value = trie.GetValueByExactKey(recognizedIntent.Value, conversationCustomDataState.PreviousIntentName).Name;
                }
                intentContext.IntentName = GetProcessorIntentName(recognizedIntent);

                switch (intentContext.IntentName)
                {
                    default:
                        intentContext.StoredEntities.Clear();
                        intentContext.StoredEntities.Add(IntentRequestEntityNames.RequestText, new EntityObject { UnderlayingObject = intentRecognizedResult.Utterance });
                        intentContext.IntentState = recognizedIntent?.Value;
                        intentContext.StoredEntities.Add(IntentRequestEntityNames.IntentsReport, new EntityObject { UnderlayingObject = intentRecognizedResult.Intents });
                        conversationCustomDataState.ActiveIntentContext = intentContext;
                        break;
                    case AgentConstantNames.FeedbackIntentName:
                        intentContext.StoredEntities.Add(IntentRequestEntityNames.PreviousIntent, new EntityObject { UnderlayingObject = conversationCustomDataState.PreviousIntentName });
                        intentContext.StoredEntities.Add(IntentRequestEntityNames.SuggestionFeedback, new EntityObject{UnderlayingObject = recognizedIntent?.Value.Split('.')[1]});
                        conversationCustomDataState.ActiveIntentContext = null;
                        break;
                    case AgentConstantNames.UnrecognizedIntentName:
                        intentContext.StoredEntities.Clear();
                        intentContext.IntentState = AgentConstantNames.UnrecognizedIntentStates.IntentNotRecognized.ToString("G");
                        intentContext.NotStoredEntities.Add(IntentRequestEntityNames.RequestText, intentRecognizedResult.Utterance);
                        conversationCustomDataState.ActiveIntentContext = null;
                        break;
                }
            }

            conversationCustomDataState.PreviousIntentName = intentContext.IntentState;
            return intentContext;
        }

       
        private string GetProcessorIntentName(IIntent recognizedIntent)
        {
            return recognizedIntent == null ? AgentConstantNames.UnrecognizedIntentName : recognizedIntent.Value.Split('.')[0];
        }

        protected override async Task<IntentContext> EnsureDefaultIntentContextAsync(ITurnContext turnContext, CancellationToken cancellationToken)
        {
            var intentContext = new IntentContext
            {
                IntentName = AgentConstantNames.WelcomeIntentName,
                IntentState = turnContext.Activity.Type == Microsoft.Bot.Schema.ActivityTypes.ConversationUpdate ? AgentConstantNames.WelcomeIntentStates.WelcomeUser.ToString() : AgentConstantNames.WelcomeIntentStates.Unknown.ToString(),
                TopicName = AgentConstantNames.TopicName,
                HasChanges = true
            };
            var eventStateUserData = await eventAgentUserStateAccessor.GetAsync(turnContext, () => new EventStateUserData(), cancellationToken).ConfigureAwait(false);
            intentContext.NotStoredEntities.Add(IntentRequestEntityNames.UserData, eventStateUserData);

            return intentContext;
        }
    }
}
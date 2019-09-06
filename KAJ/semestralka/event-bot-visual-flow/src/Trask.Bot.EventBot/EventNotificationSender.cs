using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;
using Microsoft.Bot.Connector.Authentication;
using Microsoft.Bot.Schema;
using Newtonsoft.Json.Linq;
using Trask.Bot.Event.Services;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot
{
    public class EventNotificationSender : IEventNotificationSender
    {
        private readonly BotFrameworkAdapter botAdapter;
        private readonly MicrosoftAppCredentials microsoftAppCredentials;

        private readonly IDocumentRepository documentRepository;

        public EventNotificationSender(BotFrameworkAdapter botAdapter, MicrosoftAppCredentials microsoftAppCredentials, IDocumentRepository documentRepository)
        {
            this.botAdapter = botAdapter ?? throw new ArgumentNullException(nameof(botAdapter));
            this.microsoftAppCredentials = microsoftAppCredentials ?? throw new ArgumentNullException(nameof(microsoftAppCredentials));
            this.documentRepository = documentRepository ?? throw new ArgumentNullException(nameof(documentRepository));
        }

        public async Task SendNotificationsAsync(params EventNotification[] notifications)
        {
            Trace.TraceInformation("Configuring tasks to send notification");
            var tasksToSendNotification = new List<Task>();
            foreach (var notification in notifications)
            {
                //TODO implementovat notifikace pro list registrovanych uzivatelu
//                if (notification.ForRegisteredOnly)
//                {
//                    var allRegistrations = await documentRepository.QueryAsync<EventRegistration>(r => r.Type == EventRegistration.TypeName && r.EventDefinitionId == notification.EventDefinitionId);
//                    var contextTasks = allRegistrations
//                        .Select(r => (JObject)r.FormData["channel"])
//                        .Select(BuildConversationReference)
//                        .Select(cr => new BotFrameworkBotContext(microsoftAppCredentials.MicrosoftAppId, botAdapter, cr.GetPostToBotMessage()))
//                        .Select(cr => cr.SendActivity(notification.Message));
//                    tasksToSendNotification.AddRange(contextTasks);
//                }
            }
            await Task.WhenAll(tasksToSendNotification);
        }

        private static ConversationReference BuildConversationReference(JObject channel)
        {
            return new ConversationReference
            {
                ActivityId = Guid.NewGuid().ToString(),
                User = new ChannelAccount((string)channel.GetValue("fromId")),
                Bot = new ChannelAccount(id: "88g26iaa08nk", name: "Bot"),
                ChannelId = (string)channel.GetValue("channelId"),
                ServiceUrl = (string)channel.GetValue("serviceUrl"),
                Conversation = new ConversationAccount { Id = (string)channel.GetValue("conversationId") }
            };
        }
    }
}

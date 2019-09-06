using System;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;
using Microsoft.Bot.Schema;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot.Models
{
//    public class TopicMiddleware : BaseTopicMiddleware
//    {
//        private readonly IDocumentRepository documentRepository;
//
//        public TopicMiddleware(IDocumentRepository documentRepository)
//        {
//            this.documentRepository = documentRepository ?? throw new System.ArgumentNullException(nameof(documentRepository));
//        }
//
//        protected override async Task<bool> ContinueTopic(IBotContext context, string activeTopicName)
//        {
//            switch (activeTopicName)
//            {
//                case TopicNames.EventRegistrationTopic:
//                    ((EventRegistrationTopic)context.GetConversationState<ConversationData>().ActiveTopic).SetDocumentRepository(documentRepository);
//                    break;
//                case TopicNames.EventAgendaTopic:
//                    ((EventAgendaTopic)context.GetConversationState<ConversationData>().ActiveTopic).SetDocumentRepository(documentRepository);
//                    break;
//            }
//            return await context.GetConversationState<ConversationData>().ActiveTopic.ContinueTopic(context).ConfigureAwait(false);
//        }
//
//        protected override async Task<bool> StartTopic(IBotContext context, string newTopicName)
//        {
//            ITopic newActiveTopic = null;
//            switch (newTopicName)
//            {
//                case TopicNames.EventRegistrationTopic:
//                    {
//                        var topic = new EventRegistrationTopic();
//                        topic.SetDocumentRepository(documentRepository);
//                        newActiveTopic = topic;
//                    }
//                    break;
//                case TopicNames.EventAgendaTopic:
//                    {
//                        var topic = new EventAgendaTopic();
//                        topic.SetDocumentRepository(documentRepository);
//                        newActiveTopic = topic;
//                    }
//                    break;
//                default:
//                    if (ActivityConversationUpdateButUserEmailOnActualChannelNotExists(context))
//                    {
//                        newActiveTopic = new EventPromptEmailTopic();
//                    }
//                    else
//                    {
//                        newActiveTopic = new DefaultTopic();
//                    }
//                    break;
//            }
//            context.GetConversationState<ConversationData>().ActiveTopic = newActiveTopic;
//            return await context.GetConversationState<ConversationData>().ActiveTopic.StartTopic(context).ConfigureAwait(false);
//        }
//
//        private static bool ActivityConversationUpdateButUserEmailOnActualChannelNotExists(IBotContext context)
//        {
//            var conversationUpdateActivity = context.Request.AsConversationUpdateActivity();
//            return conversationUpdateActivity != null &&
//                   context.Request.MembersAdded.Any(m => m.Id == context.Request.Recipient.Id) &&
//                   (!context.GetUserState<UserData>().EventRegistrationEmails.ContainsKey(context.Request.ChannelId) || !context.GetUserState<UserData>().EventRegistrationEmails[context.Request.ChannelId].Any());
//        }
//    }
}
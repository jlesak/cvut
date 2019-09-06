using System.Linq;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;

namespace Trask.Bot.EventBot.Models
{
//    public abstract class BaseTopicMiddleware : IMiddleware
//    {
//        public async Task OnProcessRequest(IBotContext context, MiddlewareSet.NextDelegate next)
//        {
//            bool handled;
//            var activeTopic = context.GetConversationState<ConversationData>().ActiveTopic;
//            var topIntentTopicName = GetTopIntentTopicName(context);
//            var activeTopicName = activeTopic?.Name;
//            if (!string.IsNullOrEmpty(activeTopicName) &&
//                (string.IsNullOrEmpty(topIntentTopicName) || activeTopicName == topIntentTopicName))
//            {
//                handled = await ContinueTopic(context, activeTopicName).ConfigureAwait(false);
//            }
//            else
//            {
//                handled = await StartTopic(context, topIntentTopicName).ConfigureAwait(false);
//            }
//
//            if (!handled && !(context.GetConversationState<ConversationData>().ActiveTopic is DefaultTopic))
//            {
//                context.GetConversationState<ConversationData>().ActiveTopic = new DefaultTopic();
//                handled = await context.GetConversationState<ConversationData>().ActiveTopic.ResumeTopic(context).ConfigureAwait(false);
//            }
//
//            await next().ConfigureAwait(false);
//        }
//
//        private string GetTopIntentTopicName(IBotContext context)
//        {
//            var recognizedIntents = context.Get<IRecognizedIntents>();
//            var topIntent = recognizedIntents?.TopIntent;
//            if (topIntent != null)
//            {
//                var metadataEntity = topIntent.Entities.FirstOrDefault(e => e.GroupName == QnAEntityGroupNames.Metadata);
//                if (metadataEntity != null && metadataEntity.ContainsKey(QnAMetadataNames.Topic))
//                {
//                    var topicFromMetadas = metadataEntity[QnAMetadataNames.Topic];
//                    return topicFromMetadas;
//                }
//            }
//            return string.Empty;
//        }
//
//        protected abstract Task<bool> ContinueTopic(IBotContext context, string activeTopicName);
//        protected abstract Task<bool> StartTopic(IBotContext context, string newTopicName);
//    }
}
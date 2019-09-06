using System;
using System.Diagnostics;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;
using Microsoft.Bot.Schema;
using Newtonsoft.Json;
using Trask.Bot.Schema;

namespace Trask.Bot.EventBot.Models
{
//    public class DefaultTopic : ITopic
//    {
//        public string Name { get; set; } = TopicNames.DefaultTopic;
//
//        public Task<bool> ContinueTopic(ITurnContext context)
//        {
//            if (ActivityMessageWithRecognizedIntentWasNotResponded(context))
//            {
//                Trace.TraceWarning($"Bot on channel '{context.Request.ChannelId}' in conversation '{context.Request.Conversation.Id}' with user '{context.Request.From.Name ?? context.Request.From.Id}' => top intent is not set.\r\n{{'Request': '{JsonConvert.SerializeObject(context.Request)}'}}");
//                Console.WriteLine($"Bot on channel '{context.Request.ChannelId}' in conversation '{context.Request.Conversation.Id}' with user '{context.Request.From.Name ?? context.Request.From.Id}' => top intent is not set.\r\n{{'Request': '{JsonConvert.SerializeObject(context.Request)}'}}");
//                context.SendActivity($"Omlouvám se, ale úplně Vám nerozumím. Zkuste napsat pomoc, nebo se ptejte na věci týkající se údálosti 😉");
//            }
//
//            return Task.FromResult(true);
//        }
//
//        private static bool ActivityMessageWithRecognizedIntentWasNotResponded(IBotContext context)
//        {
//            return context.Request.Type == ActivityTypes.Message && context.Get<IRecognizedIntents>().TopIntent == null;
//        }
//
//        public Task<bool> ResumeTopic(IBotContext context)
//        {
//            return Task.FromResult(true);
//        }
//
//        public Task<bool> StartTopic(IBotContext context)
//        {
//            return Task.FromResult(true);
//        }
//
//        public static async Task<bool> SetAndStartDefaultTopic(IntentContext context)
//        {
//            return await context.GetConversationState<ConversationData>().ActiveTopic.StartTopic(context).ConfigureAwait(false);
//        }
//
//        public static async Task<bool> SetAndContinueDefaultTopic(IBotContext context)
//        {
//            return await context.GetConversationState<ConversationData>().ActiveTopic.ContinueTopic(context).ConfigureAwait(false);
//        }
//    }
}
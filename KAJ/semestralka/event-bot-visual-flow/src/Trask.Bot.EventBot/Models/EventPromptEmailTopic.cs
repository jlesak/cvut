using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;
using Microsoft.Bot.Schema;

namespace Trask.Bot.EventBot.Models
{
//    public class EventPromptEmailTopic : ITopic
//    {
//        public string Name { get; set; } = TopicNames.EventPromptEmailTopic;
//
//        public async Task<bool> ContinueTopic(IBotContext context)
//        {
//            if (context.Request.Type == ActivityTypes.ConversationUpdate)
//            {
//                await context.SendActivity($"Nedokážu Vás zatím rozpoznat, pokud jste již registrován na nadcházející událost, poprosím o Váši emailovou adresu");
//                return true;
//            } 
//
//            if (!context.GetUserState<UserData>().EventRegistrationEmails.ContainsKey(context.Request.ChannelId) ||
//                !context.GetUserState<UserData>().EventRegistrationEmails[context.Request.ChannelId].Any())
//            {
//                var email = context.Request.Text;
//                var regex = new Regex(@"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$", RegexOptions.Compiled);
//                if (regex.IsMatch(email))
//                {
//                    if (!context.GetUserState<UserData>().EventRegistrationEmails.ContainsKey(context.Request.ChannelId))
//                    {
//                        context.GetUserState<UserData>().EventRegistrationEmails.Add(context.Request.ChannelId, new List<string>());
//                    }
//                    context.GetUserState<UserData>().EventRegistrationEmails[context.Request.ChannelId].Add(email);
//                    return await DefaultTopic.SetAndStartDefaultTopic(context).ConfigureAwait(false);
//                }
//                else
//                {
//                    await context.SendActivity($"Toto není emailová adresa").ConfigureAwait(false);
//                    return true;
//                }
//            }
//            
//            return true;
//        }
//
//        public Task<bool> ResumeTopic(IBotContext context)
//        {
//            throw new System.NotImplementedException();
//        }
//
//        public async Task<bool> StartTopic(IBotContext context)
//        {
//            if (context.Request.ChannelId == "directline" || context.Request.ChannelId == "webchat")
//            {
//                context.GetConversationState<ConversationData>().ActiveTopic = new DefaultTopic();
//                return await context.GetConversationState<ConversationData>().ActiveTopic.StartTopic(context).ConfigureAwait(false);
//            }
//            return await ContinueTopic(context).ConfigureAwait(false);
//        }
//    }
}
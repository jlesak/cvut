using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;
using Microsoft.Bot.Schema;
using Trask.Bot.EventBot.Responses;

namespace Trask.Bot.EventBot.Models
{
    public class EventPresentation
    {
        public string Name { get; set; }
        public int TotalSlides { get; set; }
        //public IDictionary<int, AdaptiveCard> Slides { get; set; }
    }

//    public class PresentationTopic : ITopic
//    {
//        public string Name { get; set; } = TopicNames.EventPresentationTopic;
//        public int CurrentSlide { get; set; } = 1;
//        public int totalSlides = 5;
//
//        public Task<bool> ContinueTopic(IBotContext context)
//        {
//            if (context.Request.Type == ActivityTypes.Message)
//            {
//                var userText = context.Request.AsMessageActivity().Text?.ToLowerInvariant();
//                if (Regex.IsMatch(userText, "dalsi(.*)|další(.*)"))
//                {
//                    if (CurrentSlide < totalSlides)
//                    {
//                        PresentationTopicResponses.ReplyWithSlide(context, CurrentSlide + 1);
//                        CurrentSlide++;
//                    }
//                    else
//                    {
//                        PresentationTopicResponses.ReplyWithLastSlide(context);
//                    }
//                    return Task.FromResult(true);
//                }
//                if (Regex.IsMatch(userText, "predchozi(.*)|předchozí(.*)|zpet(.*)|zpět(.*)"))
//                {
//                    if (CurrentSlide > 1)
//                    {
//                        PresentationTopicResponses.ReplyWithSlide(context, CurrentSlide - 1);
//                        CurrentSlide--;
//                    }
//                    else
//                    {
//                        PresentationTopicResponses.ReplyWithFirstSlide(context);
//                    }
//                    return Task.FromResult(true);
//                }
//                if (Regex.IsMatch(userText, "zacatek(.*)|začátek(.*)|prvni(.*)|první(.*)"))
//                {
//                    PresentationTopicResponses.ReplyWithSlide(context, 1);
//                    CurrentSlide = 1;
//                    return Task.FromResult(true);
//                }
//                if (Regex.IsMatch(userText, "posledni(.*)|poslední(.*)"))
//                {
//                    PresentationTopicResponses.ReplyWithSlide(context, totalSlides);
//                    CurrentSlide = totalSlides;
//                    return Task.FromResult(true);
//                }
//                if (Regex.IsMatch(userText, "vypni(.*)|konec(.*)"))
//                {
//                    var activeTopic = new DefaultTopic();
//                    context.GetConversationState<ConversationData>().ActiveTopic = activeTopic;
//                    PresentationTopicResponses.ReplyWithEndOFPresentaion(context);
//                    //return activeTopic.StartTopic(context);
//                    return Task.FromResult(true);
//                }
//                PresentationTopicResponses.ReplyWithSlide(context, 1);
//                CurrentSlide = 1;
//            }
//            return Task.FromResult(true);
//        }
//
//        public Task<bool> ResumeTopic(IBotContext context)
//        {
//            throw new System.NotImplementedException();
//        }
//
//        public Task<bool> StartTopic(IBotContext context)
//        {
//            if (context.Request.Type == ActivityTypes.Message)
//            {
//                PresentationTopicResponses.ReplyWithPrompt(context);
//            }
//
//            return Task.FromResult(true);
//        }
//    }
}
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using Microsoft.ApplicationInsights.DataContracts;
using Trask.Bot.Recognition.Intent;

namespace Trask.Bot.EventBot
{
    public class ApplicationInsightsEventTelemetryBuilder
    {
        private const string UnhandledErrorEventTelemetryName = "EventAgent.UnhandledError";
        private const string UserFeedbackEventTelemetryName = "EventAgent.UserFeedback";
        private const string RecognizedIntentEventTelemetryName = "EventAgent.RecognizedIntent";
        private const string UnrcognizedIntentEventTelemetryName = "EventAgent.UnrecognizedIntent";


        public static EventTelemetry BuildUnhandledErrorEvent(string conversationId, string userId)
        {
            var eventTelemetry = new EventTelemetry(UnhandledErrorEventTelemetryName);
            eventTelemetry.Timestamp = DateTime.UtcNow;
            eventTelemetry.Properties.Add("ConversationId", conversationId);
            eventTelemetry.Properties.Add("UserId", userId);
            return eventTelemetry;
        }
        
        public static EventTelemetry BuildUnrecognizedIntentEvent(string requestText)
        {
            var eventTelemetry = new EventTelemetry(UnrcognizedIntentEventTelemetryName);
            eventTelemetry.Timestamp = DateTime.UtcNow;
            eventTelemetry.Properties.Add("RequestText", requestText);
            return eventTelemetry;
        }
        
        public static EventTelemetry BuildUserFeedbackEvent(string requestText, string respondedIntent, string feedback, List<IIntent> intentsReport)
        {
            var eventTelemetry = new EventTelemetry(UserFeedbackEventTelemetryName);
            eventTelemetry.Timestamp = DateTime.UtcNow;
            eventTelemetry.Properties.Add("RecognizedIntents", string.Join(", ", intentsReport.Select(x => $"[intent: {x.Value}, score: {x.Score}]")));
            eventTelemetry.Properties.Add("Feedback", feedback);
            eventTelemetry.Properties.Add("RespondedIntent", respondedIntent);
            eventTelemetry.Properties.Add("RequestText", requestText);
            return eventTelemetry;
        }
        public static EventTelemetry BuildRecognizedIntentEvent(string requestText, string respondedIntent, List<IIntent> intentsReport)
        {
            var eventTelemetry = new EventTelemetry(RecognizedIntentEventTelemetryName);
            eventTelemetry.Timestamp = DateTime.UtcNow;
            eventTelemetry.Properties.Add("RecognizedIntents", string.Join(", ", intentsReport.Select(x => $"[intent: {x.Value}, score: {x.Score}]")));
            eventTelemetry.Properties.Add("RespondedIntent", respondedIntent);
            eventTelemetry.Properties.Add("RequestText", requestText);
            return eventTelemetry;
        }
    }
}
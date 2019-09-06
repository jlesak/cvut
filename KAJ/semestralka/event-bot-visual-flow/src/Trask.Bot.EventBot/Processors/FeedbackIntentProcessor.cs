using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ApplicationInsights;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;
using Trask.Bot.State;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot.Processors
{
    public class FeedbackIntentProcessor : IIntentProcessor
    {
        private readonly TelemetryClient telemetryClient;

        public FeedbackIntentProcessor(TelemetryClient telemetryClient)
        {
            this.telemetryClient = telemetryClient ?? throw new ArgumentNullException(nameof(telemetryClient));
        }

        public string IntentName => AgentConstantNames.FeedbackIntentName;

        public bool IsFallbackProcessor => false;

        public async Task ProcessIntent(IntentContext intentContext)
        {
            var suggestion = intentContext.Entities.ContainsKey(IntentRequestEntityNames.SuggestionFeedback)
                ? intentContext.Entities[IntentRequestEntityNames.SuggestionFeedback].ToString()
                : null;
            intentContext.IntentState = AgentConstantNames.FeedbackIntentState.Default.ToString("G");

            switch (suggestion)
            {
                case "👍":
                    await StoreFeedback(intentContext, "thumbsUp").ConfigureAwait(false);
                    return;
                case "👎":
                    await StoreFeedback(intentContext, "thumbsDown").ConfigureAwait(false);
                    return;
            }
        }

        private async Task StoreFeedback(IntentContext intentContext, string feedback)
        {
            string requestText = intentContext.Entities[IntentRequestEntityNames.RequestText].ToString();
            string respondedIntent = intentContext.Entities[IntentRequestEntityNames.PreviousIntent].ToString();
            List<IIntent> intentsReport = (List<IIntent>) intentContext.Entities[IntentRequestEntityNames.IntentsReport];

            var eventTelemetry = ApplicationInsightsEventTelemetryBuilder.BuildUserFeedbackEvent(requestText, respondedIntent, feedback, intentsReport);
            telemetryClient.TrackEvent(eventTelemetry);
        }

        
        public bool IsFinalState(object value)
        {
            return false;
        }

        
        public bool IsSwitchToDefaultIntentState(object value)
        {
            return false;
        }
    }
}
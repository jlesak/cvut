using System;
using System.Diagnostics;
using System.Threading.Tasks;
using Microsoft.ApplicationInsights;
using Newtonsoft.Json;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;

namespace Trask.Bot.EventBot.Processors
{
    public class UnrecognizedIntentProcessor : IIntentProcessor
    {
        public string IntentName => AgentConstantNames.UnrecognizedIntentName;
        public bool IsFallbackProcessor => false;
        private readonly TelemetryClient telemetryClient;

        public UnrecognizedIntentProcessor(TelemetryClient telemetryClient)
        {
            this.telemetryClient = telemetryClient ?? throw new ArgumentNullException(nameof(telemetryClient));
        }
        public Task ProcessIntent(IntentContext intentContext)
        {
            
            string requestText = intentContext.Entities[IntentRequestEntityNames.RequestText].ToString();

            var eventTelemetry = ApplicationInsightsEventTelemetryBuilder.BuildUnrecognizedIntentEvent(requestText);
            telemetryClient.TrackEvent(eventTelemetry);
            
            return Task.CompletedTask;
        }

        public bool IsFinalState(object state)
        {
            return false;
        }

        public bool IsSwitchToDefaultIntentState(object state)
        {
            return false;
        }
    }
}
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading.Tasks;
using Microsoft.ApplicationInsights;
using Newtonsoft.Json;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;

namespace Trask.Bot.EventBot.Processors
{
    // QnA na otázky týkající se eventu: jídlo, místo, parkování, počasí...
    public class EventGuideProcessor : IIntentProcessor
    {
        public string IntentName => AgentConstantNames.EventIntentName;
        public bool IsFallbackProcessor => false;
        private readonly TelemetryClient telemetryClient;

        public EventGuideProcessor(TelemetryClient telemetryClient)
        {
            this.telemetryClient = telemetryClient ?? throw new ArgumentNullException(nameof(telemetryClient));
        }
        public Task ProcessIntent(IntentContext intentContext)
        {
            IntentProcessorUtils.LogRecognizedIntent(intentContext, telemetryClient);
            IntentProcessorUtils.SetTextResponse(intentContext, intentContext.IntentState);
            
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
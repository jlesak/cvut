using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ApplicationInsights;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;

namespace Trask.Bot.EventBot.Processors
{
 
    public class GeneralProcessor : IIntentProcessor
    {
        public string IntentName => AgentConstantNames.GeneralIntentName;
        public bool IsFallbackProcessor => true;
        private readonly TelemetryClient telemetryClient;

        public GeneralProcessor(TelemetryClient telemetryClient)
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
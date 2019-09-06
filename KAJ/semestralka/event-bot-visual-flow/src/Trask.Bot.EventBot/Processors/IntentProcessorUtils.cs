using System.Collections.Generic;
using Microsoft.ApplicationInsights;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.EventBot.Responses;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;

namespace Trask.Bot.EventBot.Processors
{
    public static class IntentProcessorUtils
    {
        public static void SetTextResponse(IntentContext intentContext, string textResponse)
        {
            Dictionary<string, string> items = new Dictionary<string, string>
            {
                {IntentResponseEntityNames.Response, textResponse}
            };
            intentContext.ResponseDefinitionParameters.Body.Clear();
            intentContext.ResponseDefinitionParameters.Body.Add(new ResponseTemplateParameter());
            intentContext.ResponseDefinitionParameters.Body[0].Items.Add(items);
           
            intentContext.IntentName = "TextResponse";
            intentContext.IntentState = "TextResponse";
        }

        public static void LogRecognizedIntent(IntentContext intentContext, TelemetryClient telemetryClient)
        {
            string requestText = intentContext.Entities[IntentRequestEntityNames.RequestText].ToString();
            List<IIntent> intentsReport = (List<IIntent>) intentContext.Entities[IntentRequestEntityNames.IntentsReport];
            var eventTelemetry = ApplicationInsightsEventTelemetryBuilder.BuildRecognizedIntentEvent(requestText, intentContext.IntentState, intentsReport);
            telemetryClient.TrackEvent(eventTelemetry);
        }
    }
}
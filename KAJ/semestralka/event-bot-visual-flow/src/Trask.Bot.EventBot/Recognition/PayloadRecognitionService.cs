using System;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Threading;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using Trask.Bot.Recognition;
using Trask.Bot.Recognition.Intent;

namespace Trask.Bot.EventBot.Recognition
{
    public class PayloadRecognitionService : IIntentRecognitionService
    {
        public Task<IntentRecognizedResult> Query(IRecognitionOptions options, CancellationToken token)
        {
            var payload = options.Metadata.FirstOrDefault(m => m.Name == EventAgentIntentRecognitionOptionsBuilder.PayloadMetadataName)?.Value as JObject;
            if (payload == null) return Task.FromResult(new IntentRecognizedResult());

            var recognizedIntentResult = new IntentRecognizedResult();

            recognizedIntentResult.Utterance = payload.ToString();
            var payloadAction = payload.GetValue("action")?.ToString();
            recognizedIntentResult.Entities.Add(new RecognizedEntity { Name = IntentRequestEntityNames.ActionName, Value = payloadAction });
            switch (payloadAction)
            {
                case null:
                case "":
                    return Task.FromResult(new IntentRecognizedResult());
                case "Feedback":
                    string feedback = payload.GetValue(IntentRequestEntityNames.SuggestionFeedback).ToString();
                    recognizedIntentResult.Intents.Add(BuildIntent($"{AgentConstantNames.FeedbackIntentName}.{feedback}", 1.0f));
                    recognizedIntentResult.Entities.Add(new RecognizedEntity { Name = IntentRequestEntityNames.SuggestionFeedback, Value = feedback });
                    break;
                
            }

            return Task.FromResult(recognizedIntentResult);
        }

        private static RecognizedIntent BuildIntent(string name, float score)
        {
            var recognizedIntent = new RecognizedIntent { Value = name, Score = score};
            return recognizedIntent;
        }
    }
}
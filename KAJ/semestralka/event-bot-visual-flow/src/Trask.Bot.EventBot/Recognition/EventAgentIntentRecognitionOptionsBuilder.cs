using Microsoft.Bot.Schema;
using Newtonsoft.Json.Linq;
using Trask.Bot.Recognition;
using Trask.Bot.Services.Microsoft.Recognition;

namespace Trask.Bot.EventBot.Recognition
{
    public class EventAgentIntentRecognitionOptionsBuilder : IntentRecognitionOptionsBuilder
    {
        public const string PayloadMetadataName = "Payload";
        
        public override IRecognitionOptions BuildOptions(Activity incomingActivity)
        {
            var recognitionOptions = base.BuildOptions(incomingActivity);
            if (string.IsNullOrEmpty(incomingActivity.Text) && incomingActivity.Value != null)
            {
                recognitionOptions.Metadata.Add(new RecognitionMetadata { Name = PayloadMetadataName, Value = (JObject)incomingActivity.Value });
            }
            return recognitionOptions;
        }

    }
}
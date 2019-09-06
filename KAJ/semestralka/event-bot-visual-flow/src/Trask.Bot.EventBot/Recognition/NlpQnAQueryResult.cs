using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using Trask.Bot.QnA;
using Trask.Bot.Recognition;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Services.Microsoft.Recognition;

namespace Trask.Bot.EventBot.Recognition
{
    public class NlpQnAQueryResult : IQnAQueryResult
    {
        [JsonProperty("modelname")]
        public string[] Modelname { get; set; }
        
        [JsonProperty("sentence")]
        public string[] Sentence { get; set; }

        [JsonProperty("sentencenorm")]
        public string[] Sentencenorm { get; set; }
        
        [JsonProperty("sentenceOptText")]
        public string[] SentenceOptText { get; set; }

        [JsonProperty("intentsReport")]
        public IntentsReport[] IntentsReport { get; set; }

        [JsonProperty("sentencesTop10Text")]
        public string[] SentencesTop10Text { get; set; }

        public string Answer { get; set; }
        public float Score { get; set; }
    }

    public class IntentsReport : IIntentConvertible
    {
        [JsonProperty("intent")]
        public string Intent { get; set; }

        [JsonProperty("Koef")]
        public double Koef { get; set; }

        [JsonProperty("Aggreg %")]
        public string Aggreg { get; set; }

        [JsonProperty("AggregNorm %")]
        public string AggregNorm { get; set; }
        
        public TIntent ToIntent<TIntent>() where TIntent : IIntent, new()
        {
            return new TIntent {Name = "intent", Value = Intent, Score = float.Parse(Aggreg.Trim('%'))/100, Parameters = BuildIntentParameters().ToList()};
        }

        private IEnumerable<RecognitionMetadata> BuildIntentParameters()
        {
            return new List<RecognitionMetadata>
            {
                new RecognitionMetadata
                {
                    Name = "Coefficient",
                    Value = Koef
                },
                new RecognitionMetadata
                {
                    Name = "AggregNorm",
                    Value = AggregNorm
                }
            };
        }

        public IEnumerable<TEntity> ToEntities<TEntity>() where TEntity : IEntity, new()
        {
            throw new System.NotImplementedException();
        }
    }
}
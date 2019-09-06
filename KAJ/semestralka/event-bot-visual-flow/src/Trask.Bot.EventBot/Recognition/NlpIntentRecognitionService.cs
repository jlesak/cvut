using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Trask.Bot.QnA;
using Trask.Bot.Recognition;
using Trask.Bot.Recognition.Intent;

namespace Trask.Bot.EventBot.Recognition
{
    public class NlpIntentRecognitionService : IIntentRecognitionService
    {
        private readonly IQnAProvider qnaProvider;

        public NlpIntentRecognitionService(IQnAProvider qnaProvider)
        {
            this.qnaProvider = qnaProvider ?? throw new System.ArgumentNullException(nameof(qnaProvider));
        }
        
        public async Task<IntentRecognizedResult> Query(IRecognitionOptions options, CancellationToken token)
        {
            var nlpQnAOptions = new NlpQnAOptions
            {
                Question = options.Utterance
                // ScoreThreshold = (float)options.Metadata.FirstOrDefault(x => x.Name == nameof(NlpQnAOptions.ScoreThreshold))?.Value,
                // NumberOfRankedResults = (int)options.Metadata.FirstOrDefault(x => x.Name == nameof(NlpQnAOptions.NumberOfRankedResults))?.Value
            }; 

            var qnaAnswer = await qnaProvider.GetAnswers<NlpQnAQueryResult>(nlpQnAOptions).ConfigureAwait(false);
            var result = new IntentRecognizedResult
            {
                AlteredUtterance = options.Utterance,
                Intents = qnaAnswer.SelectMany(a => a.IntentsReport.Select(x => x.ToIntent<RecognizedIntent>())).ToList<IIntent>()
            };
            return result;
        }
    }
}
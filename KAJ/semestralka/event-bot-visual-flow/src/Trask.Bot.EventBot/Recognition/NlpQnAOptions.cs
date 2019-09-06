using Trask.Bot.QnA;

namespace Trask.Bot.EventBot.Recognition
{
    public class NlpQnAOptions : IQnAQueryOptions
    {
        public string Question { get; set; }
        public int NumberOfRankedResults { get; set; }
        public float ScoreThreshold { get; set; }
    }
}
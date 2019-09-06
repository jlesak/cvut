using System.Threading.Tasks;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;

namespace Trask.Bot.EventBot.Processors
{
    public class SurveyProcessor : IIntentProcessor
    {
        public string IntentName => AgentConstantNames.SurveyIntentName;
        public bool IsFallbackProcessor => false;

        public Task ProcessIntent(IntentContext intentContext)
        {
            //TODO logika
            return Task.CompletedTask;
        }

        public bool IsFinalState(object state)
        {
            throw new System.NotImplementedException();
        }

        public bool IsSwitchToDefaultIntentState(object state)
        {
            throw new System.NotImplementedException();
        }

    }
}
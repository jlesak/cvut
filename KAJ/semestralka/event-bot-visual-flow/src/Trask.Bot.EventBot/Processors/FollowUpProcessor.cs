using System.Threading.Tasks;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;

namespace Trask.Bot.EventBot.Processors
{
    public class FollowUpProcessor : IIntentProcessor
    {
        public string IntentName => AgentConstantNames.FollowUpIntentName;
        public bool IsFallbackProcessor => false;

        public Task ProcessIntent(IntentContext intentContext)
        {
            
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
using System.Threading.Tasks;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;

namespace Trask.Bot.EventBot.Processors
{
    public class WelcomeIntentProcessor : IIntentProcessor
    {
        public string IntentName => AgentConstantNames.WelcomeIntentName;

        public bool IsFallbackProcessor => false;

        public bool IsFinalState(object state)
        {
            return false;
        }

        public bool IsSwitchToDefaultIntentState(object state)
        {
            return false;
        }

        public Task ProcessIntent(IntentContext intentContext)
        {
            return Task.CompletedTask;
        }
    }
}
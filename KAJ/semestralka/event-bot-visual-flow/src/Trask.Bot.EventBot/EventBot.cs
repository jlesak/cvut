using System.Threading;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;

namespace Trask.Bot.EventBot
{
    public class EventBot : IBot
    {
        private readonly EventBotStateAccessors stateAccessors;

        public EventBot(EventBotStateAccessors stateAccessors)
        {
            this.stateAccessors = stateAccessors ?? throw new System.ArgumentNullException(nameof(stateAccessors));
        }

        public async Task OnTurnAsync(ITurnContext turnContext, CancellationToken cancellationToken = default(CancellationToken))
        {
            await stateAccessors.ConversationState.SaveChangesAsync(turnContext).ConfigureAwait(false);
            await stateAccessors.UserState.SaveChangesAsync(turnContext).ConfigureAwait(false);
        }
    }
}
using Microsoft.Bot.Builder;
using Trask.Bot.EventBot.Models;

namespace Trask.Bot.EventBot
{
    public class EventBotStateAccessors
    {
        public EventBotStateAccessors(UserState userState, ConversationState conversationState)
        {
            UserState = userState ?? throw new System.ArgumentNullException(nameof(userState));
            ConversationState = conversationState ?? throw new System.ArgumentNullException(nameof(conversationState));

            ConversationCustomDataState = conversationState.CreateProperty<ConversationCustomDataState>(ConversationCustomDataStateName);
            EventAgentUserState = userState.CreateProperty<EventStateUserData>(EventAgentUserStateName);
        }

        private static string ConversationCustomDataStateName = $"{nameof(EventBotStateAccessors)}.{nameof(EventBotStateAccessors.ConversationCustomDataState)}";
        private static string EventAgentUserStateName = $"{nameof(EventBotStateAccessors)}.{nameof(EventBotStateAccessors.EventAgentUserState)}";

        public UserState UserState { get; }

        public ConversationState ConversationState { get; }

        public IStatePropertyAccessor<ConversationCustomDataState> ConversationCustomDataState { get; private set; }

        public IStatePropertyAccessor<EventStateUserData> EventAgentUserState { get; private set; }
    }
}

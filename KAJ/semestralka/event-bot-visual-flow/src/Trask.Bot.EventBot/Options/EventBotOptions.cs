using System.Text;
using Trask.Bot.Azure.Options;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.Options;

namespace Trask.Bot.EventBot.Options
{
    public class EventBotOptions : BotAzureOptions
    {
        public BotAuthenticationType AuthenticationType { get; set; }

        public BotStorageType AuthenticationDataStorageType { get; set; }

        public BotStorageType ResourceDefinitionStorageType { get; set; }

        public NlpQnAProviderOptions NlpQnAProviderOptions { get; set; }
         
        public string IntentTreeFile { get; set; }
    }
}

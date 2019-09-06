using System;
using System.Linq;
using System.Threading.Tasks;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;
using Trask.Bot.Schema.Event;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot.Processors
{
    public class EventAgendaProcessor : IIntentProcessor
    {
        public string IntentName => AgentConstantNames.AgendaIntentName;
        public bool IsFallbackProcessor => false;
        private IDocumentRepository documentRepository;

        public EventAgendaProcessor(IDocumentRepository documentRepository)
        {
            this.documentRepository = documentRepository ?? throw new ArgumentNullException(nameof(documentRepository));
        }

        public async Task ProcessIntent(IntentContext intentContext)
        {
            var eventDefinitionKey = BuildEventDefinitionKey(intentContext);
            var eventDefinition = await documentRepository.ReadAsync<EventDefinition>(eventDefinitionKey).ConfigureAwait(false);
            var messages = eventDefinition.Details.Agenda.Select(x => $"**{x.From:hh\\:mm}** {x.Subject}").ToArray();

            intentContext.IntentState = AgentConstantNames.AgendaIntentStates.ShowAgenda.ToString("G");
        }
        
        private static DocumentKey BuildEventDefinitionKey(IntentContext intentContext)
        {
            var eventDefinitionKey = new DocumentKey();
            var eventId = intentContext.Entities[IntentRequestEntityNames.EventId].ToString();
            if (!string.IsNullOrEmpty(eventId))
            {
                eventDefinitionKey.Id = eventId;
            }
            return eventDefinitionKey;
        }

//        private static T GetQnAMetadataValue<T>(IBotContext context, string qnaMetadataName)
//        {
//            var recognizedIntents = context.Get<IRecognizedIntents>();
//            var topIntent = recognizedIntents.TopIntent;
//            var metadataEntity = topIntent.Entities.FirstOrDefault(e => e.GroupName == QnAEntityGroupNames.Metadata);
//            if (metadataEntity != null && metadataEntity.ContainsKey(qnaMetadataName))
//            {
//                return (T)metadataEntity[qnaMetadataName];
//            }
//            return default(T);
//        }

      
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
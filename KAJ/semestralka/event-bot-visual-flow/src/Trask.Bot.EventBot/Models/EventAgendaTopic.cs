using System;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;
//using Trask.Bot.Event.Schema;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot.Models
{
//    public class EventAgendaTopic : ITopic
//    {
//        private IDocumentRepository documentRepository;
//
//        public void SetDocumentRepository(IDocumentRepository documentRepository)
//        {
//            this.documentRepository = documentRepository ?? throw new ArgumentNullException(nameof(documentRepository));
//        }
//
//        public string Name { get; set; } = TopicNames.EventAgendaTopic;
//
//        public async Task<bool> ContinueTopic(IBotContext context)
//        {
//            var eventDefinitionKey = BuildEventDefinitionKey(context);
//            var eventDefinition = await documentRepository.ReadAsync<EventDefinition>(eventDefinitionKey).ConfigureAwait(false);
//            var messages = eventDefinition.Details.Agenda.Select(x => $"**{x.Time:hh\\:mm}** {x.Subject}").ToArray();
//            await context.SendActivity(messages).ConfigureAwait(false);
//            return await DefaultTopic.SetAndStartDefaultTopic(context).ConfigureAwait(false);
//        }
//
//        public Task<bool> ResumeTopic(IBotContext context)
//        {
//            throw new NotImplementedException();
//        }
//
//        public async Task<bool> StartTopic(IBotContext context)
//        {
//            return await ContinueTopic(context).ConfigureAwait(false);
//        }
//
//        private static DocumentKey BuildEventDefinitionKey(IBotContext context)
//        {
//            var eventDefinitionKey = new DocumentKey();
//            var eventId = GetQnAMetadataValue<string>(context, QnAMetadataNames.EventId)?.Trim();
//            if (!string.IsNullOrEmpty(eventId))
//            {
//                eventDefinitionKey.Id = eventId;
//            }
//            return eventDefinitionKey;
//        }
//
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
//    }
}
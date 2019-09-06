using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using AdaptiveCards;
using Microsoft.Bot.Builder;
using Microsoft.Bot.Schema;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
//using Trask.Bot.Event.Builders;
//using Trask.Bot.Event.Schema;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot.Models
{
//    public class EventRegistrationTopic : ITopic
//    {
//        private IDocumentRepository documentRepository;
//
//        public void SetDocumentRepository(IDocumentRepository documentRepository)
//        {
//            this.documentRepository = documentRepository ?? throw new ArgumentNullException(nameof(documentRepository));
//        }
//
//        public string Name { get; set; } = TopicNames.EventRegistrationTopic;
//        public DocumentKey EventRegistrationKey { get; set; }
//        public DocumentKey EventDefinitionKey { get; set; }
//
//        public async Task<bool> ContinueTopic(IBotContext context)
//        {
//            if (!IsSupportedChannel(context))
//            {
//                var fallbackAnswer = GetFallbackAnswer(context);
//                await context.SendActivity(fallbackAnswer).ConfigureAwait(false);
//                return await DefaultTopic.SetAndStartDefaultTopic(context).ConfigureAwait(false);
//            }
//            if (EventDefinitionKey != null && EventRegistrationKey != null && context.Request.Value == null && string.IsNullOrEmpty(context.Request.Text) == false)
//            {
//                return await DefaultTopic.SetAndContinueDefaultTopic(context).ConfigureAwait(false);
//            }
//
//            if (EventRegistrationKey == null || context.Request.Value == null)
//            {
//                var eventDefinition = await documentRepository.ReadAsync<EventDefinition>(EventDefinitionKey).ConfigureAwait(false);
//                var registrationFormCard = EventRegistrationFormBuilder.BuildAdaptiveCard(eventDefinition, default(JObject));
//                await SendRegistrationForm(context, registrationFormCard).ConfigureAwait(false);
//                EventRegistrationKey = new DocumentKey();
//                return true;
//            }
//            var eventRegistration = await ProcessEventRegistration(context.Request).ConfigureAwait(false);
//            if (eventRegistration != default(EventRegistration))
//            {
//                if (!IsValidEmail(context.Request.Value as JObject))
//                {
//                    var eventDefinition = await documentRepository.ReadAsync<EventDefinition>(EventDefinitionKey).ConfigureAwait(false);
//                    var registrationFormCard = EventRegistrationFormBuilder.BuildAdaptiveCard(eventDefinition, context.Request.Value as JObject);
//                    await context.SendActivity("Poprosím o vyplnění validní emailová adresy").ConfigureAwait(false);
//                    await SendRegistrationForm(context, registrationFormCard).ConfigureAwait(false);
//                    EventRegistrationKey = new DocumentKey();
//                    return true;
//                }
//                var storedEventRegistration = await documentRepository.WriteAsync(eventRegistration).ConfigureAwait(false);
//                EventRegistrationKey = storedEventRegistration.Key;
//
//                var userData = context.GetUserState<UserData>();
//                if (userData.EventRegistrationEmails.ContainsKey(context.Request.ChannelId) &&
//                    !context.GetUserState<UserData>().EventRegistrationEmails[context.Request.ChannelId].Contains(storedEventRegistration.Email))
//                {
//                    context.GetUserState<UserData>().EventRegistrationEmails[context.Request.ChannelId].Add(storedEventRegistration.Email);
//                }
//                else
//                {
//                    context.GetUserState<UserData>().EventRegistrationEmails.Add(context.Request.ChannelId, new List<string>() { storedEventRegistration.Email });
//                }
//                await context.SendActivity("Děkuji za registraci").ConfigureAwait(false);
//                return await DefaultTopic.SetAndStartDefaultTopic(context).ConfigureAwait(false);
//            }
//            else
//            {
//                var eventDefinition = await documentRepository.ReadAsync<EventDefinition>(EventDefinitionKey).ConfigureAwait(false);
//                var registrationFormCard = EventRegistrationFormBuilder.BuildAdaptiveCard(eventDefinition, context.Request.Value as JObject);
//                await context.SendActivity("Poprosím o doplnění povinných hodnot").ConfigureAwait(false);
//                await SendRegistrationForm(context, registrationFormCard).ConfigureAwait(false);
//                EventRegistrationKey = new DocumentKey();
//            }
//
//            return true;
//        }
//
//        private static bool IsSupportedChannel(IBotContext context) => context.Request.ChannelId == "directline" || context.Request.ChannelId == "webchat" || context.Request.ChannelId == "emulator";
//
//        private async Task SendRegistrationForm(IBotContext context, AdaptiveCard registrationFormCard)
//        {
//            var activity = context.Request.CreateReply();
//            var attachment = new Attachment()
//            {
//                ContentType = AdaptiveCard.ContentType,
//                Content = registrationFormCard
//            };
//            activity.Attachments.Add(attachment);
//
//            await context.SendActivity(activity).ConfigureAwait(false);
//        }
//
//        private async Task<EventRegistration> ProcessEventRegistration(Activity eventRegistrationActivity)
//        {
//            var incommingActivityValue = eventRegistrationActivity.Value as JObject;
//            if ((string)incommingActivityValue.SelectToken("type") == "registration-submit")
//            {
//                var eventDefinitionId = (string)incommingActivityValue.SelectToken("eventDefinitionId");
//                var eventDefinitionKey = new DocumentKey { Id = eventDefinitionId };
//                var eventDefinition = await documentRepository.ReadAsync<EventDefinition>(eventDefinitionKey).ConfigureAwait(false);
//
//                if (!ValidateRequiredFields(incommingActivityValue, eventDefinition.RegistrationForm)) return default(EventRegistration);
//
//                var eventRegistrationFormData = eventDefinition.RegistrationForm
//                    .Select(f => incommingActivityValue.SelectToken(f.Id))
//                    .Where(t => t != null)
//                    .ToDictionary(t => t.Path, t => t.ToObject<object>());
//                var eventRegistrationChannel = new
//                {
//                    serviceUrl = eventRegistrationActivity.ServiceUrl,
//                    channelId = eventRegistrationActivity.ChannelId,
//                    fromId = eventRegistrationActivity.From.Id,
//                    conversationId = eventRegistrationActivity.Conversation.Id
//                };
//                eventRegistrationFormData.Add("channel", eventRegistrationChannel);
//                var eventRegistration = new EventRegistration
//                {
//                    Email = (string)incommingActivityValue.SelectToken("email"),
//                    EventDefinitionId = eventDefinitionId,
//                    SchemaVersion = "0.1",
//                    FormData = eventRegistrationFormData
//                };
//
//                return eventRegistration;
//            }
//            return default(EventRegistration);
//        }
//
//        private bool ValidateRequiredFields(JObject eventRegistrationFormData, IList<EventDefinitionRegistrationFormField> registrationForm)
//        {
//            return registrationForm
//                .Where(f => f.Required)
//                .Select(f => eventRegistrationFormData.SelectToken(f.Id))
//                .All(t => t != null && !string.IsNullOrEmpty((string)t));
//        }
//
//        private bool IsValidEmail(JObject eventRegistrationFormData)
//        {
//            var email = eventRegistrationFormData.SelectToken("email");
//            if (email != null && string.IsNullOrEmpty(email.ToObject<string>()) == false)
//            {
//                var regex = new Regex(@"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$", RegexOptions.Compiled);
//                return regex.IsMatch(email.ToObject<string>());
//            }
//            return false;
//        }
//
//        public async Task<bool> ResumeTopic(IBotContext context)
//        {
//            if (EventRegistrationKey != null && !string.IsNullOrEmpty(EventRegistrationKey.Id))
//            {
//                return await DefaultTopic.SetAndStartDefaultTopic(context).ConfigureAwait(false);
//            }
//            return await ContinueTopic(context).ConfigureAwait(false);
//        }
//
//        public async Task<bool> StartTopic(IBotContext context)
//        {
//            if (!IsSupportedChannel(context))
//            {
//                var fallbackAnswer = GetFallbackAnswer(context);
//                await context.SendActivity(fallbackAnswer).ConfigureAwait(false);
//                return await DefaultTopic.SetAndStartDefaultTopic(context).ConfigureAwait(false);
//            }
//            EventDefinitionKey = BuildEventDefinitionKey(context);
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
//
//        private static string GetFallbackAnswer(IBotContext context)
//        {
//            var recognizedIntents = context.Get<IRecognizedIntents>();
//            var topIntent = recognizedIntents.TopIntent;
//            var answerEntity = topIntent.Entities.FirstOrDefault(e => e.GroupName == QnAEntityGroupNames.Answer);
//            if (answerEntity != null)
//            {
//                return answerEntity.ValueAs<string>()?.Trim() ?? string.Empty;
//            }
//            return string.Empty;
//        }
//    }
}
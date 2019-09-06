using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Microsoft.Bot.Schema;
using Newtonsoft.Json.Linq;
using Trask.Bot.Event.Builders;
using Trask.Bot.EventBot.Models;
using Trask.Bot.EventBot.Recognition;
using Trask.Bot.Recognition.Intent;
using Trask.Bot.Schema;
using Trask.Bot.Schema.Event;
using Trask.Bot.Event.Extensions.Schema;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot.Processors
{
    public class EventRegistrationProcessor : IIntentProcessor
    {
        private IDocumentRepository documentRepository;
        public string IntentName => AgentConstantNames.RegistrationIntentName;
        public bool IsFallbackProcessor => false;
        private DocumentKey EventRegistrationKey { get; set; }
        private DocumentKey EventDefinitionKey { get; set; }

        public EventRegistrationProcessor(IDocumentRepository documentRepository)
        {
            this.documentRepository = documentRepository ?? throw new ArgumentNullException(nameof(documentRepository));
        }

        public async Task ProcessIntent(IntentContext intentContext)
        {
            string channelId = intentContext.Entities[IntentRequestEntityNames.ChannelId].ToString();
            EventDefinition eventDefinition = await documentRepository.ReadAsync<EventDefinition>(EventDefinitionKey).ConfigureAwait(false);

            if (!IsSupportedChannel(channelId))
            {
                //TODO: dodelat response
                intentContext.IntentState = AgentConstantNames.RegistrationIntentStates.ChannelNotSupported.ToString("G");
                return;
            }

            //Co je presne EventRegistrationKey? 
            if (EventDefinitionKey != null && EventRegistrationKey != null /*&& intentContext.Request.Value == null*/ && string.IsNullOrEmpty(intentContext.Entities[IntentRequestEntityNames.RequestText].ToString()) == false)
            {
                //return await DefaultTopic.SetAndContinueDefaultTopic(intentContext).ConfigureAwait(false);
                //TODO nastavit default (Guide?) topic a state
            }

            //posle registracni formular, protoze EventRegistrationKey == null, tudiz user neni na event zaregistrovany
            if (EventRegistrationKey == null /*|| intentContext.Request.Value == null*/)
            {

                //TODO vybuildit ResponseDefinitionParameters pro registracni formular? Nebo je registracni form statickej?
                var registrationFormCard = EventDefinitionBuilder.BuildSection<EventDefinitionSectionRegistration>(eventDefinition, default(JObject));
                // await SendRegistrationForm(intentContext, registrationFormCard).ConfigureAwait(false);

                EventRegistrationKey = new DocumentKey();
                intentContext.IntentState = AgentConstantNames.RegistrationIntentStates.ShowRegistrationForm.ToString("G");
                return;
            }
            var eventRegistration = await ProcessEventRegistration((Activity)intentContext.Entities[IntentRequestEntityNames.RegistrationActivity], eventDefinition).ConfigureAwait(false);
            if (eventRegistration != default(EventRegistration))
            {
                if (!IsValidEmail(intentContext.Entities[IntentRequestEntityNames.EmailAddress].ToString()))
                {

                    //TODO vybuildit ResponseDefinitionParameters pro registracni formular? Nebo je registracni form statickej?
                    var registrationFormCard = EventDefinitionBuilder.BuildSection<EventDefinitionSectionRegistration>(eventDefinition, default(JObject)); //intentContext.Request.Value as JObject
                                                                                                                                                           //                  await intentContext.SendActivity("Poprosím o vyplnění validní emailová adresy").ConfigureAwait(false);
                                                                                                                                                           //                  await SendRegistrationForm(intentContext, registrationFormCard).ConfigureAwait(false);

                    EventRegistrationKey = new DocumentKey();
                    intentContext.IntentState = AgentConstantNames.RegistrationIntentStates.InvalidEmailAddressRegistrationForm.ToString("G");
                    return;
                }

                //TODO ulozeni pres provider
                //var storedEventRegistration = await documentRepository.WriteAsync(eventRegistration).ConfigureAwait(false);
                //EventRegistrationKey = storedEventRegistration.Key;
                EventRegistration storedEventRegistration = null;
                EventStateUserData eventStateUserData = (EventStateUserData)intentContext.Entities[IntentRequestEntityNames.UserData];

                if (eventStateUserData.EventRegistrationEmails.ContainsKey(channelId) &&
                    !eventStateUserData.EventRegistrationEmails[channelId].Contains(storedEventRegistration.UserId))
                {
                    eventStateUserData.EventRegistrationEmails[channelId].Add(storedEventRegistration.UserId);
                }

                else
                {
                    eventStateUserData.EventRegistrationEmails.Add(channelId, new List<string>() { storedEventRegistration.UserId });
                }
                //TODO do responsu await intentContext.SendActivity("Děkuji za registraci").ConfigureAwait(false);
                //return await DefaultTopic.SetAndStartDefaultTopic(intentContext).ConfigureAwait(false);
                intentContext.IntentState = AgentConstantNames.RegistrationIntentStates.SuccessfullyRegistered.ToString("G");
            }
            else
            {

                //TODO vybuildit ResponseDefinitionParameters pro registracni formular? Nebo je registracni form statickej?
                //EventDefinitionBuilder.BuildSection buildi ted nove response template parameters
                var registrationFormCard = EventDefinitionBuilder.BuildSection<EventDefinitionSectionRegistration>(eventDefinition, default(JObject)); //intentContext.Request.Value as JObject
                                                                                                                                                       //                await intentContext.SendActivity("Poprosím o doplnění povinných hodnot").ConfigureAwait(false);
                                                                                                                                                       //                await SendRegistrationForm(intentContext, registrationFormCard).ConfigureAwait(false);

                EventRegistrationKey = new DocumentKey();
                intentContext.IntentState = AgentConstantNames.RegistrationIntentStates.FillRequiredFieldsRegistrationForm.ToString("G");
            }
        }

        private static bool IsSupportedChannel(string channelId) => channelId == "directline" || channelId == "webchat" || channelId == "emulator";

        //TODO bude se odesilat template v obecnem response middlewaru (nebo co to tam ted mame)
        //        private async Task SendRegistrationForm(IntentContext intentContext, AdaptiveCard registrationFormCard)
        //        {
        //            var activity = intentContext.Request.CreateReply();
        //            var attachment = new Attachment()
        //            {
        //                ContentType = AdaptiveCard.ContentType,
        //                Content = registrationFormCard
        //            };
        //            activity.Attachments.Add(attachment);
        //
        //            await intentContext.SendActivity(activity).ConfigureAwait(false);
        //        }

        private Task<EventRegistration> ProcessEventRegistration(Activity eventRegistrationActivity, EventDefinition eventDefinition)
        {
            var incomingActivityValue = eventRegistrationActivity.Value as JObject;
            if ((string)incomingActivityValue.SelectToken("type") == "registration-submit")
            {
                var eventDefinitionId = (string)incomingActivityValue.SelectToken("eventDefinitionId");
                var eventDefinitionKey = new DocumentKey { Id = eventDefinitionId };

                var formObjectFields = eventDefinition.GetDefinitionSection<EventDefinitionSectionRegistration>((ed) => null).FormObjectFields;
                if (!ValidateRequiredFields(incomingActivityValue, formObjectFields))
                    return Task.FromResult(default(EventRegistration));

                var eventRegistrationFormObjectFields = formObjectFields
                    .Select(f => incomingActivityValue.SelectToken(f.Id))
                    .Where(t => t != null)
                    .ToDictionary(t => t.Path, t => t.ToObject<object>());
                var eventRegistrationChannel = new
                {
                    serviceUrl = eventRegistrationActivity.ServiceUrl,
                    channelId = eventRegistrationActivity.ChannelId,
                    fromId = eventRegistrationActivity.From.Id,
                    conversationId = eventRegistrationActivity.Conversation.Id
                };
                eventRegistrationFormObjectFields.Add("channel", eventRegistrationChannel);
                var eventRegistration = new EventRegistration
                {
                    UserId = (string)incomingActivityValue.SelectToken("email"),
                    EventDefinitionId = eventDefinitionId,
                    AdditionalProperties = eventRegistrationFormObjectFields
                };

                return Task.FromResult(eventRegistration);
            }
            return Task.FromResult(default(EventRegistration));
        }

        private bool ValidateRequiredFields(JObject eventRegistrationFormData, IEnumerable<FormObjectInputField> registrationForm)
        {
            return registrationForm
                .Where(f => f.Required)
                .Select(f => eventRegistrationFormData.SelectToken(f.Id))
                .All(t => t != null && !string.IsNullOrEmpty((string)t));
        }

        private bool IsValidEmail(string emailAddress)
        {
            if (emailAddress != null && string.IsNullOrEmpty(emailAddress) == false)
            {
                var regex = new Regex(@"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$", RegexOptions.Compiled);
                return regex.IsMatch(emailAddress);
            }
            return false;
        }

        //        public async Task<bool> ResumeTopic(IntentContext intentContext)
        //        {
        //            if (EventRegistrationKey != null && !string.IsNullOrEmpty(EventRegistrationKey.Id))
        //            {
        //                return await DefaultTopic.SetAndStartDefaultTopic(intentContext).ConfigureAwait(false);
        //            }
        //            return await ContinueTopic(intentContext).ConfigureAwait(false);
        //        }
        //
        //        public async Task<bool> StartTopic(IntentContext intentContext)
        //        {
        //            if (!IsSupportedChannel(intentContext))
        //            {
        //                var fallbackAnswer = GetFallbackAnswer(intentContext);
        //                await intentContext.SendActivity(fallbackAnswer).ConfigureAwait(false);
        //                return await DefaultTopic.SetAndStartDefaultTopic(intentContext).ConfigureAwait(false);
        //            }
        //            EventDefinitionKey = BuildEventDefinitionKey(intentContext);
        //            return await ContinueTopic(intentContext).ConfigureAwait(false);
        //        }

        //        private static DocumentKey BuildEventDefinitionKey(IntentContext intentContext)
        //        {
        //            var eventDefinitionKey = new DocumentKey();
        //            var eventId = GetQnAMetadataValue<string>(intentContext, QnAMetadataNames.EventId)?.Trim();
        //            if (!string.IsNullOrEmpty(eventId))
        //            {
        //                eventDefinitionKey.Id = eventId;
        //            }
        //            return eventDefinitionKey;
        //        }

        //        private static T GetQnAMetadataValue<T>(IntentContext intentContext, string qnaMetadataName)
        //        {
        //            var recognizedIntents = intentContext.Get<IRecognizedIntents>();
        //            var topIntent = recognizedIntents.TopIntent;
        //            var metadataEntity = topIntent.Entities.FirstOrDefault(e => e.GroupName == QnAEntityGroupNames.Metadata);
        //            if (metadataEntity != null && metadataEntity.ContainsKey(qnaMetadataName))
        //            {
        //                return (T)metadataEntity[qnaMetadataName];
        //            }
        //            return default(T);
        //        }

        //        private static string GetFallbackAnswer(IntentContext intentContext)
        //        {
        //            var recognizedIntents = intentContext.Get<IRecognizedIntents>();
        //            var topIntent = recognizedIntents.TopIntent;
        //            var answerEntity = topIntent.Entities.FirstOrDefault(e => e.GroupName == QnAEntityGroupNames.Answer);
        //            if (answerEntity != null)
        //            {
        //                return answerEntity.ValueAs<string>()?.Trim() ?? string.Empty;
        //            }
        //            return string.Empty;
        //        }

        public bool IsFinalState(object state)
        {
            return true;
        }

        public bool IsSwitchToDefaultIntentState(object state)
        {
            return false;
        }

    }
}
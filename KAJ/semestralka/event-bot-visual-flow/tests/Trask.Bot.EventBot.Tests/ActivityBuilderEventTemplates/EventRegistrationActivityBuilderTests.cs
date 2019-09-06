using System.Collections.Generic;
using AdaptiveCards;
using FluentAssertions;
using Microsoft.Bot.Schema;
using Trask.Bot.Event.Builders;
using Trask.Bot.Schema;
using Trask.Bot.Schema.Event;
using Xunit;

namespace Trask.Bot.EventBot.ActivityBuilderEventTemplates.Tests
{
    public class EventRegistrationActivityBuilderTests
    {
        [Fact]
        public void ActivityBuilderShouldBuildEventRegistrationActivityCorrectly()
        {
            Activity activity = BuildDefaultResponseActivity();
            var responseTemplateParameters = new ResponseTemplateParameters
            {
                Body = new List<ResponseTemplateParameter>
                    {
                        new ResponseTemplateParameter
                        {
                            Container = new Dictionary<string, string>
                            {
                                { EventResponseTemplateParametersNames.Subject, "Event subject" },
                                { EventResponseTemplateParametersNames.Title, "Section registration" },
                                { EventResponseTemplateParametersNames.FormFieldActions, "{\"type\":\"Action.Submit\",\"data\":{\"type\":\"RegistrationSubmit\",\"eventDefinitionId\":\"D1\",\"eventDefinitionSectionId\":\"S1\"},\"title\":\"A1\"}" }
                            },
                            Items = new List<IDictionary<string, string>>
                            {
                                new Dictionary<string, string>
                                {
                                    { EventResponseTemplateParametersNames.FormFieldLabel, "F1" },
                                    { EventResponseTemplateParametersNames.FormFieldColor, "default" },
                                    { EventResponseTemplateParametersNames.FormFieldInput, "{\"type\":\"Input.Text\",\"id\":\"F1\",\"value\":\"F1Value\"}" }
                                },
                                new Dictionary<string, string>
                                {
                                    { EventResponseTemplateParametersNames.FormFieldLabel, "F2" },
                                    { EventResponseTemplateParametersNames.FormFieldColor, "attention" },
                                    { EventResponseTemplateParametersNames.FormFieldInput, "{\"type\":\"Input.Text\",\"id\":\"F2\",\"value\":\"F2Value\"}" }
                                },
                                new Dictionary<string, string>
                                {
                                    { EventResponseTemplateParametersNames.FormFieldLabel, "F3" },
                                    { EventResponseTemplateParametersNames.FormFieldColor, "default" },
                                    { EventResponseTemplateParametersNames.FormFieldInput, "{\"type\":\"Input.Text\",\"id\":\"F3\",\"value\":\"default value\"}" }
                                },
                            }
                        }
                    }
            };
            var responseDefinitions = new[] {
                new ResponseDefinition("response-definition:ef3ee822-a531-4c4a-9c7b-ae620c87adac")
                {
                    Topic = "EventAgent",
                    Intent = "event.registration",
                    State = "PromptToFillRegistration",
                    Channel = null,
                    Locale = null,
                    Domain = null,
                    PayloadType = "adaptive-card",
                    Payload = "{\"type\":\"AdaptiveCard\",\"body\":[{\"type\":\"Container\",\"items\":[{\"type\":\"TextBlock\",\"size\":\"medium\",\"weight\":\"bolder\",\"text\":\"{Subject}\"},{\"type\":\"TextBlock\",\"text\":\"{Title}\"},{\"type\":\"TextBlock\",\"text\":\"Don't worry, we'll never share or sell your information.\",\"isSubtle\":true,\"wrap\":true,\"size\":\"small\"}]}],\"actions\":[{FormField.Actions}],\"version\":\"1.0\"}",
                    ValidFrom = null,
                    ValidTo = null
                },
                new ResponseDefinition("response-definition:6fed7bd2-2018-40f7-8ff2-0a4f7d9334f3")
                {
                    Topic = "EventAgent",
                    Intent = "event.registration",
                    State = "PromptToFillRegistration",
                    Channel = null,
                    Locale = null,
                    Domain = null,
                    PayloadType = "adaptive-card-container",
                    Payload = "{\"type\":\"Container\",\"items\":[{\"type\":\"TextBlock\",\"text\":\"{FormField.Label}\",\"color\":\"{FormField.Color}\",\"wrap\":true},{FormField.Input}]}",
                    ValidFrom = null,
                    ValidTo = null
                }
            };
            var expectedActivities = new[]
            {
                new Activity
                {
                    Type = ActivityTypes.Message,
                    ReplyToId = activity.Id,
                    Conversation = activity.Conversation,
                    ChannelId = activity.ChannelId,
                    Recipient = activity.From,
                    From = activity.Recipient,
                    AttachmentLayout = "list",
                    Attachments = new List<Attachment>
                    {
                        new Attachment
                        {
                            ContentType = "application/vnd.microsoft.card.adaptive",
                            Content = new AdaptiveCard(new AdaptiveSchemaVersion("1.0"))
                            {
                                Actions = new List<AdaptiveAction>
                                {
                                    new AdaptiveSubmitAction
                                    {
                                        Title = "A1",
                                        Data = new
                                        {
                                            type = "RegistrationSubmit",
                                            eventDefinitionId = "D1",
                                            eventSectionDefinitionId = "S1"
                                        }
                                    }
                                },
                                Body = new List<AdaptiveElement>
                                {
                                    new AdaptiveContainer
                                    {
                                        Items = new List<AdaptiveElement>
                                        {
                                            new AdaptiveTextBlock
                                            {
                                                Size = AdaptiveTextSize.Medium,
                                                Weight = AdaptiveTextWeight.Bolder,
                                                Text = "Event subject"
                                            },
                                            new AdaptiveTextBlock
                                            {
                                                Text = "Section registration"
                                            },
                                            new AdaptiveTextBlock
                                            {
                                                Text = "Don't worry, we'll never share or sell your information.",
                                                IsSubtle = true,
                                                Wrap = true,
                                                Size = AdaptiveTextSize.Small
                                            }
                                        }
                                    },
                                    new AdaptiveContainer
                                    {
                                        Items = new List<AdaptiveElement>{
                                            new AdaptiveTextBlock
                                            {
                                                Text = "F1",
                                                Color = AdaptiveTextColor.Default,
                                                Wrap = true
                                            },
                                            new AdaptiveTextInput
                                            {
                                                Id = "F1",
                                                Value = "F1Value"
                                            }
                                        }
                                    },
                                    new AdaptiveContainer
                                    {
                                        Items = new List<AdaptiveElement>{
                                            new AdaptiveTextBlock
                                            {
                                                Text = "F2",
                                                Color = AdaptiveTextColor.Attention,
                                                Wrap = true
                                            },
                                            new AdaptiveTextInput
                                            {
                                                Id = "F2",
                                                Value = "F2Value"
                                            }
                                        }
                                    },
                                    new AdaptiveContainer
                                    {
                                        Items = new List<AdaptiveElement>{
                                            new AdaptiveTextBlock
                                            {
                                                Text = "F3",
                                                Color = AdaptiveTextColor.Default,
                                                Wrap = true
                                            },
                                            new AdaptiveTextInput
                                            {
                                                Id = "F3",
                                                Value = "default value"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    Entities = new List<Entity>(),
                    Text = string.Empty
                }
            };
            var activities = Trask.Bot.Services.Microsoft.Extensions.ActivityBuilder.BuildActivities(activity, responseTemplateParameters, responseDefinitions);
            activities.Should()
                      .BeEquivalentTo(expectedActivities, a => a.Excluding(x => x.SelectedMemberPath.EndsWith("Timestamp")));
        }

        [Fact]
        public void ActivityBuilderShouldBuildEventInvitationCorrectly()
        {
            Activity activity = BuildDefaultResponseActivity();
            var responseTemplateParameters = new ResponseTemplateParameters
            {
                Body = new List<ResponseTemplateParameter>
                    {
                        new ResponseTemplateParameter
                        {
                            Container = new Dictionary<string, string>
                            {
                                { EventResponseTemplateParametersNames.Subject, "Event subject" },
                                { EventResponseTemplateParametersNames.Group, "Event group" },
                                { EventResponseTemplateParametersNames.Description, "Event description" },
                                { EventResponseTemplateParametersNames.ImageUrl, "https://event.image.url.com" },
                                { EventResponseTemplateParametersNames.EventId, "D1" }
                            },
                            Items = new List<IDictionary<string, string>>
                            {
                                new Dictionary<string, string>
                                {
                                    {"", ""}
                                }
                            }
                        }
                    }
            };
            var eventDefinition = new EventDefinition
            {
                Id = "D1",
                Subject = "Event subject",
                Group = "Event group",
                Details = new EventDefinitionDetails
                {
                    Description = "Event description",
                    ImageUrl = "https://event.image.url.com"
                }
            };
            var responseDefinitions = new[]
            {
                new ResponseDefinition("response-definition:1aad6be4-7168-47c3-9d41-f491dab832e8")
                {
                    Topic = "EventAgent",
                    Intent = "event.invitation",
                    State = "PromptToShowRegistration",
                    Channel = null,
                    Locale = null,
                    Domain = null,
                    PayloadType = "adaptive-card",
                    Payload = "{\"type\":\"AdaptiveCard\",\"backgroundImage\":\"{ImageUrl}\",\"body\":[{\"type\":\"Container\",\"items\":[{\"type\":\"TextBlock\",\"size\":\"medium\",\"weight\":\"bolder\",\"text\":\"{Group}\"},{\"type\":\"TextBlock\",\"weight\":\"bolder\",\"wrap\":true,\"isSubtle\":true,\"text\":\"{Subject}\"},{\"type\":\"TextBlock\",\"text\":\"{Description}\",\"isSubtle\":true,\"wrap\":true,\"size\":\"small\"}]}],\"actions\":[{\"type\":\"Action.Submit\",\"title\":\"Registrovat\",\"data\":{\"type\":\"Registration.Show\",\"eventDefinitionId\":\"{EventId}\"}}],\"version\":\"1.0\"}",
                    ValidFrom = null,
                    ValidTo = null
                },
                new ResponseDefinition("response-definition:fa2393ba-8129-484b-817b-6f5cb9edd040")
                {
                    Topic = "EventAgent",
                    Intent = "event.invitation",
                    State = "PromptToShowRegistration",
                    Channel = null,
                    Locale = null,
                    Domain = null,
                    PayloadType = "adaptive-card-container",
                    Payload = "{\"type\":\"Container\",\"items\":[{\"type\":\"TextBlock\",\"weight\":\"bolder\",\"wrap\":true,\"text\":\"Tato pozvánka vyžaduje potvrzení Vaší registrace\"}]}",
                    ValidFrom = null,
                    ValidTo = null,
                }
            };
            var expectedActivities = new[]
            {
                new Activity
                {
                    Type = ActivityTypes.Message,
                    ReplyToId = activity.Id,
                    Conversation = activity.Conversation,
                    ChannelId = activity.ChannelId,
                    Recipient = activity.From,
                    From = activity.Recipient,
                    AttachmentLayout = "list",
                    Attachments = new List<Attachment>
                    {
                        new Attachment
                        {
                            ContentType = "application/vnd.microsoft.card.adaptive",
                            Content = new AdaptiveCard(new AdaptiveSchemaVersion("1.0"))
                            {
                                BackgroundImage = new System.Uri("https://event.image.url.com"),
                                Actions = new List<AdaptiveAction>
                                {
                                    new AdaptiveSubmitAction
                                    {
                                        Title = "Registrovat",
                                        Data = new
                                        {
                                            type = "Registration.Show",
                                            eventDefinitionId = "D1"
                                        }
                                    }
                                },
                                Body = new List<AdaptiveElement>
                                {
                                    new AdaptiveContainer
                                    {
                                        Items = new List<AdaptiveElement>
                                        {
                                            new AdaptiveTextBlock
                                            {
                                                Size = AdaptiveTextSize.Medium,
                                                Weight = AdaptiveTextWeight.Bolder,
                                                Text = "Event group"
                                            },
                                            new AdaptiveTextBlock
                                            {
                                                Weight = AdaptiveTextWeight.Bolder,
                                                Wrap = true,
                                                IsSubtle = true,
                                                Text = "Event subject"
                                            },
                                            new AdaptiveTextBlock
                                            {
                                                Text = "Event description",
                                                IsSubtle = true,
                                                Wrap = true,
                                                Size = AdaptiveTextSize.Small
                                            }
                                        }
                                    },
                                    new AdaptiveContainer
                                    {
                                        Items = new List<AdaptiveElement>
                                        {
                                            new AdaptiveTextBlock
                                            {
                                                Weight = AdaptiveTextWeight.Bolder,
                                                Wrap = true,
                                                Text = "Tato pozvánka vyžaduje potvrzení Vaší registrace"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    Entities = new List<Entity>(),
                    Text = string.Empty
                }
            };

            var activities = Trask.Bot.Services.Microsoft.Extensions.ActivityBuilder.BuildActivities(activity, responseTemplateParameters, responseDefinitions);
            activities.Should()
                      .BeEquivalentTo(expectedActivities, a => a.Excluding(x => x.SelectedMemberPath.EndsWith("Timestamp")));
        }

        private static Activity BuildDefaultResponseActivity()
        {
            Activity activity = Activity.CreateMessageActivity() as Activity;
            activity.Conversation = new ConversationAccount { Id = "xxx", Name = "xxx" };
            activity.Id = "xxxxxxxx-1-xxxxx";
            activity.ChannelId = "webchat";
            activity.From = new ChannelAccount { Id = "yyy", Name = "yyy" };
            activity.Recipient = new ChannelAccount { Id = "zzz", Name = "zzz" };
            return activity;
        }
    }
}
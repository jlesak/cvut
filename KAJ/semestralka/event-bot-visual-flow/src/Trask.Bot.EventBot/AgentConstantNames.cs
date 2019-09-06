using System.ComponentModel;
using Trask.Bot.State;

namespace Trask.Bot.EventBot
{
    public class AgentConstantNames
    {
        public const string TopicName = "EventAgent";
        public const string WelcomeIntentName = "welcome";
        public const string EventIntentName = "event";
        public const string AgendaIntentName = "agenda";
        public const string RegistrationIntentName = "registration";
        public const string SurveyIntentName = "survey";
        public const string FollowUpIntentName = "followUp";
        public const string UnrecognizedIntentName = "UnrecognizedIntent";
        public const string GeneralIntentName = "general";
        public const string FeedbackIntentName = "feedback";
        public const string MinorIntentName = "MinorIntent";

       
        public enum WelcomeIntentStates
        {
            [Description("Default state")]
            Unknown,

            [Description("Welcome new or existing user")]
            WelcomeUser,

            [Description("User want to display help")]
            [FinalState]
            Help,

            [Description("Unhandled exception occurred")]
            [FinalState]
            [SwitchToDefaultIntent]
            UnhandledError
        }
    
        public enum UnrecognizedIntentStates
        {
            IntentNotRecognized
        }

        public enum AgendaIntentStates
        {
            ShowAgenda
        }

        public enum RegistrationIntentStates
        {
            ChannelNotSupported,
            ShowRegistrationForm,
            InvalidEmailAddressRegistrationForm,
            FillRequiredFieldsRegistrationForm,
            SuccessfullyRegistered,
        }

        public enum FeedbackIntentState
        {
            [Description("Default state")]
            Default,

            [Description("User choice 👍")]
            [SwitchToDefaultIntent]
            ThumbsUp,

            [Description("User write suggestion with 👎")]
            [SwitchToDefaultIntent]
            ThumbsDown
        }

    }
}
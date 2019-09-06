using System.Collections.Generic;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot.Models
{
    /// <summary>
    /// Persisted user data
    /// </summary>
    public class EventStateUserData
    {
        public string UserId { get; set; }
        public IDictionary<string, IList<string>> EventRegistrationEmails { get; set; } = new Dictionary<string, IList<string>>();
    }
}
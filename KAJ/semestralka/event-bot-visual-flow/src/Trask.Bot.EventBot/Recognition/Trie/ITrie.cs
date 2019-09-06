using System.Collections.Generic;

namespace Trask.Bot.EventBot.Recognition.Trie
{
    public interface ITrie<T> : IEnumerable<T> where T : class, IHasName
    {
        void Add(T value);
        void AddRange(IEnumerable<T> value);

        IEnumerable<T> GetValuesByPrefix(string prefix);
        T GetValueByExactKey(string key);
    }
    
    public interface IHasName
    {
        string Name { get; set; }
    }
}
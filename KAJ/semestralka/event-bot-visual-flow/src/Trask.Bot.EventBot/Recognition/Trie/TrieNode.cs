using System.Collections.Generic;

namespace Trask.Bot.EventBot.Recognition.Trie
{
    internal class TrieNode<T> where T : class, IHasName
    {
        public IDictionary<string, TrieNode<T>>  Edges = new Dictionary<string, TrieNode<T>>();
        public TrieNode<T> Parent { get; set; }
        public T Value { get; set; }
    }
}
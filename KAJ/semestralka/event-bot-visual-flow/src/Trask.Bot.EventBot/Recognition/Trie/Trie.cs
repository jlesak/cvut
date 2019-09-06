using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.Bot.Builder;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace Trask.Bot.EventBot.Recognition.Trie
{
    public class Trie<T> : ITrie<T> where T : class, IHasName, new()
    {
        private readonly TrieNode<T> _root = new TrieNode<T>();

        public Trie()
        {
        }
        public Trie(IEnumerable<T> values)
        {
            AddRange(values);
        }

        /// <summary>
        /// Builds trie from intents returned by NLP API
        /// </summary>
        /// <param name="serviceEndpoint">NLP API URL</param>
        /// <param name="modelName"></param>
        public async void BuildTrie(string serviceEndpoint, string modelName)
        {
            var intents = await GetIntents(serviceEndpoint, modelName);
            AddRange(intents);
        }
        
        /// <summary>
        /// Builds trie from intents in .txt file
        /// </summary>
        /// <param name="filePath"></param>
        public void BuildTrie(string filePath)
        {
            switch (Path.GetExtension(filePath))
            {
               case ".txt":
                   var intents = File.ReadAllLines(filePath).Select(i => new T {Name = i});
                   AddRange(intents);
                   break;
               case ".json":
                   string json;
                   using ( StreamReader reader = new StreamReader(filePath))
                   {
                        json = reader.ReadToEnd();
                   }
                   var jArray = (JArray)JsonConvert.DeserializeObject(json);
                   BuildTreeFomJson(jArray);
                   break;
               default:
                   throw new NotImplementedException($"File extension {Path.GetExtension(filePath)} is not supported.");
            }
        }

        private void BuildTreeFomJson(JArray jsonArray)
        {
            foreach (var item in jsonArray.Children<JObject>())
            {
                foreach (JObject jObject in item.Properties().First(p => p.Name == "children").Value.Children<JObject>().ToList())
                {
                    ReadJObject(jObject, "");
                }
            }
        }

        private void ReadJObject(JObject jObject, string intentValue)
        {
            var name = (string)jObject.Properties().FirstOrDefault(p => p.Name == "name")?.Value;
            intentValue += '.' + name;
            var children = jObject.Properties().First(p => p.Name == "children").Value.Children<JObject>().ToList();
            if (children.Count == 0)
            {
                Add(new T{Name = intentValue.Remove(0,1)});
                return;
            }
            foreach (var child in children)
            {
                ReadJObject(child, intentValue);
            }
        }

        private async Task<IEnumerable<T>> GetIntents(string serviceEndpoint, string modelName)
        {
            string getIntentsUrl = $"{serviceEndpoint}svdintents?modelname={modelName}";
            HttpClient httpClient = new HttpClient();
            using (var response = await httpClient.GetAsync(getIntentsUrl).ConfigureAwait(false))
            {
                if (!response.IsSuccessStatusCode)
                {
                    System.Diagnostics.Trace.TraceWarning($"'Request Url': '{getIntentsUrl}', 'StatusCode': '{response.StatusCode}', 'Response': '{await response.Content.ReadAsStringAsync().ConfigureAwait(false)}'");
                    return new List<T>();
                }

                var jsonResponse = await response.Content.ReadAsStringAsync().ConfigureAwait(false);
                var queryResult = (JObject)JsonConvert.DeserializeObject(jsonResponse);
                var intents = queryResult["intents"].ToList();
                return intents.Select(i => new T{Name = i.Value<string>()});
            }
        }
        
        public void Add(T value)
        {
            var node = _root;
            var intents = value.Name.Split('.');
            for (int i = 0; i < intents.Length; i++)
            {
                node.Edges.TryGetValue(intents[i], out var next);
                if (next == null)
                {
                    next = new TrieNode<T>()
                    {
                        Parent = node,
                        Value = new T{Name = intents[i]}
                    };
                    node.Edges.Add(intents[i], next);
                }
                node = next;
            }
            node.Value = value; 

        }
        public void AddRange(IEnumerable<T> values)
        {
            foreach (var value in values)
            {
                Add(value);
            }
        }

        private IEnumerable<T> GetValueRecursively(TrieNode<T> node)
        {
            if (node.Value != null)
            {
                yield return node.Value;
            }
            foreach (var range in node.Edges.Values.SelectMany(GetValueRecursively))
            {
                yield return range;
            }
        }

        private TrieNode<T> NodeAtPrefix(string prefix, TrieNode<T> startNode = null)
        {
            var node = startNode ?? _root;
            var previousNode = node;
            
            foreach (var c in prefix.Split('.'))
            {
                node.Edges.TryGetValue(c, out var next);
                if (next == null)
                {
                    return node.Parent == null || previousNode == node.Parent ? null : NodeAtPrefix(prefix, node.Parent);
                }
                node = next;
            }
            return node;
        }

        public IEnumerable<T> GetValuesByPrefix(string prefix)
        {
            var node = NodeAtPrefix(prefix);
            return node == null ? Enumerable.Empty<T>() : GetValueRecursively(node);
        }

        public IEnumerable<T> GetValuesByPrefix(string prefix, string previousIntent)
        {
            var startNode = NodeAtPrefix(previousIntent);
            
            var node = NodeAtPrefix(prefix, startNode);
            return node == null ? Enumerable.Empty<T>() : GetValueRecursively(node);
        }

        public T GetValueByExactKey(string key)
        {
            var node = NodeAtPrefix(key);
            return node?.Value;
        }
        
        public T GetValueByExactKey(string key, string previousIntent)
        {
            if(previousIntent == null) previousIntent = String.Empty;
            var startNode = NodeAtPrefix(previousIntent);

            var node = NodeAtPrefix(key, startNode);
            return node.Value;

        }

        public IEnumerator<T> GetEnumerator()
        {
            return GetValueRecursively(_root).GetEnumerator();
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }
    }
}
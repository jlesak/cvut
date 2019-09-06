using System;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Newtonsoft.Json.Linq;
using Trask.Bot.Schema;
using Trask.Bot.Services;

namespace Trask.Bot.EventBot
{
    public static class DatabaseSeeder
    {
        public static void SeedData(IServiceProvider serviceProvider, string seedDataDirectoryPath)
        {
            if (serviceProvider == null)
            {
                throw new ArgumentNullException(nameof(serviceProvider));
            }

            var path = Path.Combine(Path.GetDirectoryName(Assembly.GetExecutingAssembly().Location), seedDataDirectoryPath);
            if (!System.IO.Directory.Exists(path)) return;

            var responseDefinitionProvider = serviceProvider.GetRequiredService<IResponseDefinitionProvider>();
            //warm up (table create)
            responseDefinitionProvider.QueryAsync("1", "1", null, null, false).ConfigureAwait(false).GetAwaiter().GetResult();

            System.Diagnostics.Trace.TraceInformation($"Seeding database with documents from '{seedDataDirectoryPath}'...");
            var documentTasks = System.IO.Directory
                    .GetFiles(path, "*.json")
                    .Select(p => JToken.Parse(System.IO.File.ReadAllText(p)))
                    .SelectMany(t =>
                    {
                        if (t.Type == JTokenType.Array)
                        {
                            return ((JArray)t).Cast<JObject>();
                        }
                        if (t.Type == JTokenType.Object)
                        {
                            return new[] { (JObject)t }.AsEnumerable();
                        }
                        return Enumerable.Empty<JObject>();
                    })
                    .Select(o =>
                    {
                        if (o["id"].ToString().StartsWith(ResponseDefinition.DocumentType))
                        {
                            return responseDefinitionProvider.WriteAsync(o.ToObject<ResponseDefinition>()) as Task;
                        }
                        return Task.CompletedTask;
                    });
            Task.WhenAll(documentTasks).ConfigureAwait(false).GetAwaiter().GetResult();
        }
    }
}

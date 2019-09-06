using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;

namespace Trask.Bot.EventBot
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var host = BuildWebHost(args);
            DatabaseSeeder.SeedData(host.Services, "initial-documents");
            host.Run();
        }

        public static IWebHost BuildWebHost(string[] args) =>
            WebHost.CreateDefaultBuilder(args)
                .UseStartup<Startup>()
                .UseApplicationInsights()
                .Build();
    }
}

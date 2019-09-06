using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Bot.Builder.Integration.AspNet.Core;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Trask.Bot.Auth.Schema;
using Trask.Bot.Azure.Services;
using Trask.Bot.Options;
using Trask.Bot.Storage;

namespace Trask.Bot.EventBot
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddEventBot<EventBot>(options =>
            {
                Configuration.GetSection("EventBotOptions").Bind(options);
                Configuration.GetSection("EventBotCosmosDbOptions")?.Bind(options.DocumentDbOptions);
                Configuration.GetSection("EventBotAzureStorageOptions").Bind(options.AzureStorageOptions);
                if (options.DocumentDbOptions != null &&
                    (options.AuthenticationDataStorageType == BotStorageType.DocumentDb ||
                    options.BotStatesStorageType == BotStorageType.DocumentDb ||
                    options.ResourceDefinitionStorageType == BotStorageType.DocumentDb ||
                    options.ResponseDefinitionStorageType == BotStorageType.DocumentDb ||
                    options.TranscriptStorageType == BotStorageType.DocumentDb))
                {
                    services.AddSingleton<IDocumentRepository, DefaultDocumentRepository>(sp => new DefaultDocumentRepository(options.DocumentDbOptions));
                }
            });

            services.AddMvc()
                    .SetCompatibilityVersion(CompatibilityVersion.Version_2_1);
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            else
            {
                app.UseHsts();
                app.UseHttpsRedirection();
            }

            app.UseMvc();
            app.UseBotFramework();
        }
    }
}
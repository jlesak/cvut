using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;
using Trask.Bot.EventBot.Options;
using Trask.Bot.QnA;
using Trask.Bot.Services.Microsoft.QnA;

namespace Trask.Bot.EventBot.Recognition
{
    public class NlpQnAProvider : IQnAProvider
    {
        private static HttpClient httpClient = new HttpClient();
        private readonly NlpQnAProviderOptions options;

        public NlpQnAProvider(NlpQnAProviderOptions options)
        {
            this.options = options ?? throw new ArgumentNullException(nameof(options));
        }

        public async Task<TQnAQueryResult[]> GetAnswers<TQnAQueryResult>(IQnAQueryOptions qnaQueryOptions) where TQnAQueryResult : IQnAQueryResult, new()
        {
            if (qnaQueryOptions == null)
            {
                throw new ArgumentNullException(nameof(qnaQueryOptions));
            }

            string getAnswerUrl = $"{options.ServiceEndpoint}{options.SvdMatch}?modelname={options.ModelName}&irmode=F&rettype=json&maxlen=5&sentence={qnaQueryOptions.Question}";
            using (var response = await httpClient.GetAsync(getAnswerUrl).ConfigureAwait(false))
            {
                if (!response.IsSuccessStatusCode)
                {
                    System.Diagnostics.Trace.TraceWarning($"'Request Url': '{getAnswerUrl}', 'StatusCode': '{response.StatusCode}', 'Response': '{await response.Content.ReadAsStringAsync().ConfigureAwait(false)}'");
                    return new TQnAQueryResult[0];
                }

                var jsonResponse = await response.Content.ReadAsStringAsync().ConfigureAwait(false);
                var queryResult = JsonConvert.DeserializeObject<TQnAQueryResult>(jsonResponse);

                return new[] { queryResult };
            }
        }       
    }
}
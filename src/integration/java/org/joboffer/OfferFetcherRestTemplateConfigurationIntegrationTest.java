package org.joboffer;

import org.joboffer.domain.offer.OfferFetcher;
import org.joboffer.infrastructure.offer.http.HttpClientConnectionConfigurationProperties;
import org.joboffer.infrastructure.offer.http.OfferFetcherClientConfig;
import org.springframework.web.client.RestTemplate;

public class OfferFetcherRestTemplateConfigurationIntegrationTest extends OfferFetcherClientConfig {

    public OfferFetcher remoteOfferFetcherClientTest(long connectionTimeout, long readTimeout, String uri, int port) {
        HttpClientConnectionConfigurationProperties properties = new HttpClientConnectionConfigurationProperties(
                connectionTimeout,
                readTimeout,
                uri,
                port
        );
        RestTemplate restTemplate = restTemplate(properties);
        return remoteOfferFetcherClient(restTemplate, properties);
    }
}

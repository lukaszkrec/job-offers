package org.joboffer.infrastructure.offer.http;

import org.joboffer.domain.offer.OfferFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
class OfferFetcherClientConfig {


    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }


    @Bean
    public RestTemplate restTemplate(
            @Value("${offer.http.client.config.connectionTimeout}") long connectionTimeout,
                                     @Value("${offer.http.client.config.readTimeout}") long readTimeout,
                                     RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

    @Bean
    public OfferFetcher remoteOfferFetcherClient(RestTemplate restTemplate,
                                                 @Value("${offer.http.client.config.uri}") String uri,
                                                 @Value("${offer.http.client.config.port}") int port) {
        return new OfferFetcherRestTemplate(restTemplate, uri , port);
    }
}
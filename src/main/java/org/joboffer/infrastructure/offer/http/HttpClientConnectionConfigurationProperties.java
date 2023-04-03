package org.joboffer.infrastructure.offer.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "offer.http.client.config")
public record HttpClientConnectionConfigurationProperties(long connectionTimeout,
                                                          long readTimeout,
                                                          String uri,
                                                          int port) {
}

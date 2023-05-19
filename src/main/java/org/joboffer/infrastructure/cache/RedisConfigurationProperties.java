package org.joboffer.infrastructure.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.redis")
public record RedisConfigurationProperties(String host, int port) {
}

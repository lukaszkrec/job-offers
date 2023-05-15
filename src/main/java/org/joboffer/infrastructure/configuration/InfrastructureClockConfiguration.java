package org.joboffer.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class InfrastructureClockConfiguration {
    @Bean
    Clock clock() {
        return Clock.fixed(Instant.now(), ZoneId.systemDefault());
    }
}

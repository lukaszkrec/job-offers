package org.joboffer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
class IntegrationTestConfig {

    @Primary
    @Bean
    Clock fixedClock() {
        return Clock.fixed(Instant.parse("2023-05-01T17:33:47.877+02:00"), ZoneId.systemDefault());
    }
}

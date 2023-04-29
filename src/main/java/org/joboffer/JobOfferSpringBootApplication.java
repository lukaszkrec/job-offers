package org.joboffer;

import org.joboffer.infrastructure.offer.http.HttpClientConnectionConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableConfigurationProperties(HttpClientConnectionConfigurationProperties.class)
public class JobOfferSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOfferSpringBootApplication.class, args);

    }
}

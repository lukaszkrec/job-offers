package org.joboffer.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OfferFacadeConfiguration {

    @Bean
    OfferFacade offerFacade(OfferRepository repository, OfferFetcher offerFetcher) {
        OfferValidation validation = new OfferValidation(repository);
        return new OfferFacade(repository, validation, offerFetcher);
    }
}

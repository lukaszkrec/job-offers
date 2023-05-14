package org.joboffer.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

@Configuration
class OfferFacadeConfiguration {

    @Bean
    OfferFacade offerFacade(OfferRepository repository, OfferFetcher offerFetcher) {
        final OfferValidation validation = new OfferValidation(repository);
        return new OfferFacade(repository, validation, offerFetcher);
    }

    @Bean
    OfferIdentifierListener customerListener(MongoOperations mongoOperations) {
        final PrimarySequenceService sequenceService = new PrimarySequenceService(mongoOperations);
        return new OfferIdentifierListener(sequenceService);
    }
}

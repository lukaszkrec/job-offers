package org.joboffer.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface OfferRepository extends MongoRepository<Offer, String> {
    Optional<Offer> findOfferById(String id);

    Optional<Offer> findOfferByOfferUrl(String url);

    boolean existsByOfferUrl(String url);
}

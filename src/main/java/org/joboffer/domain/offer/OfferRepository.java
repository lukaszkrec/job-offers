package org.joboffer.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends MongoRepository<Offer, String> {

    List<Offer> findAll();

    Optional<Offer> findOfferById(String id);

    Offer save(Offer offer);

    Optional<Offer> findOfferByOfferUrl(String url);

    boolean existsByOfferUrl(String url);
}

package org.joboffer.domain.offer;

import java.util.List;
import java.util.Optional;

interface OfferRepository {

    List<Offer> findAllOffers();

    Optional<Offer> findOfferById(String id);

    Offer save(Offer offer);

    Optional<Offer> findByUrl(String url);

    boolean existsByUrl(String url);

    List<Offer> saveAll(List<Offer> offers);
}

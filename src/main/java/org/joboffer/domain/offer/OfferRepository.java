package org.joboffer.domain.offer;

import java.util.List;
import java.util.Map;

interface OfferRepository {

    List<Offer> findAllOffers();

    Offer findOfferById(String id);

    Offer save(Offer offer);

    void fetchAllOffersAndSaveAllIfNotExist(Map<String, Offer> offers);
}

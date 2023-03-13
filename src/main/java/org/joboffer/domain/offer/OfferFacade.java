package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepositoryImpl repositoryImpl;

    public Map<String, Offer> findAllOffers() {
        return repositoryImpl.findAllOffers();
    }

    public Offer findOfferById(String offerId) {
        return repositoryImpl.findOfferById(offerId);
    }

    public Offer save(Offer offer) {
        return repositoryImpl.save(offer);
    }

    public void fetchAllOffersAndSaveAllIffNotExist(Map<String, Offer> offers) {

    }


}

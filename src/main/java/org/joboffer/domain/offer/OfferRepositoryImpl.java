package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
class OfferRepositoryImpl implements OfferRepository {

    private final Map<String, Offer> offers = new HashMap<>();


    @Override
    public Map<String, Offer> findAllOffers() {
        return offers;
    }

    @Override
    public Offer findOfferById(String offerId) {
        if (offerWithIdDoesNotExist(offerId)) {
            throw new IllegalArgumentException("Offer with id " + offerId + " does not exist");
        }
        return offers.get(offerId);
    }

    @Override
    public Offer save(Offer offer) {
        if (offerIdIsNotNull(offer)) {
            throw new IllegalArgumentException("Offer must have an id");
        }
        if (offerExistsWithSameId(offer.getId())) {
            throw new IllegalArgumentException("Offer already exists");
        }
        offers.put(offer.getId(), offer);
        return offer;
    }

    @Override
    public void fetchAllOffersAndSaveAllIfNotExist(Map<String, Offer> offersToSave) {
        offersToSave.values()
                .stream()
                .filter(this::offerDoesNotExists)
                .forEach(offer -> offers.put(offer.getId(), offer));
    }

    private boolean offerIdIsNotNull(Offer offer) {
        return offer.getId() == null;
    }

    private boolean offerExistsWithSameId(String offerId) {
        return offers.keySet().stream()
                .anyMatch(id -> id.equals(offerId));
    }

    private boolean offerWithIdDoesNotExist(String offerId) {
        return !offers.containsKey(offerId);
    }

    private boolean offerDoesNotExists(Offer offer) {
        return !offers.containsValue(offer);
    }
}

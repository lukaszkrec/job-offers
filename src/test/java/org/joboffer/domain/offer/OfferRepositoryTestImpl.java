package org.joboffer.domain.offer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class OfferRepositoryTestImpl implements OfferRepository {

    private final Map<String, Offer> offers = new HashMap<>();

    @Override
    public Offer save(Offer offerToSave) {
        return offers.put(offerToSave.getId(), offerToSave);
    }

    @Override
    public List<Offer> findAllOffers() {
        return offers.values().stream().toList();
    }


    @Override
    public Optional<Offer> findOfferById(String offerId) {
        return offers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(offerId))
                .map(Map.Entry::getValue)
                .findFirst();
    }


    @Override
    public List<Offer> fetchAllOffersAndSaveAllIfNotExist(List<Offer> offerList) {
        return offerList.stream()
                .map(this::save)
                .toList();
    }
}

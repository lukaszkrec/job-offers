package org.joboffer.domain.offer;

import java.util.*;

class OfferRepositoryTestImpl implements OfferRepository {

    private final Map<String, Offer> offers = new HashMap<>();

    @Override
    public Offer save(Offer offerToSave) {
        return offers.put(offerToSave.getId(), offerToSave);
    }

    @Override
    public Optional<Offer> findByUrl(String url) {
        return offers.values()
                .stream()
                .filter(offer -> offer.getOfferUrl().equals(url))
                .findFirst();
    }

    @Override
    public boolean existsByUrl(String url) {
        return offers.values()
                .stream()
                .noneMatch(offer -> offer.getOfferUrl().equals(url));
    }

    @Override
    public List<Offer> saveAll(List<Offer> offerList) {
        return offerList.stream()
                .map(this::save)
                .toList();
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
}

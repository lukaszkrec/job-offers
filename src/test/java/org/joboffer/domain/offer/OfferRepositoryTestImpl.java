package org.joboffer.domain.offer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OfferRepositoryTestImpl implements OfferRepository {

    private final Map<String, Offer> offers = new HashMap<>();

    @Override
    public List<Offer> findAllOffers() {
        return offers.values().stream().toList();
    }


    @Override
    public Offer findOfferById(String offerId) {
        checkingIfOfferWithIdDoesExist(offerId);
        return offers.get(offerId);
    }


    @Override
    public Offer save(Offer savedOffer) {
        checkingIfTheOfferIdIsNotNull(savedOffer);
        checkingIfAnOfferWithTheSameIdExist(savedOffer);
        offers.put(savedOffer.getId(), savedOffer);
        return savedOffer;

    }

    @Override
    public void fetchAllOffersAndSaveAllIfNotExist(Map<String, Offer> offerDtos) {
        offerDtos.values().stream()
                .filter(this::offerDoesNotExists)
                .forEach(offer -> offers.put(offer.getId(), offer));
    }

    boolean offerDoesNotExists(Offer offer) {
        return offers.values()
                .stream()
                .noneMatch(existingOffer -> existingOffer.equals(offer));
    }

    boolean offerIdIsNotNull(Offer offer) {
        return offer.getId() == null;
    }

    boolean offerExistsWithSameId(String offerId) {
        return offers.keySet().stream()
                .anyMatch(id -> id.equals(offerId));

    }

    boolean offerWithIdDoesNotExist(String offerId) {
        return offers.keySet().stream()
                .noneMatch(id -> id.equals(offerId));
    }

    void checkingIfAnOfferWithTheSameIdExist(Offer savedOffer) {
        if (offerExistsWithSameId(savedOffer.getId())) {
            throw new OfferAlreadyExistException("Offer with id " + savedOffer.getId() + " already exists");
        }
    }

    void checkingIfTheOfferIdIsNotNull(Offer savedOffer) {
        if (offerIdIsNotNull(savedOffer)) {
            throw new IllegalArgumentException("Offer must have an id");
        }
    }

    void checkingIfOfferWithIdDoesExist(String offerId) {
        if (offerWithIdDoesNotExist(offerId)) {
            throw new OfferDoesNotExist("Offer with id " + offerId + " does not exist");
        }
    }
}

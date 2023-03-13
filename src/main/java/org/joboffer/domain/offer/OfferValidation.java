package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class OfferValidation {

    private final OfferRepository repository;

    boolean offerIdIsNotNull(Offer offer) {
        return offer.getId() == null;
    }

    boolean offerExistsWithSameId(String offerId) {
        return repository.findAllOffers().stream()
                .anyMatch(offerDto -> offerDto.getId().equals(offerId));
    }

    boolean offerWithIdDoesNotExist(String offerId) {
        return !repository.findAllOffers().contains(repository.findOfferById(offerId));
    }

    boolean offerDoesNotExists(Offer offer) {
        return !repository.findAllOffers().contains(offer);
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

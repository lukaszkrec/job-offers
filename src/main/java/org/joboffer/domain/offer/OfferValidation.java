package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

@AllArgsConstructor
class OfferValidation {

    private final OfferRepository repository;

    boolean checkingIfOfferIdIsNull(OfferDto offerDto) {
        return offerDto.getId() == null;
    }

    boolean checkingIfOfferExistsWithSameId(String offerId) {
        return repository.findAllOffers()
                .stream()
                .anyMatch(offerDto -> offerDto.getId().equals(offerId));
    }

    boolean checkingIfOfferDoesNotExists(Offer offer) {
        return !repository.findAllOffers().contains(offer);
    }

    boolean checkingIfOffersWithGivenUrlAlreadyExist(OfferDto offerDto) {
        return repository.findAllOffers()
                .stream()
                .anyMatch(filteredOffer -> filteredOffer.getUrl().equals(offerDto.getUrl()));
    }


    boolean checkingIfOfferWithGivenIdExist(Offer serchedOffer) {
        return repository.findAllOffers()
                .stream()
                .anyMatch(offer -> offer.getId().equals(serchedOffer.getId()));
    }
}

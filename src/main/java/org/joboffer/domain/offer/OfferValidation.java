package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

@AllArgsConstructor
class OfferValidation {

    private final OfferRepository repository;

    boolean checkingIfOfferExistsWithSameId(String offerId) {
        return repository.findAll()
                .stream()
                .anyMatch(offerDto -> offerDto.getId().equals(offerId));
    }

    boolean checkingIfOffersWithGivenUrlAlreadyExist(OfferDto offerDto) {
        return repository.findAll()
                .stream()
                .anyMatch(filteredOffer -> filteredOffer.getOfferUrl().equals(offerDto.getOfferUrl()));
    }

    boolean checkingIfOfferIdIsNull(OfferDto offerDto) {
        return offerDto.getId() == null;
    }

    boolean checkingIfOfferUrlIsNotNullAndUrlInNotEmpty(Offer offer) {
        return repository.findOfferByOfferUrl(offer.getOfferUrl()).isEmpty();
    }
}

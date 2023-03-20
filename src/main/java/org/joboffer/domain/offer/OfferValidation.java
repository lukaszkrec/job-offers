package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

@AllArgsConstructor
class OfferValidation {

    private final OfferRepository repository;

    boolean checkingIfOfferExistsWithSameId(String offerId) {
        return repository.findAllOffers()
                .stream()
                .anyMatch(offerDto -> offerDto.getId().equals(offerId));
    }

    boolean checkingIfOffersWithGivenUrlAlreadyExist(OfferDto offerDto) {
        return repository.findAllOffers()
                .stream()
                .anyMatch(filteredOffer -> filteredOffer.getUrl().equals(offerDto.getUrl()));
    }


    boolean checkingIfOfferIdIsNull(OfferDto offerDto) {
        return offerDto.getId() == null;
    }

    boolean checkingIfOfferWithGivenIdExist(Offer serchedOffer) {
        return repository.findAllOffers()
                .stream()
                .anyMatch(offer -> offer.getId().equals(serchedOffer.getId()));
    }

    boolean checkingIfOfferDoesNotExistsByOfferUrl(Offer offer) {
        return repository.existsByUrl(offer.getUrl());

    }

    boolean checkingIfOfferUrlIsNotNullAndUrlInNotEmpty(Offer offer) {
        return repository.findByUrl(offer.getUrl()).isEmpty();
    }
}

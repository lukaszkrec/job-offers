package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

import java.util.List;

import static org.joboffer.domain.offer.OfferMapper.mapToOfferDto;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository repository;
    private final OfferValidation offerValidation;

    public List<OfferDto> findAllOffers() {
        return repository.findAllOffers().stream()
                .map(OfferMapper::mapToOfferDto)
                .toList();
    }


    public OfferDto findOfferById(String offerId) {
        offerValidation.checkingIfOfferWithIdDoesExist(offerId);
        Offer offer = repository.findOfferById(offerId);
        return mapToOfferDto(offer);
    }


    public OfferDto save(Offer savedOffer) {
        offerValidation.checkingIfTheOfferIdIsNotNull(savedOffer);
        offerValidation.checkingIfAnOfferWithTheSameIdExist(savedOffer);
        Offer offer = repository.save(savedOffer);
        return mapToOfferDto(offer);

    }


    public void fetchAllOffersAndSaveAllIfNotExist(List<OfferDto> offerDtos) {
        offerDtos.stream()
                .map(OfferMapper::mapOffer)
                .filter(offerValidation::offerDoesNotExists)
                .forEach(repository::save);
    }
}

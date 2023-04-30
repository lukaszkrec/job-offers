package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

import java.util.List;

import static org.joboffer.domain.offer.OfferMapper.mapToOffer;
import static org.joboffer.domain.offer.OfferMapper.mapToOfferDto;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository repository;
    private final OfferValidation offerValidation;
    private final OfferFetcher offerFetcher;

    public OfferDto register(OfferDto offer) {
        if (offerValidation.checkingIfOfferIdIsNull(offer)) {
            throw new OfferParametersCredentialException("Offer id can not be: " + null);
        }
        if (offerValidation.checkingIfOfferExistsWithSameId(offer.getId())) {
            throw new OfferParametersCredentialException("Offer with id: " + offer.getId() + " already exists");
        }
        if (offerValidation.checkingIfOffersWithGivenUrlAlreadyExist(offer)) {
            throw new DuplicatedKeyException("Offer with the same url: " + offer.getOfferUrl() + " already exists");
        }
        Offer mappedOffer = mapToOffer(offer);
        repository.save(mappedOffer);
        return mapToOfferDto(mappedOffer);
    }

    public List<OfferDto> findAllOffers() {
        return repository.findAll().stream()
                .map(OfferMapper::mapToOfferDto)
                .toList();
    }

    public OfferDto findOfferById(String offerId) {
        return repository.findOfferById(offerId)
                .filter(offerValidation::checkingIfOfferWithGivenIdExist)
                .map(OfferMapper::mapToOfferDto)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " does not exist"));
    }

    public List<OfferDto> fetchAllOffersAndSaveAllIfNotExist() {
        List<Offer> fetchedOffers = offerFetcher.fetchAllOffers()
                .stream()
                .map(OfferMapper::mapToOffer)
                .filter(offerValidation::checkingIfOfferDoesNotExistsByOfferUrl)
                .filter(offerValidation::checkingIfOfferUrlIsNotNullAndUrlInNotEmpty)
                .toList();
        repository.saveAll(fetchedOffers);
        return fetchedOffers
                .stream()
                .map(OfferMapper::mapToOfferDto)
                .toList();
    }
}

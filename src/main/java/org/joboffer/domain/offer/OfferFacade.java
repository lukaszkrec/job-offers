package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

import java.util.List;

import static org.joboffer.domain.offer.OfferMapper.mapToOffer;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository repository;
    private final OfferValidation offerValidation;

    public Offer register(OfferDto offer) {
        if (offerValidation.checkingIfOfferIdIsNull(offer)) {
            throw new OfferParametersCredentialException("Offer id can not be: " + null);
        }
        if (offerValidation.checkingIfOfferExistsWithSameId(offer.getId())) {
            throw new OfferParametersCredentialException("Offer with id: " + offer.getId() + " already exists");
        }
        if (offerValidation.checkingIfOffersWithGivenUrlAlreadyExist(offer)) {
            throw new DuplicatedKeyException("Offer with the same url: " + offer.getUrl() + " already exists");
        }
        Offer mappedOffer = mapToOffer(offer);
        return repository.save(mappedOffer);
    }

    public List<OfferDto> findAllOffers() {
        return repository.findAllOffers().stream()
                .map(OfferMapper::mapToOfferDto)
                .toList();
    }


    public OfferDto findOfferById(String offerId) {
        return repository.findOfferById(offerId)
                .filter(offerValidation::checkingIfOfferWithGivenIdExist)
                .map(OfferMapper::mapToOfferDto)
                .orElseThrow(() -> new OfferNotFoundException("Offer with id: " + offerId + " does not exist"));
    }


    public List<Offer> fetchAllOffersAndSaveAllIfNotExist(List<Offer> offerList) {
        return offerList.stream()
                .filter(offerValidation::checkingIfOfferDoesNotExists)
                .map(offers -> repository.fetchAllOffersAndSaveAllIfNotExist(offerList))
                .flatMap(List::stream)
                .toList();

    }
}

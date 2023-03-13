package org.joboffer.domain.offer;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

import java.util.List;

import static org.joboffer.domain.offer.OfferMapper.mapToOfferDto;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository repository;

    public List<OfferDto> findAllOffers() {
        return repository.findAllOffers().stream()
                .map(OfferMapper::mapToOfferDto)
                .toList();
    }


    public OfferDto findOfferById(String offerId) {
        if (offerWithIdDoesNotExist(offerId)) {
            throw new OfferDoesNotExist("Offer with id " + offerId + " does not exist");
        }
        Offer offer = repository.findOfferById(offerId);
        return mapToOfferDto(offer);
    }


    public OfferDto save(Offer savedOffer) {
        if (offerIdIsNotNull(savedOffer)) {
            throw new IllegalArgumentException("Offer must have an id");
        }
        if (offerExistsWithSameId(savedOffer.getId())) {
            throw new OfferAlreadyExistException("Offer with id " + savedOffer.getId() + " already exists");
        }
        Offer offer = repository.save(savedOffer);
        return mapToOfferDto(offer);

    }


    public void fetchAllOffersAndSaveAllIfNotExist(List<OfferDto> offerDtos) {
        offerDtos.stream()
                .map(OfferMapper::mapOffer)
                .filter(this::offerDoesNotExists)
                .forEach(repository::save);
    }

    private boolean offerIdIsNotNull(Offer offer) {
        return offer.getId() == null;
    }

    private boolean offerExistsWithSameId(String offerId) {
        return repository.findAllOffers().stream()
                .anyMatch(offerDto -> offerDto.getId().equals(offerId));
    }

    private boolean offerWithIdDoesNotExist(String offerId) {
        return !repository.findAllOffers().contains(repository.findOfferById(offerId));
    }

    private boolean offerDoesNotExists(Offer offer) {
        return !repository.findAllOffers().contains(offer);
    }
}

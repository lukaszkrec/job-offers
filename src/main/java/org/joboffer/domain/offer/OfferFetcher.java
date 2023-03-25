package org.joboffer.domain.offer;

import org.joboffer.domain.offer.dto.OfferDto;

import java.util.List;

public interface OfferFetcher {

    List<OfferDto> fetchAllOffers();
}

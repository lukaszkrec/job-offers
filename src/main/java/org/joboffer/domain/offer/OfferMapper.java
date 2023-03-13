package org.joboffer.domain.offer;

import org.joboffer.domain.offer.dto.OfferDto;

class OfferMapper {

    static OfferDto mapToOfferDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .url(offer.getUrl())
                .workSite(offer.getWorkSite())
                .company(offer.getCompany())
                .salary(offer.getSalary())
                .build();
    }

    static Offer mapOffer(OfferDto dto) {
        return Offer.builder()
                .id(dto.getId())
                .url(dto.getUrl())
                .workSite(dto.getWorkSite())
                .company(dto.getCompany())
                .salary(dto.getSalary())
                .build();
    }

}

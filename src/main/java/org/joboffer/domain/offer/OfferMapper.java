package org.joboffer.domain.offer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    static Offer mapToOffer(OfferDto dto) {
        return Offer.builder()
                .id(dto.getId())
                .url(dto.getUrl())
                .workSite(dto.getWorkSite())
                .company(dto.getCompany())
                .salary(dto.getSalary())
                .build();
    }
}

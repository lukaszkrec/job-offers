package org.joboffer.domain.offer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class OfferMapper {

    static OfferDto mapToOfferDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .company(offer.getCompany())
                .salary(offer.getSalary())
                .offerUrl(offer.getOfferUrl())
                .build();
    }

    static Offer mapToOffer(OfferDto dto) {
        return Offer.builder()
                .id(dto.id())
                .title(dto.title())
                .company(dto.company())
                .salary(dto.salary())
                .offerUrl(dto.offerUrl())
                .build();
    }
}

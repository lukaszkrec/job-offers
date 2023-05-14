package org.joboffer.domain.offer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.joboffer.domain.offer.dto.OfferDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferMapper {

    public static OfferDto mapToOfferDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .company(offer.getCompany())
                .salary(offer.getSalary())
                .offerUrl(offer.getOfferUrl())
                .build();
    }

    public static Offer mapToOffer(OfferDto dto) {
        return Offer.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .company(dto.getCompany())
                .salary(dto.getSalary())
                .offerUrl(dto.getOfferUrl())
                .build();
    }
}

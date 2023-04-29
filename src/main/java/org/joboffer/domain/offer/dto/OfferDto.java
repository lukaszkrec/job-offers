package org.joboffer.domain.offer.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class OfferDto {
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String company;
    @NonNull
    private String salary;
    @NonNull
    private String offerUrl;
}


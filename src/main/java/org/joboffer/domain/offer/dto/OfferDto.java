package org.joboffer.domain.offer.dto;

import lombok.*;

@Data
@Builder
public class OfferDto {
    private String id;
    private String url;
    private String workSite;
    private String company;
    private String salary;
}

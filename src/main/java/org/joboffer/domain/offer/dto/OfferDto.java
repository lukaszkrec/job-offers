package org.joboffer.domain.offer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {
    private String id;
    @NotEmpty(message = "Title is mandatory and can not be null")
    private String title;
    @NotEmpty(message = "Company is mandatory and can not be null")
    private String company;
    @NotEmpty(message = "Salary is mandatory and can not be null")
    private String salary;
    @NotEmpty(message = "OfferUrl is mandatory and can not be null")
    private String offerUrl;
}


package org.joboffer.domain.offer.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Builder
public record OfferDto(String id,
                       @NotEmpty(message = "Title is mandatory and can not be null")
                       String title,
                       @NotEmpty(message = "Company is mandatory and can not be null")
                       String company,
                       @NotEmpty(message = "Salary is mandatory and can not be null")
                       String salary,
                       @NotEmpty(message = "OfferUrl is mandatory and can not be null")
                       String offerUrl
) implements Serializable {
}


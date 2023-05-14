package org.joboffer.infrastructure.offer.apivalidation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Violation {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    private String property;
    private String rejectedValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> constraintPropertyPath;
}
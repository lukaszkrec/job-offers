package org.joboffer.infrastructure.offer.apivalidation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ValidationErrorResponse {

    private String status;
    private String message;
    private OffsetDateTime timestamp;
    private List<Violation> violations;
}

package org.joboffer.infrastructure.offer.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
class ErrorResponse {
    private String status;
    private OffsetDateTime timestamp;
    private String message;
    private String description;
}

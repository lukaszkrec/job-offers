package org.joboffer.infrastructure.error;

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
    private String errorStatus;
    private OffsetDateTime timestamp;
    private String message;
    private String description;
}

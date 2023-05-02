package org.joboffer.infrastructure.exceptionhandling;

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
    private String statusCode;
    private OffsetDateTime date;
    private String message;
    private String description;
}

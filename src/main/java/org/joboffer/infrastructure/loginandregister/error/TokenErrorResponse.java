package org.joboffer.infrastructure.loginandregister.error;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record TokenErrorResponse(String status,
                                 OffsetDateTime timestamp,
                                 String message,
                                 String description) {
}

package org.joboffer.infrastructure.loginandregister.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record TokenErrorResponse(String message,
                                 HttpStatus status) {
}

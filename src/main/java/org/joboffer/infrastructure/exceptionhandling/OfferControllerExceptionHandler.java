package org.joboffer.infrastructure.exceptionhandling;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.DuplicatedKeyException;
import org.joboffer.domain.offer.OfferNotFoundException;
import org.joboffer.domain.offer.OfferParametersCredentialException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Clock;
import java.time.OffsetDateTime;

@ControllerAdvice
@AllArgsConstructor
public class OfferControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final Clock clock;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOfferNotFoundException(OfferNotFoundException exception, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.toString())
                .date(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({OfferParametersCredentialException.class, DuplicatedKeyException.class})
    public ResponseEntity<ErrorResponse> handleOfferParametersCredentialException(OfferParametersCredentialException exception, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .date(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errorResponse);
    }
}

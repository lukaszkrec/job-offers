package org.joboffer.infrastructure.offer.error;

import lombok.AllArgsConstructor;

import org.joboffer.domain.offer.OfferNotFoundException;
import org.joboffer.domain.offer.OfferParametersCredentialException;
import org.springframework.dao.DuplicateKeyException;
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
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedKeyException(DuplicateKeyException exception, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);


    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(OfferParametersCredentialException.class)
    public ResponseEntity<ErrorResponse> handleOfferParametersCredentialException(OfferParametersCredentialException exception, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .timestamp(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errorResponse);

    }
}

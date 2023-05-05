package org.joboffer.infrastructure.error;

import lombok.AllArgsConstructor;

import org.joboffer.domain.offer.OfferNotFoundException;
import org.joboffer.domain.offer.OfferParametersCredentialException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
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
    public ErrorResponse handleOfferNotFoundException(OfferNotFoundException exception, WebRequest request) {
        return ErrorResponse.builder()
                .errorStatus(HttpStatus.NOT_FOUND.toString())
                .timestamp(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateKeyException.class)
    public ErrorResponse handleDuplicatedKeyException(DuplicateKeyException exception, WebRequest request) {
        return ErrorResponse.builder()
                .errorStatus(HttpStatus.NOT_FOUND.toString())
                .timestamp(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(OfferParametersCredentialException.class)
    public ErrorResponse handleOfferParametersCredentialException(OfferParametersCredentialException exception, WebRequest request) {
        return ErrorResponse.builder()
                .errorStatus(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .timestamp(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();

    }
}

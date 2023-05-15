package org.joboffer.infrastructure.loginandregister.error;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.OffsetDateTime;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class LoginAndRegisterControllerErrorHandler {

    private final Clock clock;
    private static final String BAD_CREDENTIALS = "Bad Credentials";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<TokenErrorResponse> handleBadCredentialsException(BadCredentialsException exception, WebRequest request) {
        TokenErrorResponse tokenErrorResponse = TokenErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .timestamp(OffsetDateTime.now(clock))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(tokenErrorResponse);
    }
}

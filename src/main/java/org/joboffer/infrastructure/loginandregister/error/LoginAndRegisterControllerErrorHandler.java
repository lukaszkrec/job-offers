package org.joboffer.infrastructure.loginandregister.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class LoginAndRegisterControllerErrorHandler {

    private static final String BAD_CREDENTIALS = "Bad Credentials";

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<TokenErrorResponse> handleBadCredentialsException() {
        TokenErrorResponse tokenErrorResponse = TokenErrorResponse.builder()
                .message(BAD_CREDENTIALS)
                .status(HttpStatus.UNAUTHORIZED)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(tokenErrorResponse);
    }
}

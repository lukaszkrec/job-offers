package org.joboffer.domain.loginandregister;

public class UserDoesNotExistException extends IllegalArgumentException {
    public UserDoesNotExistException(String message) {
        super(message);
    }
}

package org.joboffer.domain.loginandregister;

public class UserAlreadyExistException extends IllegalArgumentException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}

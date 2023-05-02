package org.joboffer.domain.offer;

public class DuplicatedKeyException extends RuntimeException {
    DuplicatedKeyException(String message) {
        super(message);
    }
}

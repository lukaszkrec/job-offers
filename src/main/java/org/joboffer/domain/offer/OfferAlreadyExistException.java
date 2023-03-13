package org.joboffer.domain.offer;

class OfferAlreadyExistException extends IllegalArgumentException {
    public OfferAlreadyExistException(String message) {
        super(message);
    }
}

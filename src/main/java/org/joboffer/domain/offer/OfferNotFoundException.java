package org.joboffer.domain.offer;

class OfferNotFoundException extends RuntimeException {
    OfferNotFoundException(String message) {
        super(message);
    }
}

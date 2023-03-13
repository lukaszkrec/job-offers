package org.joboffer.domain.offer;

class OfferDoesNotExist extends IllegalArgumentException {
    public OfferDoesNotExist(String message) {
        super(message);
    }
}

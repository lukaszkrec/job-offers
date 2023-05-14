package org.joboffer.domain.offer;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;


class OfferIdentifierListener extends AbstractMongoEventListener<Offer> {

    private final PrimarySequenceService primarySequenceService;

    OfferIdentifierListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Offer> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(String.valueOf(primarySequenceService.getNextValue()));
        }
    }
}
 
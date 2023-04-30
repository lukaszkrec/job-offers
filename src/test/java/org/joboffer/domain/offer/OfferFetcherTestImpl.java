package org.joboffer.domain.offer;

import org.joboffer.domain.offer.dto.OfferDto;

import java.util.ArrayList;
import java.util.List;

class OfferFetcherTestImpl implements OfferFetcher {

    private final List<Offer> offersToFetch = new ArrayList<>(
            List.of(
                    new Offer("Scrum Master", "Amazon", "353651235", "http://www.example.com1"),
                    new Offer("PO", "Apel", "1010431510", "http://www.example.com2"),
                    new Offer("QA", "Gogiel", "5613251", "http://www.example.com3"),
                    new Offer("PY Dev", "Media Expert", "634673", "http://www.example.com4"),
                    new Offer("C++ Dev", "Comarch", "321231", "http://www.example.com5"),
                    new Offer("C# Dev", "Sii", "64363", "http://www.example.com6"),
                    new Offer("Scala Dev", "GlobalLogic", "623622342", "http://www.example.com7"))
    );

    @Override
    public List<OfferDto> fetchAllOffers() {
        return offersToFetch
                .stream()
                .map(OfferMapper::mapToOfferDto)
                .toList();
    }
}

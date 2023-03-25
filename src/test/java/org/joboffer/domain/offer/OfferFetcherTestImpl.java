package org.joboffer.domain.offer;

import org.joboffer.domain.offer.dto.OfferDto;

import java.util.ArrayList;
import java.util.List;

class OfferFetcherTestImpl implements OfferFetcher {

    private final List<Offer> offersToFetch = new ArrayList<>(
            List.of(
                    new Offer("1L", "Scrum Master", "Amazon", "353651235", "http://www.example.com1"),
                    new Offer("2L", "PO", "Apel", "1010431510", "http://www.example.com2"),
                    new Offer("3L", "QA", "Gogiel", "5613251", "http://www.example.com3"),
                    new Offer("4L", "PY Dev", "Media Expert", "634673", "http://www.example.com4"),
                    new Offer("5L", "C++ Dev", "Comarch", "321231", "http://www.example.com5"),
                    new Offer("6L", "C# Dev", "Sii", "64363", "http://www.example.com6"),
                    new Offer("7L", "Scala Dev", "GlobalLogic", "623622342", "http://www.example.com7"))
    );

    @Override
    public List<OfferDto> fetchAllOffers() {
        return offersToFetch
                .stream()
                .map(OfferMapper::mapToOfferDto)
                .toList();
    }
}

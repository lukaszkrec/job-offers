package org.joboffer.domain.offer;

import java.util.ArrayList;
import java.util.List;

class OfferFetcherTestImpl implements OfferFetcher {

    private final List<Offer> offersToFetch = new ArrayList<>(
            List.of(
                    new Offer("1L", "http://www.example.com1", "Scrum Master", "Amazon", "101010"),
                    new Offer("2L", "http://www.example.com2", "PO", "Apel", "4141251"),
                    new Offer("3L", "http://www.example.com3", "QA", "Gogiel", "5135624734"),
                    new Offer("4L", "http://www.example.com4", "PY Dev", "Media Expert", "5145141"),
                    new Offer("5L", "http://www.example.com5", "C++ Dev", "Comarch", "5312514"),
                    new Offer("6L", "http://www.example.com6", "C# Dev", "Sii", "5235124"),
                    new Offer("7L", "http://www.example.com7", "Scala Dev", "GlobalLogic", "4141354135")
            )
    );

    @Override
    public List<Offer> fetchAllOffers() {
        return offersToFetch;
    }
}

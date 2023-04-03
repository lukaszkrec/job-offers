package org.joboffer.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joboffer.domain.offer.OfferFacade;
import org.joboffer.domain.offer.dto.OfferDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class OfferFetcherScheduler {

    private final OfferFacade offerFacade;
    private static final String STARTED_OFFERS_FETCHING_MESSAGE = "Started offers fetching {}";
    private static final String STOPPED_OFFERS_FETCHING_MESSAGE = "Stopped offers fetching {}";
    private static final String ADDED_NEW_OFFERS_MESSAGE = "Added new {} offers";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelayString = "${offer.http.client.config.fetcherRunOccurrence}")
    public List<OfferDto> scheduleOfferFetcher() {
        log.info(STARTED_OFFERS_FETCHING_MESSAGE, dateFormat.format(new Date()));
        List<OfferDto> fetchedOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        log.info(ADDED_NEW_OFFERS_MESSAGE, fetchedOffers.size());
        log.info(STOPPED_OFFERS_FETCHING_MESSAGE, dateFormat.format(new Date()));
        return fetchedOffers;
    }
}

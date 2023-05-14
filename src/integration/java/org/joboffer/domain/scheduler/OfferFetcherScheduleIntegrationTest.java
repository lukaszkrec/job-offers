package org.joboffer.domain.scheduler;

import org.joboffer.domain.BaseIntegrationTest;
import org.joboffer.JobOfferSpringBootApplication;
import org.joboffer.domain.offer.OfferFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = JobOfferSpringBootApplication.class, properties = "scheduling.enabled=true")
class OfferFetcherScheduleIntegrationTest extends BaseIntegrationTest {

    @SpyBean
    OfferFetcher offerFetcher;

    @Test
    void offerFetcherSchedulerCall() {
        await().atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> verify(offerFetcher, times(2)).fetchAllOffers());
    }
}

package org.joboffer.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.joboffer.BaseIntegrationTest;
import org.joboffer.SampleJobOfferResponse;
import org.joboffer.domain.offer.OfferFacade;
import org.joboffer.domain.offer.OfferRepository;
import org.joboffer.domain.offer.dto.OfferDto;
import org.joboffer.infrastructure.offer.scheduler.OfferFetcherScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/*
step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
step 8: there are 2 new offers in external HTTP server
step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
step 12: user made GET /offers/1000 and system returned OK(200) with offer
step 13: there are 2 new offers in external HTTP server
step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer
step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer
 */

class JobOfferResponseIntegrationTest extends BaseIntegrationTest implements SampleJobOfferResponse {

    @SpyBean
    OfferFacade offerFacade;

    @Autowired
    OfferFetcherScheduler fetcherScheduler;

    @Autowired
    OfferRepository offerRepository;

    @Test
    @DisplayName("There are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)")
    void should_return_empty_response_when_there_are_no_offers_in_external_http_server() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));

        //when
        List<OfferDto> offersFromExternalServer = fetcherScheduler.scheduleOfferFetcher();

        //then
        Assertions.assertThat(offersFromExternalServer).isEmpty();
    }

    @Test
    @DisplayName("Scheduler ran 1st time and made GET to external server and system added 0 offers to database")
    void should_call_external_server_and_fetched_no_offers_when_there_are_no_offers_in_external_server() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));

        //when

        List<OfferDto> fetchedOffers = fetcherScheduler.scheduleOfferFetcher();

        //then
        assertAll(
                () -> Assertions.assertThat(fetchedOffers).isEmpty(),
                () -> Assertions.assertThat(offerRepository.findAll()).isEmpty(),
                () -> Mockito.verify(offerFacade, times(1)).fetchAllOffersAndSaveAllIfNotExist()
        );
    }
}

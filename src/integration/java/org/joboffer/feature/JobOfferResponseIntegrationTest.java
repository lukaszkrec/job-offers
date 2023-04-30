package org.joboffer.feature;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JobOfferResponseIntegrationTest extends BaseIntegrationTest implements SampleJobOfferResponse {

    @SpyBean
    OfferFacade offerFacade;

    @Autowired
    OfferFetcherScheduler fetcherScheduler;

    @Autowired
    OfferRepository offerRepository;


    @Test
    @DisplayName("User tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)")
    void test11() {
    }

    @Test
    @DisplayName("User made GET /offers with no jwt token and system returned UNAUTHORIZED(401)")
    void test12() {
    }

    @Test
    @DisplayName("User made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)")
    void test13() {
    }

    @Test
    @DisplayName("User tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC")
    void test14() {
    }


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

    @Test
    @DisplayName("User made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers")
    void test1() throws Exception {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));

        List<OfferDto> offersFromExternalServer = fetcherScheduler.scheduleOfferFetcher();
        Assertions.assertThat(offersFromExternalServer).isEmpty();

        //when
        MvcResult mvcResult = mvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseWithNoOffers = mvcResult.getResponse().getContentAsString();
        List<OfferDto> offerResponse = objectMapper.readValue(responseWithNoOffers, new TypeReference<>() {
        });

        assertAll(
                () -> Assertions.assertThat(offerResponse).isEmpty(),
                () -> Assertions.assertThat(offerRepository.findAll()).isEmpty(),
                () -> Mockito.verify(offerFacade, times(1)).fetchAllOffersAndSaveAllIfNotExist()
        );
    }

    @Test
    @DisplayName("There are 2 new offers in external HTTP server")
    void test2() {
    }

    @Test
    @DisplayName("Scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database")
    void test3() {

    }

    @Test
    @DisplayName("User made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”")
    void test4() {

    }

    @Test
    @DisplayName("User made GET /offers/1000 and system returned OK(200) with offer")
    void test5() {

    }

    @Test
    @DisplayName("There are 2 new offers in external HTTP server")
    void test6() {

    }

    @Test
    @DisplayName("Scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database")
    void test7() {

    }

    @Test
    @DisplayName("User made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer")
    void test8() {

    }

    @Test
    @DisplayName("User made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer")
    void test9() {

    }

    @Test
    @DisplayName("User made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000")
    void test10() {

    }
}

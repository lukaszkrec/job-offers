package org.joboffer.domain.client.error;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.assertj.core.api.Assertions;
import org.joboffer.domain.OfferFetcherRestTemplateConfigurationIntegrationTest;
import org.joboffer.domain.SampleJobOfferResponse;
import org.joboffer.domain.offer.OfferFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.joboffer.domain.BaseIntegrationTest.WIRE_MOCK_HOST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OfferFetcherRestTemplateErrorsIntegrationTest implements SampleJobOfferResponse {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private final HttpStatus INTERVAL_SERVER_ERROR_500 = HttpStatus.INTERNAL_SERVER_ERROR;
    private final HttpStatus NO_CONTENT_204 = HttpStatus.NO_CONTENT;
    private final HttpStatus NOT_FOUND_404 = HttpStatus.NOT_FOUND;
    private final HttpStatus UNAUTHORIZED_401 = HttpStatus.UNAUTHORIZED;


    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    OfferFetcher offerFetcher = new OfferFetcherRestTemplateConfigurationIntegrationTest()
            .remoteOfferFetcherClientTest(
                    1000,
                    1000,
                    WIRE_MOCK_HOST,
                    wireMockServer.getPort()
            );

    @Test
    void should_throw_exception_INTERVAL_SERVER_ERROR_500_when_http_service_returning_CONNECTION_RESET_BY_PEER() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        //when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> offerFetcher.fetchAllOffers());

        //then
        assertAll(
                () -> Assertions.assertThat(exception.getReason()).isEqualTo("Error while using http client"),
                () -> Assertions.assertThat(exception.getStatus()).isEqualTo(INTERVAL_SERVER_ERROR_500)
        );
    }

    @Test
    void should_throw_exception_NOT_FOUND_404_when_http_service_returning_not_found_status() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)));

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> offerFetcher.fetchAllOffers());

        // then
        assertAll(
                () -> Assertions.assertThat(exception.getStatus()).isEqualTo(NOT_FOUND_404)
        );
    }


    @Test
    void should_throw_exception_INTERVAL_SERVER_ERROR_500_when_fault_empty_response() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(wiremock.org.apache.hc.core5.http.HttpStatus.SC_OK)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                        .withFault(Fault.EMPTY_RESPONSE)));

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> offerFetcher.fetchAllOffers());

        // then
        assertAll(
                () -> Assertions.assertThat(exception.getStatus()).isEqualTo(INTERVAL_SERVER_ERROR_500)
        );
    }

    @Test
    void should_throw_exception_INTERVAL_SERVER_ERROR_500_fault_malformed_response_chunk() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> offerFetcher.fetchAllOffers());

        // then
        assertAll(
                () -> Assertions.assertThat(exception.getStatus()).isEqualTo(INTERVAL_SERVER_ERROR_500)
        );
    }

    @Test
    void should_throw_exception_INTERVAL_SERVER_ERROR_500_when_fault_random_data_then_close() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> offerFetcher.fetchAllOffers());


        // then
        assertAll(
                () -> Assertions.assertThat(exception.getStatus()).isEqualTo(INTERVAL_SERVER_ERROR_500)
        );
    }

    @Test
    void should_throw_NO_CONTENT_204_when_status_is_204_no_content() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                        .withBody(bodyWithFourOffersJson())));

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> offerFetcher.fetchAllOffers());

        // then
        assertAll(
                () -> Assertions.assertThat(exception.getStatus()).isEqualTo(NO_CONTENT_204)
        );
    }

    @Test
    void should_throw_exception_INTERVAL_SERVER_ERROR_500_when_response_delay_is_5000_ms_and_client_has_1000ms_read_timeout() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                        .withBody(bodyWithFourOffersJson())
                        .withFixedDelay(5000)));

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> offerFetcher.fetchAllOffers());

        // then
        assertAll(
                () -> Assertions.assertThat(exception.getStatus()).isEqualTo(INTERVAL_SERVER_ERROR_500)
        );
    }

    @Test
    void should_throw_exception_UNAUTHORIZED_401_when_http_service_returning_unauthorized_status() {
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON)
                        .withStatus(HttpStatus.UNAUTHORIZED.value()))
        );

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> offerFetcher.fetchAllOffers());

        // then
        assertAll(
                () -> Assertions.assertThat(exception.getStatus()).isEqualTo(UNAUTHORIZED_401)
        );
    }
}

package org.joboffer.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.joboffer.BaseIntegrationTest;
import org.joboffer.SampleJobOfferResponse;
import org.joboffer.domain.offer.OfferFacade;
import org.joboffer.domain.offer.OfferNotFoundException;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
    void should_return_empty_list_of_offers_when_user_want_to_get_all_offers_and_external_server_doesnt_have_any_offers() throws Exception {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));

        List<OfferDto> offersFromExternalServer = fetcherScheduler.scheduleOfferFetcher();
        assertAll(
                () -> Assertions.assertThat(offersFromExternalServer).isEmpty(),
                () -> Assertions.assertThat(offerRepository.findAll()).isEmpty()
        );

        //when
        MvcResult mvcResult = mvc.perform(get("/offers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseWithNoOffers = mvcResult.getResponse().getContentAsString();
        List<OfferDto> offerResponse = objectMapper.readValue(responseWithNoOffers, new TypeReference<>() {
        });

        //then
        assertAll(
                () -> Assertions.assertThat(offerResponse).isEmpty(),
                () -> Assertions.assertThat(offerRepository.findAll()).isEmpty(),
                () -> Mockito.verify(offerFacade, times(1)).fetchAllOffersAndSaveAllIfNotExist()
        );
    }

    @Test
    @DisplayName("User made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”")
    void should_return_NOT_FOUND_404_when_offer_with_provided_id_does_not_exist() throws Exception {
        //given , when
        final String searchedOfferId = "9999";
        mvc.perform(get("/offers/{offerId}", searchedOfferId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          statusCode: "404 NOT_FOUND",
                          timestamp: "2023-05-01T17:33:47.877+02:00",
                          message: "Offer with id: 9999 does not exist",
                          description: "uri=/offers/9999"
                        }
                                                """));

        //then
        assertAll(
                () -> assertThrows(OfferNotFoundException.class, () -> offerFacade.findOfferById(searchedOfferId), "Offer with id: " + searchedOfferId + " does not exist"),
                () -> Assertions.assertThat(offerRepository.findOfferById(searchedOfferId)).isNotPresent(),
                // second times call cause of asserThrows
                () -> Mockito.verify(offerFacade, times(2)).findOfferById(searchedOfferId)
        );
    }

    @Test
    @DisplayName("There are 2 new offers in external HTTP server")
    void should_fetch_two_offers_when_there_are_two_offers_in_external_http_server() throws Exception {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithTwoOffersJson())));

        List<OfferDto> offersFromExternalServer = fetcherScheduler.scheduleOfferFetcher();
        Assertions.assertThat(offersFromExternalServer).hasSize(2);

        //when
        MvcResult mvcResult = mvc.perform(get("/offers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseWithNoOffers = mvcResult.getResponse().getContentAsString();
        List<OfferDto> offerResponse = objectMapper.readValue(responseWithNoOffers, new TypeReference<>() {
        });

        //then
        assertAll(
                () -> Assertions.assertThat(offerResponse).hasSize(2),
                () -> Assertions.assertThat(offerRepository.findAll()).hasSize(2),
                () -> Mockito.verify(offerFacade, times(1)).fetchAllOffersAndSaveAllIfNotExist()
        );
    }

    @Test
    @DisplayName("User made GET /offers/1000 and system returned OK(200) with offer")
    void should_return_an_offer_found_by_id_when_offer_exist_with_provided_id() throws Exception {
        //given
        final String searchedOfferId = "1000";
        OfferDto offerDto = new OfferDto(searchedOfferId, "Junior Java Developer", "BlueSoft Sp. z o.o.", "7 000 – 9 000 PLN", "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre");
        OfferDto registeredOffer = offerFacade.register(offerDto);
        assertAll(
                () -> Assertions.assertThat(registeredOffer.getId()).isEqualTo(searchedOfferId),
                () -> Assertions.assertThat(registeredOffer.getTitle()).isEqualTo("Junior Java Developer"),
                () -> Assertions.assertThat(registeredOffer.getCompany()).isEqualTo("BlueSoft Sp. z o.o."),
                () -> Assertions.assertThat(registeredOffer.getSalary()).isEqualTo("7 000 – 9 000 PLN"),
                () -> Assertions.assertThat(registeredOffer.getOfferUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"),
                () -> Assertions.assertThat(offerRepository.findAll()).hasSize(1)
        );

        //when
        mvc.perform(get("/offers/{offerId}", searchedOfferId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          "id": "1000",
                          "title": "Junior Java Developer",
                          "company": "BlueSoft Sp. z o.o.",
                          "salary": "7 000 – 9 000 PLN",
                          "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"
                        }"""));

        //then
        assertAll(
                () -> Assertions.assertThat(offerRepository.findOfferById(searchedOfferId)).isPresent(),
                () -> Mockito.verify(offerFacade, times(1)).findOfferById(searchedOfferId)
        );
    }

    @Test
    @DisplayName("Scheduler run and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database")
    void should_save_two_offers_into_database_with_sequential_id_when_there_are_two_offers_in_external_server() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithTwoOffersJson())));

        //when
        List<OfferDto> offersFromExternalServerResponse = fetcherScheduler.scheduleOfferFetcher();

        //then
        OfferDto offerDto1 = offersFromExternalServerResponse.get(0);
        OfferDto offerDto2 = offersFromExternalServerResponse.get(1);
        assertAll(
                () -> Assertions.assertThat(offerDto1.getId()).isEqualTo("1000"),
                () -> Assertions.assertThat(offerDto2.getId()).isEqualTo("2000"),
                () -> Assertions.assertThat(offersFromExternalServerResponse).hasSize(2),
                () -> Assertions.assertThat(offerRepository.findAll()).hasSize(2),
                () -> Mockito.verify(offerFacade, times(1)).fetchAllOffersAndSaveAllIfNotExist()
        );
    }

    @Test
    @DisplayName("Scheduler run and made GET to external server and system added 4 new offers with ids: 1000, 2000, 3000 and 4000 to database")
    void should_save_four_offers_into_database_with_sequential_id_when_there_are_four_offers_in_external_server() {
        //given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithFourOffersJson())));

        //when, then
        List<OfferDto> offersFromExternalServerResponse = fetcherScheduler.scheduleOfferFetcher();
        OfferDto offerDto1 = offersFromExternalServerResponse.get(0);
        OfferDto offerDto2 = offersFromExternalServerResponse.get(1);
        OfferDto offerDto3 = offersFromExternalServerResponse.get(2);
        OfferDto offerDto4 = offersFromExternalServerResponse.get(3);

        assertAll(
                () -> Assertions.assertThat(offerDto1.getId()).isEqualTo("1000"),
                () -> Assertions.assertThat(offerDto2.getId()).isEqualTo("2000"),
                () -> Assertions.assertThat(offerDto3.getId()).isEqualTo("3000"),
                () -> Assertions.assertThat(offerDto4.getId()).isEqualTo("4000"),
                () -> Assertions.assertThat(offersFromExternalServerResponse).hasSize(4),
                () -> Assertions.assertThat(offerRepository.findAll()).hasSize(4),
                () -> Mockito.verify(offerFacade, times(1)).fetchAllOffersAndSaveAllIfNotExist()
        );
    }

    @Test
    @DisplayName("User made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer")
    void should_create_an_offer_when_provided_offer_parameters_are_correct() throws Exception {
        //given
        OfferDto offerDto = new OfferDto("1000", "Junior Java Developer", "BlueSoft Sp. z o.o.", "7 000 – 9 000 PLN", "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre");
        //when
        mvc.perform(post("/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          "id": "1000",
                          "title": "Junior Java Developer",
                          "company": "BlueSoft Sp. z o.o.",
                          "salary": "7 000 – 9 000 PLN",
                          "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"
                        }"""));

        assertAll(
                () -> Mockito.verify(offerFacade, times(1)).register(offerDto)
        );
    }

    @Test
    @DisplayName("User made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer")
    void should_return_list_of_one_offer_from_database_when_one_offer_exist_in_database() throws Exception {
        //given
        OfferDto offerDto = new OfferDto("1000", "Junior Java Developer", "BlueSoft Sp. z o.o.", "7 000 – 9 000 PLN", "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre");
        OfferDto registeredOffer = offerFacade.register(offerDto);
        assertAll(
                () -> Assertions.assertThat(registeredOffer.getId()).isEqualTo("1000"),
                () -> Assertions.assertThat(registeredOffer.getTitle()).isEqualTo("Junior Java Developer"),
                () -> Assertions.assertThat(registeredOffer.getCompany()).isEqualTo("BlueSoft Sp. z o.o."),
                () -> Assertions.assertThat(registeredOffer.getSalary()).isEqualTo("7 000 – 9 000 PLN"),
                () -> Assertions.assertThat(registeredOffer.getOfferUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"),
                () -> Assertions.assertThat(offerRepository.findAll()).hasSize(1)
        );

        mvc.perform(get("/offers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("Junior Java Developer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].company").value("BlueSoft Sp. z o.o."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].salary").value("7 000 – 9 000 PLN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].offerUrl").value("https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"));

        assertAll(
                () -> Mockito.verify(offerFacade, times(1)).findAllOffers()
        );
    }

    @Test
    @DisplayName("User made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000")
    void should_return_list_of_two_offers_from_database_when_two_offers_exist_in_database() throws Exception {
        //given
        OfferDto offerDto1 = new OfferDto("1000", "Junior Java Developer", "BlueSoft Sp. z o.o.", "7 000 – 9 000 PLN", "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre");
        OfferDto offerDto2 = new OfferDto("2000", "Java (CMS) Developer", "Efigence SA", "16 000 – 18 000 PLN", "https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh");
        OfferDto registeredOffer1 = offerFacade.register(offerDto1);
        OfferDto registeredOffer2 = offerFacade.register(offerDto2);
        assertAll(
                () -> Assertions.assertThat(registeredOffer1.getId()).isEqualTo("1000"),
                () -> Assertions.assertThat(registeredOffer1.getTitle()).isEqualTo("Junior Java Developer"),
                () -> Assertions.assertThat(registeredOffer1.getCompany()).isEqualTo("BlueSoft Sp. z o.o."),
                () -> Assertions.assertThat(registeredOffer1.getSalary()).isEqualTo("7 000 – 9 000 PLN"),
                () -> Assertions.assertThat(registeredOffer1.getOfferUrl()).isEqualTo("https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"),
                () -> Assertions.assertThat(registeredOffer2.getId()).isEqualTo("2000"),
                () -> Assertions.assertThat(registeredOffer2.getTitle()).isEqualTo("Java (CMS) Developer"),
                () -> Assertions.assertThat(registeredOffer2.getCompany()).isEqualTo("Efigence SA"),
                () -> Assertions.assertThat(registeredOffer2.getSalary()).isEqualTo("16 000 – 18 000 PLN"),
                () -> Assertions.assertThat(registeredOffer2.getOfferUrl()).isEqualTo("https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh"),
                () -> Assertions.assertThat(offerRepository.findAll()).hasSize(2)
        );

        mvc.perform(get("/offers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("Junior Java Developer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].company").value("BlueSoft Sp. z o.o."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].salary").value("7 000 – 9 000 PLN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].offerUrl").value("https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value("2000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value("Java (CMS) Developer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].company").value("Efigence SA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].salary").value("16 000 – 18 000 PLN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].offerUrl").value("https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh"));

        assertAll(
                () -> Mockito.verify(offerFacade, times(1)).findAllOffers()
        );
    }
}

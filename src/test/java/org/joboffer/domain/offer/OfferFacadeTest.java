package org.joboffer.domain.offer;

import org.joboffer.domain.offer.dto.OfferDto;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;
import static org.joboffer.domain.offer.OfferMapper.mapToOfferDto;
import static org.junit.jupiter.api.Assertions.assertAll;

class OfferFacadeTest {


    private final OfferRepository repository = new OfferRepositoryTestImpl();
    private final OfferValidation validation = new OfferValidation(repository);
    private final OfferFetcher fetcher = new OfferFetcherTestImpl();
    private final OfferFacade offerFacade = new OfferFacade(repository, validation, fetcher);

    @Test
    void should_save_two_offers_when_there_are_no_offers_in_database() {
        //given
        Offer offer1 = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        Offer offer2 = new Offer("2L", "https://www.example1.com", "HR", "Amazong", "55555");
        OfferDto offerDto1 = mapToOfferDto(offer1);
        OfferDto offerDto2 = mapToOfferDto(offer2);

        //when
        offerFacade.register(offerDto1);
        offerFacade.register(offerDto2);
        List<OfferDto> offersDatabaseAfterRegistration = offerFacade.findAllOffers();

        //then
        assertThat(offersDatabaseAfterRegistration).hasSize(2).containsExactlyInAnyOrder(offerDto1, offerDto2);
    }

    @Test
    void should_throw_exception_when_repository_already_had_offers_with_the_same_url() {
        //given
        Offer offer1 = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        Offer offer2 = new Offer("2L", "https://www.example1.com", "HR", "Amazong", "55555");
        Offer offer3 = new Offer("3L", "https://www.example1.com", "Scrum Master", "GLob", "659832");
        OfferDto offerDto1 = mapToOfferDto(offer1);
        OfferDto offerDto2 = mapToOfferDto(offer2);
        OfferDto offerDto3 = mapToOfferDto(offer3);
        offerFacade.register(offerDto1);
        offerFacade.register(offerDto2);

        //when
        //then
        assertAll(
                () -> assertThatRuntimeException()
                        .isThrownBy(() -> offerFacade.register(offerDto3))
                        .withMessage("Offer with the same url: " + offerDto3.getUrl() + " already exists"),
                () -> assertThat(offerFacade.findAllOffers()).hasSize(2)
                        .containsExactlyInAnyOrder(offerDto1, offerDto2)
        );
    }

    @Test
    void should_return_an_offer_if_offer_with_the_given_id_exist() {
        //given
        String searchedOfferId = "1L";
        Offer offer = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        OfferDto offerDto = mapToOfferDto(offer);
        offerFacade.register(offerDto);

        //when
        OfferDto offerById = offerFacade.findOfferById(searchedOfferId);

        //then
        assertAll(
                () -> assertThat(offerById).usingRecursiveComparison().isEqualTo(offer),
                () -> assertThat(offerFacade.findAllOffers()).hasSize(1).containsExactlyInAnyOrder(offerDto)
        );
    }

    @Test
    void should_throw_an_exception_when_offer_with_the_given_id_does_not_exist() {
        //given
        String searchedOfferId = "2L";
        Offer offer = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        OfferDto offerDto = mapToOfferDto(offer);
        offerFacade.register(offerDto);

        //when
        //then
        assertThatRuntimeException()
                .isThrownBy(() -> offerFacade.findOfferById(searchedOfferId))
                .withMessage("Offer with id: " + searchedOfferId + " does not exist");
    }

    @Test
    void should_save_offer_when_given_params_are_correct() {
        //given
        Offer offer = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        OfferDto offerDto = mapToOfferDto(offer);

        //when
        offerFacade.register(offerDto);
        List<OfferDto> offersDatabaseAfterRegistration = offerFacade.findAllOffers();

        //then
        assertAll(
                () -> assertThat(offersDatabaseAfterRegistration).contains(offerDto),
                () -> assertThat(offersDatabaseAfterRegistration).hasSize(1).containsExactly(offerDto),
                () -> assertThat(offerDto.getId()).isNotEmpty().isEqualTo("1L"),
                () -> assertThat(offerDto.getUrl()).isNotEmpty().isEqualTo("https://www.example.com"),
                () -> assertThat(offerDto.getWorkSite()).isNotEmpty().isEqualTo("Developer"),
                () -> assertThat(offerDto.getCompany()).isNotEmpty().isEqualTo("Apfel"),
                () -> assertThat(offerDto.getSalary()).isNotEmpty().isEqualTo("10000")
        );
    }

    @Test
    void should_throw_an_exception_when_saved_offer_has_null_id() {
        //given
        Offer offer = new Offer(null, "https://www.example.com", "Developer", "Apfel", "10000");
        OfferDto offerDto = mapToOfferDto(offer);

        //when
        //then
        assertAll(
                () -> assertThatRuntimeException()
                        .isThrownBy(() -> offerFacade.register(offerDto))
                        .withMessage("Offer id can not be: " + null),
                () -> assertThat(offerDto.getId()).isNull()
        );

    }

    @Test
    void should_throw_an_exception_when_saved_offer_has_same_id_as_existing_offer() {
        //given
        Offer offer1 = new Offer("1L", "https://www.example1.com", "Developer", "Apfel", "10000");
        Offer offer2 = new Offer("1L", "https://www.example2.com", "Developer", "Apfel", "10000");
        OfferDto offerDto1 = mapToOfferDto(offer1);
        OfferDto offerDto2 = mapToOfferDto(offer2);

        //when
        //then
        assertAll(
                () -> assertThatRuntimeException()
                        .isThrownBy(() -> {
                            offerFacade.register(offerDto1);
                            offerFacade.register(offerDto2);
                        }).withMessage("Offer with id: " + offer2.getId() + " already exists"),
                () -> assertThat(offerFacade.findAllOffers()).hasSize(1).containsExactlyInAnyOrder(offerDto1)
        );
    }

    @Test
    void should_fetch_all_offers_and_save_all_when_offers_are_not_in_database() {
        //given
        List<OfferDto> databaseBeforeFetching = offerFacade.findAllOffers();

        //when
        List<Offer> fetchedOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        List<OfferDto> databaseAfterFetching = offerFacade.findAllOffers();

        //then
        assertAll(
                () -> assertThat(databaseBeforeFetching).isEmpty(),
                () -> assertThat(fetchedOffers).hasSize(7).containsExactlyInAnyOrderElementsOf(
                        Stream.of(
                                new Offer("1L", "http://www.example.com1", "Scrum Master", "Amazon", "101010"),
                                new Offer("2L", "http://www.example.com2", "PO", "Apel", "4141251"),
                                new Offer("3L", "http://www.example.com3", "QA", "Gogiel", "5135624734"),
                                new Offer("4L", "http://www.example.com4", "PY Dev", "Media Expert", "5145141"),
                                new Offer("5L", "http://www.example.com5", "C++ Dev", "Comarch", "5312514"),
                                new Offer("6L", "http://www.example.com6", "C# Dev", "Sii", "5235124"),
                                new Offer("7L", "http://www.example.com7", "Scala Dev", "GlobalLogic", "4141354135")
                        ).toList()
                ),
                () -> assertThat(databaseAfterFetching).hasSize(7).containsExactlyInAnyOrderElementsOf(
                        Stream.of(
                                        new Offer("1L", "http://www.example.com1", "Scrum Master", "Amazon", "101010"),
                                        new Offer("2L", "http://www.example.com2", "PO", "Apel", "4141251"),
                                        new Offer("3L", "http://www.example.com3", "QA", "Gogiel", "5135624734"),
                                        new Offer("4L", "http://www.example.com4", "PY Dev", "Media Expert", "5145141"),
                                        new Offer("5L", "http://www.example.com5", "C++ Dev", "Comarch", "5312514"),
                                        new Offer("6L", "http://www.example.com6", "C# Dev", "Sii", "5235124"),
                                        new Offer("7L", "http://www.example.com7", "Scala Dev", "GlobalLogic", "4141354135")
                                )
                                .map(OfferMapper::mapToOfferDto)
                                .toList()
                )
        );
    }

    @Test
    void should_fetch_and_save_only_two_offers_when_rest_of_offers_are_the_same_as_in_database() {
        //given
        inMemoryDatabaseConfiguration();
        List<OfferDto> offersDatabaseBeforeFetching = offerFacade.findAllOffers();

        //when
        List<Offer> fetchedOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        List<OfferDto> offersDatabaseAfterFetching = offerFacade.findAllOffers();

        //then
        assertAll(
                () -> assertThat(offersDatabaseBeforeFetching).hasSize(5).containsExactlyInAnyOrderElementsOf(
                        Stream.of(
                                        new Offer("1L", "http://www.example.com1", "Scrum Master", "Amazon", "101010"),
                                        new Offer("2L", "http://www.example.com2", "PO", "Apel", "4141251"),
                                        new Offer("3L", "http://www.example.com3", "QA", "Gogiel", "5135624734"),
                                        new Offer("4L", "http://www.example.com4", "PY Dev", "Media Expert", "5145141"),
                                        new Offer("5L", "http://www.example.com5", "C++ Dev", "Comarch", "5312514")
                                )
                                .map(OfferMapper::mapToOfferDto)
                                .toList()
                ),
                () -> assertThat(fetchedOffers).hasSize(2).containsExactlyInAnyOrder(
                        new Offer("6L", "http://www.example.com6", "C# Dev", "Sii", "5235124"),
                        new Offer("7L", "http://www.example.com7", "Scala Dev", "GlobalLogic", "4141354135")
                ),
                () -> assertThat(offersDatabaseAfterFetching).hasSize(7).containsExactlyInAnyOrderElementsOf(
                        Stream.of(
                                        new Offer("1L", "http://www.example.com1", "Scrum Master", "Amazon", "101010"),
                                        new Offer("2L", "http://www.example.com2", "PO", "Apel", "4141251"),
                                        new Offer("3L", "http://www.example.com3", "QA", "Gogiel", "5135624734"),
                                        new Offer("4L", "http://www.example.com4", "PY Dev", "Media Expert", "5145141"),
                                        new Offer("5L", "http://www.example.com5", "C++ Dev", "Comarch", "5312514"),
                                        new Offer("6L", "http://www.example.com6", "C# Dev", "Sii", "5235124"),
                                        new Offer("7L", "http://www.example.com7", "Scala Dev", "GlobalLogic", "4141354135")
                                )
                                .map(OfferMapper::mapToOfferDto)
                                .toList()
                )
        );
    }


    private List<Offer> inMemoryDatabaseConfiguration() {
        List<Offer> inMemoryTestData = List.of(
                new Offer("1L", "http://www.example.com1", "Scrum Master", "Amazon", "101010"),
                new Offer("2L", "http://www.example.com2", "PO", "Apel", "4141251"),
                new Offer("3L", "http://www.example.com3", "QA", "Gogiel", "5135624734"),
                new Offer("4L", "http://www.example.com4", "PY Dev", "Media Expert", "5145141"),
                new Offer("5L", "http://www.example.com5", "C++ Dev", "Comarch", "5312514")
        );
        return inMemoryTestData.stream()
                .map(OfferMapper::mapToOfferDto)
                .map(offerFacade::register)
                .toList();
    }
}
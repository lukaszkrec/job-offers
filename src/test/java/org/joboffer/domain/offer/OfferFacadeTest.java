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
        Offer offer1 = new Offer("1L", "Scrum Master", "Amazon", "353651235", "http://www.example.com1");
        Offer offer2 = new Offer("2L", "PO", "Apel", "1010431510", "http://www.example.com2");

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
        Offer offer1 = new Offer("1L", "Scrum Master", "Amazon", "353651235", "http://www.example.com1");
        Offer offer2 = new Offer("2L", "PO", "Apel", "1010431510", "http://www.example.com2");
        Offer offer3 = new Offer("3L", "QA", "Gogiel", "5613251", "http://www.example.com2");
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
                        .withMessage("Offer with the same url: " + offerDto3.offerUrl() + " already exists"),
                () -> assertThat(offerFacade.findAllOffers()).hasSize(2)
                        .containsExactlyInAnyOrder(offerDto1, offerDto2)
        );
    }

    @Test
    void should_return_an_offer_if_offer_with_the_given_id_exist() {
        //given
        String searchedOfferId = "1L";
        Offer offer = new Offer("1L", "Scrum Master", "Amazon", "353651235", "http://www.example.com1");
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
        Offer offer = new Offer("1L", "Scrum Master", "Amazon", "353651235", "http://www.example.com1");
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
        Offer offer = new Offer("1L", "Scrum Master", "Amazon", "353651235", "http://www.example.com");
        OfferDto offerDto = mapToOfferDto(offer);

        //when
        offerFacade.register(offerDto);
        List<OfferDto> offersDatabaseAfterRegistration = offerFacade.findAllOffers();

        //then
        assertAll(
                () -> assertThat(offersDatabaseAfterRegistration).hasSize(1).containsExactly(offerDto),
                () -> assertThat(offerDto.id()).isEqualTo("1L"),
                () -> assertThat(offerDto.offerUrl()).isEqualTo("http://www.example.com"),
                () -> assertThat(offerDto.title()).isEqualTo("Scrum Master"),
                () -> assertThat(offerDto.company()).isEqualTo("Amazon"),
                () -> assertThat(offerDto.salary()).isEqualTo("353651235")
        );
    }

    @Test
    void should_throw_an_exception_when_saved_offer_has_null_id() {
        //given
        Offer offer = new Offer(null, "Scrum Master", "Amazon", "353651235", "http://www.example.com1");
        OfferDto offerDto = mapToOfferDto(offer);

        //when
        //then
        assertAll(
                () -> assertThatRuntimeException()
                        .isThrownBy(() -> offerFacade.register(offerDto))
                        .withMessage("Offer id can not be: " + null),
                () -> assertThat(offerDto.id()).isNull()
        );

    }

    @Test
    void should_throw_an_exception_when_saved_offer_has_same_id_as_existing_offer() {
        //given
        OfferDto offer1 = new OfferDto("1L", "Scrum Master", "Amazon", "353651235", "http://www.example.com1");
        OfferDto offer2 = new OfferDto("1L", "PO", "Apel", "1010431510", "http://www.example.com2");

        //when
        //then
        assertAll(
                () -> assertThatRuntimeException()
                        .isThrownBy(() -> {
                            offerFacade.register(offer1);
                            offerFacade.register(offer2);
                        }).withMessage("Offer with id: " + offer2.id() + " already exists"),
                () -> assertThat(offerFacade.findAllOffers()).hasSize(1).containsExactlyInAnyOrder(offer1)
        );
    }

    @Test
    void should_fetch_all_offers_and_save_all_when_offers_are_not_in_database() {
        //given
        List<OfferDto> databaseBeforeFetching = offerFacade.findAllOffers();

        //when
        List<OfferDto> fetchedOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        List<OfferDto> databaseAfterFetching = offerFacade.findAllOffers();

        //then
        assertAll(
                () -> assertThat(databaseBeforeFetching).isEmpty(),
                () -> assertThat(fetchedOffers).hasSize(7).containsExactlyInAnyOrderElementsOf(
                        Stream.of(
                                        new Offer("Scrum Master", "Amazon", "353651235", "http://www.example.com1"),
                                        new Offer("PO", "Apel", "1010431510", "http://www.example.com2"),
                                        new Offer("QA", "Gogiel", "5613251", "http://www.example.com3"),
                                        new Offer("PY Dev", "Media Expert", "634673", "http://www.example.com4"),
                                        new Offer("C++ Dev", "Comarch", "321231", "http://www.example.com5"),
                                        new Offer("C# Dev", "Sii", "64363", "http://www.example.com6"),
                                        new Offer("Scala Dev", "GlobalLogic", "623622342", "http://www.example.com7")
                                ).map(OfferMapper::mapToOfferDto)
                                .toList()
                ),
                () -> assertThat(databaseAfterFetching).hasSize(7).containsExactlyInAnyOrderElementsOf(
                        Stream.of(
                                        new Offer("Scrum Master", "Amazon", "353651235", "http://www.example.com1"),
                                        new Offer("PO", "Apel", "1010431510", "http://www.example.com2"),
                                        new Offer("QA", "Gogiel", "5613251", "http://www.example.com3"),
                                        new Offer("PY Dev", "Media Expert", "634673", "http://www.example.com4"),
                                        new Offer("C++ Dev", "Comarch", "321231", "http://www.example.com5"),
                                        new Offer("C# Dev", "Sii", "64363", "http://www.example.com6"),
                                        new Offer("Scala Dev", "GlobalLogic", "623622342", "http://www.example.com7")
                                )
                                .map(OfferMapper::mapToOfferDto)
                                .toList()
                )
        );
    }

    @Test
    void should_fetch_and_save_only_two_offers_when_rest_of_offers_are_the_same_as_in_database() {
        //given
        //when
        inMemoryDatabaseConfiguration();
        List<OfferDto> offersDatabaseBeforeFetching = offerFacade.findAllOffers();
        List<OfferDto> fetchedOffers = offerFacade.fetchAllOffersAndSaveAllIfNotExist();
        List<OfferDto> offersDatabaseAfterFetching = offerFacade.findAllOffers();

        //then
        assertAll(
                () -> assertThat(offersDatabaseBeforeFetching).hasSize(5)
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactlyInAnyOrderElementsOf(
                                Stream.of(
                                                new Offer("Scrum Master", "Amazon", "353651235", "http://www.example.com1"),
                                                new Offer("PO", "Apel", "1010431510", "http://www.example.com2"),
                                                new Offer("QA", "Gogiel", "5613251", "http://www.example.com3"),
                                                new Offer("PY Dev", "Media Expert", "634673", "http://www.example.com4"),
                                                new Offer("C++ Dev", "Comarch", "321231", "http://www.example.com5")
                                        )
                                        .map(OfferMapper::mapToOfferDto)
                                        .toList()
                        ),
                () -> assertThat(fetchedOffers).hasSize(2)
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactlyInAnyOrder(
                                mapToOfferDto(new Offer("C# Dev", "Sii", "64363", "http://www.example.com6")),
                                mapToOfferDto(new Offer("Scala Dev", "GlobalLogic", "623622342", "http://www.example.com7"))
                        ),
                () -> assertThat(offersDatabaseAfterFetching).hasSize(7)
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactlyInAnyOrderElementsOf(
                                Stream.of(
                                                new Offer("Scrum Master", "Amazon", "353651235", "http://www.example.com1"),
                                                new Offer("PO", "Apel", "1010431510", "http://www.example.com2"),
                                                new Offer("QA", "Gogiel", "5613251", "http://www.example.com3"),
                                                new Offer("PY Dev", "Media Expert", "634673", "http://www.example.com4"),
                                                new Offer("C++ Dev", "Comarch", "321231", "http://www.example.com5"),
                                                new Offer("C# Dev", "Sii", "64363", "http://www.example.com6"),
                                                new Offer("Scala Dev", "GlobalLogic", "623622342", "http://www.example.com7"))
                                        .map(OfferMapper::mapToOfferDto)
                                        .toList()
                        )
        );
    }


    private List<OfferDto> inMemoryDatabaseConfiguration() {
        List<OfferDto> inMemoryTestData = List.of(
                new OfferDto("1L", "Scrum Master", "Amazon", "353651235", "http://www.example.com1"),
                new OfferDto("2L", "PO", "Apel", "1010431510", "http://www.example.com2"),
                new OfferDto("3L", "QA", "Gogiel", "5613251", "http://www.example.com3"),
                new OfferDto("4L", "PY Dev", "Media Expert", "634673", "http://www.example.com4"),
                new OfferDto("5L", "C++ Dev", "Comarch", "321231", "http://www.example.com5")
        );
        return inMemoryTestData.stream()
                .map(offerFacade::register)
                .toList();
    }
}
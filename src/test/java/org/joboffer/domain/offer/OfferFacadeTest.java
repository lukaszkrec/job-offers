package org.joboffer.domain.offer;

import org.joboffer.domain.offer.dto.OfferDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.joboffer.domain.offer.OfferMapper.mapToOfferDto;

class OfferFacadeTest {

    private final OfferRepository repository = new OfferRepositoryTestImpl();
    private final OfferValidation validation = new OfferValidation(repository);
    private final OfferFacade offerFacade = new OfferFacade(repository, validation);

    @Test
    void should_return_list_of_offers_containing_two_offers() {
        //given
        Offer offer1 = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        Offer offer2 = new Offer("2L", "https://www.example1.com", "HR", "Amazong", "55555");
        offerFacade.save(offer1);
        offerFacade.save(offer2);

        //when
        OfferDto offerDto1 = mapToOfferDto(offer1);
        OfferDto offerDto2 = mapToOfferDto(offer2);
        List<OfferDto> allOffers = offerFacade.findAllOffers();

        //then
        assertThat(allOffers).hasSize(2).contains(offerDto1, offerDto2);

    }

    @Test
    void should_return_an_offer_if_an_offer_with_the_given_id_exist() {
        //given
        String searchedOfferId = "1L";
        Offer offer1 = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        offerFacade.save(offer1);

        //when
        OfferDto offerById = offerFacade.findOfferById(searchedOfferId);

        //then
        assertThat(offerById).usingRecursiveComparison().isEqualTo(offer1);

    }

    @Test
    void should_throw_an_exception_when_offer_with_the_given_id_does_not_exist() {
        //given
        String searchedOfferId = "2L";
        Offer offer1 = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        offerFacade.save(offer1);

        //when
        //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> offerFacade.findOfferById(searchedOfferId))
                .withMessage("Offer with id " + searchedOfferId + " does not exist");

    }

    @Test
    void should_save_offer_when_given_params_are_correct() {
        //given
        Offer offer = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");

        //when
        OfferDto savedOffer = offerFacade.save(offer);
        List<OfferDto> allOffers = offerFacade.findAllOffers();
        //then
        assertThat(allOffers).contains(savedOffer);
    }

    @Test
    void should_throw_an_exception_when_saved_offer_has_null_id() {
        //given
        Offer offer = new Offer(null, "https://www.example.com", "Developer", "Apfel", "10000");

        //when
        //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> offerFacade.save(offer))
                .withMessage("Offer must have an id");
    }

    @Test
    void should_throw_an_exception_when_saved_offer_has_same_id_as_existing_offer() {
        //given
        Offer offer1 = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");
        Offer offer2 = new Offer("1L", "https://www.example.com", "Developer", "Apfel", "10000");

        //when
        //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    offerFacade.save(offer1);
                    offerFacade.save(offer2);
                }).withMessage("Offer with id " + offer2.getId() + " already exists");
    }

    @Test
    void should_fetch_all_offers_and_save_all_if_not_exist() {
        //given
        Offer offer1 = new Offer("1L", "https://www.example1.com", "Developer", "Apfel", "10000");
        Offer offer2 = new Offer("2L", "https://www.example2.com", "HR", "Gogiel", "5000");

        offerFacade.save(offer1);
        offerFacade.save(offer2);

        //when
        Offer duplicateOffer = new Offer("2L", "https://www.example2.com", "HR", "Gogiel", "5000");
        Offer offer4 = new Offer("3L", "https://www.example3.com", "Architect", "Amazong", "15000");

        OfferDto duplicateOfferDto = mapToOfferDto(duplicateOffer);
        OfferDto offerDto4 = mapToOfferDto(offer4);

        List<OfferDto> offersToFetch = List.of(duplicateOfferDto, offerDto4);
        offerFacade.fetchAllOffersAndSaveAllIfNotExist(offersToFetch);
        List<OfferDto> offers = offerFacade.findAllOffers();

        //then
        assertThat(offers).hasSize(3);

    }
}
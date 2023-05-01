package org.joboffer.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.OfferFacade;
import org.joboffer.domain.offer.dto.OfferDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
@AllArgsConstructor
class OffersController {

    private final OfferFacade offerFacade;

    @GetMapping
    public ResponseEntity<List<OfferDto>> getOffers() {
        List<OfferDto> allOffers = offerFacade.findAllOffers();
        return ResponseEntity.ok(allOffers);
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable String offerId) {
        OfferDto offerById = offerFacade.findOfferById(offerId);
        return ResponseEntity.ok(offerById);
    }

    @PostMapping
    public ResponseEntity<OfferDto> addOffers(@RequestBody OfferDto offer) {
        OfferDto registeredOffer = offerFacade.register(offer);
        return ResponseEntity.ok(registeredOffer);
    }
}
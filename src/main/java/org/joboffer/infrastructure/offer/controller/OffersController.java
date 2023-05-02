package org.joboffer.infrastructure.offer.controller;

import lombok.AllArgsConstructor;
import org.joboffer.domain.offer.OfferFacade;
import org.joboffer.domain.offer.dto.OfferDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping(value = "/{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferDto> getOfferById(@PathVariable String offerId) {
        OfferDto offerById = offerFacade.findOfferById(offerId);
        return ResponseEntity.ok()
                .body(offerById);
    }

    @PostMapping
    public ResponseEntity<OfferDto> addOffers(@RequestBody OfferDto offer) {
        OfferDto registeredOffer = offerFacade.register(offer);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(new OfferDto().getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(registeredOffer);
    }
}

package org.joboffer.infrastructure.offer.http;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joboffer.domain.offer.OfferFetcher;
import org.joboffer.domain.offer.OfferNotFoundException;
import org.joboffer.domain.offer.dto.OfferDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class OfferFetcherRestTemplate implements OfferFetcher {

    private final RestTemplate restTemplate;
    private final String uriService;
    private final int serverPort;

    @Override
    public List<OfferDto> fetchAllOffers() {
        log.info("Started fetching offers using http client");
        HttpHeaders httpHeaders = new HttpHeaders();
        final HttpEntity<HttpHeaders> headersHttpEntity = new HttpEntity<>(httpHeaders);
        try {
            String urlForService = getUrlForService();
            final String url = UriComponentsBuilder.fromHttpUrl(urlForService).toUriString();
            ResponseEntity<List<OfferDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    headersHttpEntity,
                    new ParameterizedTypeReference<>() {
                    });
            final List<OfferDto> responseBody = response.getBody();
            if (Objects.isNull(responseBody)) {
                log.info("Response Body was null returning empty list");
                throw new OfferNotFoundException("Unable to fetch offers");
            }
            log.info("Success Response Body Returned: " + responseBody);
            return responseBody;
        } catch (ResourceAccessException e) {
            log.error("Error while fetching offers using http client: " + e.getMessage());
            throw new OfferNotFoundException("Unable to fetch offers");
        }
    }

    private String getUrlForService() {
        return uriService + ":" + serverPort + "/offers";
    }
}
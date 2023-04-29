package org.joboffer.domain.offer;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.StreamSupport;


class OfferRepositoryTestImpl implements OfferRepository {

    private final Map<String, Offer> offers = new HashMap<>();

    @Override
    public Offer save(Offer offerToSave) {
        return offers.put(offerToSave.getId(), offerToSave);
    }

    @Override
    public Optional<Offer> findOfferByOfferUrl(String url) {
        return offers.values()
                .stream()
                .filter(offer -> offer.getOfferUrl().equals(url))
                .findFirst();
    }

    @Override
    public boolean existsByOfferUrl(String url) {
        return offers.values()
                .stream()
                .noneMatch(offer -> offer.getOfferUrl().equals(url));
    }


    @Override
    public Optional<Offer> findOfferById(String offerId) {
        return offers.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(offerId))
                .map(Map.Entry::getValue)
                .findFirst();
    }


    @Override
    public Optional<Offer> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }


    @Override
    public <S extends Offer> List<S> saveAll(Iterable<S> entities) {
        return (List<S>) StreamSupport.stream(entities.spliterator(), false)
                .map(this::save)
                .toList();
    }

    @Override
    public List<Offer> findAll() {
        return offers.values().stream().toList();
    }

    @Override
    public Iterable<Offer> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Offer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Offer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Offer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Offer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Offer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(Example<S> example) {
        return false;
    }


    @Override
    public <S extends Offer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}

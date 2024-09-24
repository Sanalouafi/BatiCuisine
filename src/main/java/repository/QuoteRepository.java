package main.java.repository;

import main.java.entities.Quote;

import java.util.List;

public interface QuoteRepository {

    void save(Quote quote);

    Quote findById(Long id);

    List<Quote> findAll();

    void update(Quote quote);

    void deleteById(Long id);
}

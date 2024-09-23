package main.java.repository;

import main.java.entities.Labor;
import main.java.exception.LaborValidationException;

import java.util.List;
import java.util.Optional;

public interface LaborRepository {
    Optional<Labor> findById(Long id);
    List<Labor> findAll();
    void save(Labor labor) throws LaborValidationException;
    void update(Labor labor) throws LaborValidationException;
    void deleteById(Long id);
}

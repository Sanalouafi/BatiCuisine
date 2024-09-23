package main.java.repository;

import main.java.entities.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Optional<Client> findById(Long id);
    List<Client> findAll();
    void save(Client client);
    void update(Client client);
    void deleteById(Long id);
}

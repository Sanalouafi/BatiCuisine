package main.java.service;

import main.java.entities.Client;
import main.java.exception.ClientValidationException;
import main.java.repository.ClientRepository;
import main.java.repository.impl.ClientRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepositoryImpl();
    }

    public Optional<Client> findClientById(Long id) {
        if (id == null || id <= 0) {
            throw new ClientValidationException("Invalid client ID provided.");
        }
        return clientRepository.findById(id);
    }
    private Optional<Client > findClientByName(String name){
        if (name==null||name.isEmpty()){
            throw new ClientValidationException("Invalid client ID provided.");
        }
        return  clientRepository.findByName(name);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public void createClient(Client client) {
        validateClient(client);
        clientRepository.save(client);
    }

    public void updateClient(Client client) {
        if (client.getId() == null || client.getId() <= 0) {
            throw new ClientValidationException("Invalid client ID for update.");
        }
        validateClient(client);
        clientRepository.update(client);
    }

    public void deleteClient(Long id) {
        if (id == null || id <= 0) {
            throw new ClientValidationException("Invalid client ID for deletion.");
        }
        clientRepository.deleteById(id);
    }

    private void validateClient(Client client) {
        if (client == null) {
            throw new ClientValidationException("Client cannot be null.");
        }
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new ClientValidationException("Client name cannot be null or empty.");
        }
        if (client.getAddress() == null || client.getAddress().trim().isEmpty()) {
            throw new ClientValidationException("Client address cannot be null or empty.");
        }
        if (client.getPhone() == null || client.getPhone().trim().isEmpty()) {
            throw new ClientValidationException("Client phone number cannot be null or empty.");
        }
        if (client.getIsProfessional() == null) {
            throw new ClientValidationException("Client professional status cannot be null.");
        }
    }
}

package main.java.repository.impl;

import main.java.connection.DatabaseConnection;
import main.java.entities.Client;
import main.java.repository.ClientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository {

    private final Connection connection;

    public ClientRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Client> findById(Long id) {
        String query = "SELECT * FROM client WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhone(resultSet.getString("phone"));
                client.setIsProfessional(resultSet.getBoolean("is_professional"));
                return Optional.of(client);
            }
        } catch (SQLException e) {
            System.out.println("Error finding client by ID: " + e.getMessage());
        }
        return Optional.empty();
    }
    @Override
    public Optional<Client> findByName(String name) {
        String query = "SELECT * FROM client WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            List<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhone(resultSet.getString("phone"));
                client.setIsProfessional(resultSet.getBoolean("is_professional"));
                clients.add(client);
            }

            return clients.stream().findFirst();

        } catch (SQLException e) {
            System.out.println("Error finding client by name: " + e.getMessage());
        }
        return Optional.empty();
    }
    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhone(resultSet.getString("phone"));
                client.setIsProfessional(resultSet.getBoolean("is_professional"));
                clients.add(client);
            }
        } catch (SQLException e) {
            System.out.println("Error finding all clients: " + e.getMessage());
        }
        return clients;
    }


    @Override
    public void save(Client client) {
        // Ensure client has proper values set
        if (client.getIsProfessional() == null) {
            client.setIsProfessional(false);
        }

        String query = "INSERT INTO client (name, address, phone, is_professional) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getAddress());
            statement.setString(3, client.getPhone());
            statement.setBoolean(4, client.getIsProfessional());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        client.setId(generatedKeys.getLong(1));
                    }
                }
                System.out.println("Client saved successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error saving client: " + e.getMessage());
        }
    }
    @Override
    public void update(Client client) {
        // Validate the client before updating
        client.setName(client.getName());
        client.setAddress(client.getAddress());
        client.setPhone(client.getPhone());
        client.setIsProfessional(client.getIsProfessional());

        String query = "UPDATE client SET name = ?, address = ?, phone = ?, is_professional = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getAddress());
            statement.setString(3, client.getPhone());
            statement.setBoolean(4, client.getIsProfessional());
            statement.setLong(5, client.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Client updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error updating client: " + e.getMessage());
        }
    }


    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Client deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting client: " + e.getMessage());
        }
    }
}

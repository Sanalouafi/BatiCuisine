package main.java.repository.impl;

import main.java.connection.DatabaseConnection;
import main.java.entities.Material;
import main.java.enums.ComponentType;
import main.java.repository.MaterialRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialRepositoryImpl implements MaterialRepository {

    private final Connection connection;

    public MaterialRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Material material) {
        String query = "INSERT INTO material (name, type, unit_cost, quantity, vat_rate, project_id, transport_cost, quality_coefficient) " +
                "VALUES (?, ?::componenttype, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, material.getName());
            statement.setString(2, material.getType().name());
            statement.setBigDecimal(3, material.getUnitCost());
            statement.setBigDecimal(4, material.getQuantity());
            statement.setBigDecimal(5, material.getVatRate());
            statement.setLong(6, material.getProject().getId());
            statement.setBigDecimal(7, material.getTransportCost());
            statement.setBigDecimal(8, material.getQualityCoefficient());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        material.setId(generatedKeys.getLong(1));
                    }
                }
                System.out.println("Material saved successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error saving material: " + e.getMessage());
        }
    }

    @Override
    public void update(Material material) {
        String query = "UPDATE material SET name = ?, unit_cost = ?, quantity = ?, vat_rate = ?, transport_cost = ?, quality_coefficient = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, material.getName());
            statement.setBigDecimal(2, material.getUnitCost());
            statement.setBigDecimal(3, material.getQuantity());
            statement.setBigDecimal(4, material.getVatRate());
            statement.setBigDecimal(5, material.getTransportCost());
            statement.setBigDecimal(6, material.getQualityCoefficient());
            statement.setLong(7, material.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Material updated successfully!");
            } else {
                System.out.println("No material found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating material: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM material WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Material deleted successfully!");
            } else {
                System.out.println("No material found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting material: " + e.getMessage());
        }
    }

    @Override
    public Optional<Material> findById(Long id) {
        String query = "SELECT * FROM material WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Material material = new Material();
                    material.setId(resultSet.getLong("id"));
                    material.setName(resultSet.getString("name"));
                    material.setType(ComponentType.valueOf(resultSet.getString("type")));
                    material.setUnitCost(resultSet.getBigDecimal("unit_cost"));
                    material.setQuantity(resultSet.getBigDecimal("quantity"));
                    material.setVatRate(resultSet.getBigDecimal("vat_rate"));
                    material.setTransportCost(resultSet.getBigDecimal("transport_cost"));
                    material.setQualityCoefficient(resultSet.getBigDecimal("quality_coefficient"));

                    return Optional.of(material);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding material: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Material> findAll() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT * FROM material";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Material material = new Material();
                material.setId(resultSet.getLong("id"));
                material.setName(resultSet.getString("name"));
                material.setType(ComponentType.valueOf(resultSet.getString("type")));
                material.setUnitCost(resultSet.getBigDecimal("unit_cost"));
                material.setQuantity(resultSet.getBigDecimal("quantity"));
                material.setVatRate(resultSet.getBigDecimal("vat_rate"));
                material.setTransportCost(resultSet.getBigDecimal("transport_cost"));
                material.setQualityCoefficient(resultSet.getBigDecimal("quality_coefficient"));

                materials.add(material);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving materials: " + e.getMessage());
        }
        return materials;
    }
}

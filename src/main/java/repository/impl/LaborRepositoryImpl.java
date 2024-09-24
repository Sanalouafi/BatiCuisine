package main.java.repository.impl;

import main.java.connection.DatabaseConnection;
import main.java.entities.Labor;
import main.java.entities.Project;
import main.java.exception.LaborValidationException;
import main.java.repository.LaborRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LaborRepositoryImpl implements LaborRepository {

    private final Connection connection;

    public LaborRepositoryImpl() {

        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Labor> findById(Long id) {
        String query = "SELECT * FROM labor WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Labor labor = new Labor();
                labor.setId(resultSet.getLong("id"));
                labor.setName(resultSet.getString("name"));
                labor.setVatRate(resultSet.getBigDecimal("vat_rate"));
                labor.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                labor.setHoursWorked(resultSet.getBigDecimal("hours_worked"));
                labor.setProductivityFactor(resultSet.getBigDecimal("productivity_factor"));
                return Optional.of(labor);
            }
        } catch (SQLException e) {
            System.out.println("Error finding labor by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Labor> findAll() {
        List<Labor> labors = new ArrayList<>();
        String query = "SELECT * FROM labor";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Labor labor = new Labor();
                labor.setId(resultSet.getLong("id"));
                labor.setName(resultSet.getString("name"));
                labor.setVatRate(resultSet.getBigDecimal("vat_rate"));
                labor.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                labor.setHoursWorked(resultSet.getBigDecimal("hours_worked"));
                labor.setProductivityFactor(resultSet.getBigDecimal("productivity_factor"));
                labors.add(labor);
            }
        } catch (SQLException e) {
            System.out.println("Error finding all labors: " + e.getMessage());
        }
        return labors;
    }

    @Override
    public void save(Labor labor) throws LaborValidationException {
        validateLabor(labor);

        String query = "INSERT INTO labor (name,vat_rate, hourly_rate, hours_worked, productivity_factor, type,project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?::componenttype,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, labor.getName());
            statement.setBigDecimal(2, labor.getVatRate());
            statement.setBigDecimal(3, labor.getHourlyRate());
            statement.setBigDecimal(4, labor.getHoursWorked());
            statement.setBigDecimal(5, labor.getProductivityFactor());
            statement.setString(6, labor.getType().name());
            statement.setLong(7, labor.getProject().getId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        labor.setId(generatedKeys.getLong(1));
                    }
                }
                System.out.println("Labor saved successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error saving labor: " + e.getMessage());
        }
    }


    @Override
    public void update(Labor labor) throws LaborValidationException {
        validateLabor(labor);

        String query = "UPDATE labor SET name = ?, vat_rate = ?, hourly_rate = ?, hours_worked = ?, productivity_factor = ?, type = ?::componenttype,project_id=? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, labor.getName());
            statement.setBigDecimal(2, labor.getVatRate());
            statement.setBigDecimal(3, labor.getHourlyRate());
            statement.setBigDecimal(4, labor.getHoursWorked());
            statement.setBigDecimal(5, labor.getProductivityFactor());
            statement.setString(6, labor.getType().name());
            statement.setLong(7, labor.getProject().getId());
            statement.setLong(8, labor.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Labor updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error updating labor: " + e.getMessage());
        }
    }


    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM labor WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Labor deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting labor: " + e.getMessage());
        }
    }
    @Override
    public List<Labor> findByProject(Project project) {
        List<Labor> laborList = new ArrayList<>();
        String query = "SELECT * FROM labor WHERE project_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, project.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Labor labor = new Labor();
                    labor.setId(resultSet.getLong("id"));
                    labor.setName(resultSet.getString("name"));
                    labor.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                    labor.setHoursWorked(resultSet.getBigDecimal("hours_worked"));
                    labor.setVatRate(resultSet.getBigDecimal("vat_rate"));

                    laborList.add(labor);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving labor for project: " + e.getMessage());
        }
        return laborList;
    }


    private void validateLabor(Labor labor) throws LaborValidationException {
        if (labor.getHourlyRate() == null || labor.getHourlyRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new LaborValidationException("Hourly rate cannot be negative.");
        }
        if (labor.getHoursWorked() == null || labor.getHoursWorked().compareTo(BigDecimal.ZERO) < 0) {
            throw new LaborValidationException("Hours worked cannot be negative.");
        }
        if (labor.getProductivityFactor() == null || labor.getProductivityFactor().compareTo(BigDecimal.ZERO) < 0 || labor.getProductivityFactor().compareTo(BigDecimal.ONE) > 1) {
            throw new LaborValidationException("Productivity factor must be between 0 and 1.");
        }
    }
}

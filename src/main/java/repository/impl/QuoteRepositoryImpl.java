package main.java.repository.impl;

import main.java.connection.DatabaseConnection;
import main.java.entities.Project;
import main.java.entities.Quote;
import main.java.repository.QuoteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuoteRepositoryImpl implements QuoteRepository {

    private final Connection connection;

    public QuoteRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Quote quote) {
        String query = "INSERT INTO Quote (estimated_amount, issue_date, validity_date, is_accepted, project_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBigDecimal(1, quote.getEstimatedAmount());
            statement.setDate(2, Date.valueOf(quote.getIssueDate()));
            statement.setDate(3, Date.valueOf(quote.getValidityDate()));
            statement.setBoolean(4, quote.getIsAccepted());
            statement.setLong(5, quote.getProject().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        quote.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error saving quote: " + e.getMessage());
        }
    }

    @Override
    public Quote findById(Long id) {
        String query = "SELECT * FROM Quote WHERE id = ?";
        Quote quote = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Mapping ResultSet to Quote directly in this method
                quote = new Quote();
                quote.setId(resultSet.getLong("id"));
                quote.setEstimatedAmount(resultSet.getBigDecimal("estimated_amount"));
                quote.setIssueDate(resultSet.getDate("issue_date").toLocalDate());
                quote.setValidityDate(resultSet.getDate("validity_date").toLocalDate());
                quote.setIsAccepted(resultSet.getBoolean("is_accepted"));

                Project project = new Project();
                project.setId(resultSet.getLong("project_id"));
                quote.setProject(project);
            }
        } catch (SQLException e) {
            System.out.println("Error finding quote by id: " + e.getMessage());
        }
        return quote;
    }

    @Override
    public List<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        String query = "SELECT * FROM Quote";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                // Mapping ResultSet to Quote directly in this loop
                Quote quote = new Quote();
                quote.setId(resultSet.getLong("id"));
                quote.setEstimatedAmount(resultSet.getBigDecimal("estimated_amount"));
                quote.setIssueDate(resultSet.getDate("issue_date").toLocalDate());
                quote.setValidityDate(resultSet.getDate("validity_date").toLocalDate());
                quote.setIsAccepted(resultSet.getBoolean("is_accepted"));

                Project project = new Project();
                project.setId(resultSet.getLong("project_id"));
                quote.setProject(project);

                quotes.add(quote);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving quotes: " + e.getMessage());
        }
        return quotes;
    }

    @Override
    public void update(Quote quote) {
        String query = "UPDATE Quote SET estimated_amount = ?, issue_date = ?, validity_date = ?, is_accepted = ?, project_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBigDecimal(1, quote.getEstimatedAmount());
            statement.setDate(2, Date.valueOf(quote.getIssueDate()));
            statement.setDate(3, Date.valueOf(quote.getValidityDate()));
            statement.setBoolean(4, quote.getIsAccepted());
            statement.setLong(5, quote.getProject().getId());
            statement.setLong(6, quote.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating quote: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM Quote WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting quote: " + e.getMessage());
        }
    }
}

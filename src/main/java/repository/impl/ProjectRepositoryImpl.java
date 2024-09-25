package main.java.repository.impl;

import main.java.connection.DatabaseConnection;
import main.java.entities.Client;
import main.java.entities.Project;
import main.java.enums.ProjectStatus;
import main.java.repository.ProjectRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final Connection connection;

    public ProjectRepositoryImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Project> findById(Long id) {
        String query = "SELECT * FROM Project WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getLong("id"));
                project.setName(resultSet.getString("name"));
                project.setProfitMargin(resultSet.getBigDecimal("profit_margin"));
                project.setTotalCost(resultSet.getBigDecimal("total_cost"));
                project.setStatus(ProjectStatus.valueOf(resultSet.getString("status")));

                Client client = new Client();
                client.setId(resultSet.getLong("client_id"));
                project.setClient(client);

                return Optional.of(project);
            }
        } catch (SQLException e) {
            System.out.println("Error finding project by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM Project";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getLong("id"));
                project.setName(resultSet.getString("name"));
                project.setProfitMargin(resultSet.getBigDecimal("profit_margin"));
                project.setTotalCost(resultSet.getBigDecimal("total_cost"));
                project.setStatus(ProjectStatus.valueOf(resultSet.getString("status")));

                Client client = new Client();
                client.setId(resultSet.getLong("client_id"));
                project.setClient(client);

                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Error finding all projects: " + e.getMessage());
        }
        return projects;
    }

    @Override
    public void save(Project project) {
        validateProject(project);

        String query = "INSERT INTO Project (name, profit_margin, total_cost, status, client_id) VALUES (?, ?, ?, ?::projectstatus, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, project.getName());
            statement.setBigDecimal(2, project.getProfitMargin());
            statement.setBigDecimal(3, project.getTotalCost());
            statement.setString(4, project.getStatus().name());
            statement.setLong(5, project.getClient().getId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setId(generatedKeys.getLong(1));
                    }
                }
                System.out.println("Project saved successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error saving project: " + e.getMessage());
        }
    }

    @Override
    public void update(Project project) {
        validateProject(project);

        String query = "UPDATE project SET name = ?, profit_margin = ?, total_cost = ?, status = ?::projectstatus, client_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, project.getName());
            statement.setBigDecimal(2, project.getProfitMargin());
            statement.setBigDecimal(3, project.getTotalCost());
            statement.setString(4, project.getStatus().name()); // Ensure this matches the enum values in the database
            statement.setLong(5, project.getClient().getId());
            statement.setLong(6, project.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Project updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error updating project: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM Project WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Project deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting project: " + e.getMessage());
        }
    }

    @Override
    public List<Project> findByStatus(ProjectStatus status) {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM Project WHERE status = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status.name());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getLong("id"));
                project.setName(resultSet.getString("name"));
                project.setProfitMargin(resultSet.getBigDecimal("profit_margin"));
                project.setTotalCost(resultSet.getBigDecimal("total_cost"));
                project.setStatus(ProjectStatus.valueOf(resultSet.getString("status")));

                Client client = new Client();
                client.setId(resultSet.getLong("client_id"));
                project.setClient(client);

                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Error finding projects by status: " + e.getMessage());
        }
        return projects;
    }

    private void validateProject(Project project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (project.getProfitMargin() == null || project.getProfitMargin().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Profit margin cannot be negative");
        }
        if (project.getTotalCost() == null || project.getTotalCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total cost cannot be negative");
        }
        if (project.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (project.getClient() == null || project.getClient().getId() == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
    }
    @Override
    public void updateProjectStatus(Long projectId, ProjectStatus newStatus) {
        Optional<Project> projectOptional = findById(projectId);

        if (projectOptional.isEmpty()) {
            System.out.println("Project with ID " + projectId + " does not exist.");
            return;
        }

        String query = "UPDATE Project SET status = ?::projectstatus WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus.name());
            statement.setLong(2, projectId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Project status updated successfully!");
            } else {
                System.out.println("No project was found with ID " + projectId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating project status: " + e.getMessage());
        }
    }
    //get client's projects

@Override
public List<Project> findProjectsByClient(Long clientId) {
    List<Project> projects = new ArrayList<>();
    String query = "SELECT * FROM Project WHERE client_id = ?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setLong(1, clientId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Project project = new Project();
            project.setId(resultSet.getLong("id"));
            project.setName(resultSet.getString("name"));
            project.setProfitMargin(resultSet.getBigDecimal("profit_margin"));
            project.setTotalCost(resultSet.getBigDecimal("total_cost"));
            project.setStatus(ProjectStatus.valueOf(resultSet.getString("status")));

            Client client = new Client();
            client.setId(resultSet.getLong("client_id"));
            project.setClient(client);

            projects.add(project);
        }
    } catch (SQLException e) {
        System.out.println("Error finding projects by client: " + e.getMessage());
    }

    return projects;
}

}

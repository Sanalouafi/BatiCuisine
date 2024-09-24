package main.java.service;

import main.java.entities.Project;
import main.java.enums.ProjectStatus;
import main.java.exception.ProjectValidationException;
import main.java.repository.ProjectRepository;
import main.java.repository.impl.ProjectRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService() {
        this.projectRepository = new ProjectRepositoryImpl();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public void save(Project project) {
        validateProject(project);
        projectRepository.save(project);
    }

    public void update(Project project) {
        validateProject(project);
        projectRepository.update(project);
    }

    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    public List<Project> findByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status);
    }

    private void validateProject(Project project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new ProjectValidationException("Project name cannot be null or empty.");
        }
        if (project.getProfitMargin() == null || project.getProfitMargin().compareTo(BigDecimal.ZERO) < 0) {
            throw new ProjectValidationException("Profit margin cannot be negative.");
        }
        if (project.getTotalCost() == null || project.getTotalCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new ProjectValidationException("Total cost cannot be negative.");
        }
        if (project.getStatus() == null) {
            throw new ProjectValidationException("Project status cannot be null.");
        }
        if (project.getClient() == null || project.getClient().getId() == null) {
            throw new ProjectValidationException("Client cannot be null.");
        }
    }
}

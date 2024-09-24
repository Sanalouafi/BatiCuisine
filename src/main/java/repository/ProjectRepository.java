package main.java.repository;

import main.java.entities.Project;
import main.java.enums.ProjectStatus;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Optional<Project> findById(Long id);
    List<Project> findAll();
    void save(Project project);
    void update(Project project);
    void deleteById(Long id);

    List<Project> findByStatus(ProjectStatus status);
    void updateProjectStatus(Long projectId, ProjectStatus newStatus);
}

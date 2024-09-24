package main.java.repository;
import main.java.entities.Material;
import main.java.entities.Project;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MaterialRepository {
    void save(Material material);
    void update(Material material);
    void delete(Long id);
    Optional<Material> findById(Long id);
    List<Material> findAll();
    List<Material> findByProject(Project project);
}


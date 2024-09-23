package main.java.repository;
import main.java.entities.Material;
import java.util.List;
import java.util.Optional;

public interface MaterialRepository {
    void save(Material material);
    void update(Material material);
    void delete(Long id);
    Optional<Material> findById(Long id);
    List<Material> findAll();
}


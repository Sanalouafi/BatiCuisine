package main.java.repository;
import main.java.entities.Material;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MaterialRepository {
    void save(Material material);
    void update(Material material);
    void delete(Long id);
    Optional<Material> findById(Long id);
    List<Material> findAll();
    // Total cost without VAT for all materials
    BigDecimal calculCost();
    // Total cost with VAT for all materials
    BigDecimal calculWithVatCost();
}


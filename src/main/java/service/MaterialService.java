package main.java.service;

import main.java.entities.Material;
import main.java.exception.MaterialValidationException;
import main.java.repository.MaterialRepository;
import main.java.repository.impl.MaterialRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService() {
        this.materialRepository = new MaterialRepositoryImpl();
    }

    public void save(Material material) {
        validate(material);
        materialRepository.save(material);
    }

    public void update(Material material) {
        validate(material);
        materialRepository.update(material);
    }

    public void delete(Long id) {
        materialRepository.delete(id);
    }

    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }

    public List<Material> findAll() {
        return materialRepository.findAll();
    }
    // Calculate total cost without VAT for all materials
    public BigDecimal calculCost() {
        return materialRepository.calculCost();
    }

    // Calculate total cost with VAT for all materials
    public BigDecimal calculWithVatCost() {
        return materialRepository.calculWithVatCost();
    }

    private void validate(Material material) {
        if (material.getName() == null || material.getName().isEmpty()) {
            throw new MaterialValidationException("Material name cannot be empty.");
        }
        if (material.getTransportCost() == null || material.getTransportCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new MaterialValidationException("Transport cost cannot be negative.");
        }
        if (material.getQualityCoefficient() == null || material.getQualityCoefficient().compareTo(BigDecimal.ZERO) < 0 ||
                material.getQualityCoefficient().compareTo(BigDecimal.ONE) > 0) {
            throw new MaterialValidationException("Quality coefficient must be between 0 and 1.");
        }
    }
}

package main.java.service;

import main.java.entities.Labor;
import main.java.entities.Project;
import main.java.exception.LaborValidationException;
import main.java.repository.LaborRepository;
import main.java.repository.impl.LaborRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LaborService {

    private final LaborRepository laborRepository;

    public LaborService() {

        this.laborRepository = new LaborRepositoryImpl();
    }

    public void addLabor(Labor labor) {
        try {
            laborRepository.save(labor);
        } catch (LaborValidationException e) {
            System.out.println("Error adding labor: " + e.getMessage());
        }
    }

    public void updateLabor(Labor labor) {
        try {
            laborRepository.update(labor);
        } catch (LaborValidationException e) {
            System.out.println("Error updating labor: " + e.getMessage());
        }
    }

    public void deleteLabor(Long id) {
        laborRepository.deleteById(id);
    }

    public Optional<Labor> getLaborById(Long id) {
        return laborRepository.findById(id);
    }

    public List<Labor> getAllLabors() {
        return laborRepository.findAll();
    }
    //  method to calculate cost with vat and without vat

    public double[] calculateTotalCost(Project project) {
        List<Labor> laborList = laborRepository.findByProject(project);
        double totalWithoutVAT = 0;
        double totalWithVAT = 0;

        for (Labor labor : laborList) {
            double baseCost = labor.getHoursWorked().doubleValue() * labor.getHourlyRate().doubleValue();

            double totalCostBeforeVAT = baseCost;

            double totalCostWithVAT = totalCostBeforeVAT * (1 + labor.getVatRate().doubleValue() / 100);

            totalWithoutVAT += totalCostBeforeVAT;
            totalWithVAT += totalCostWithVAT;
        }

        return new double[]{totalWithoutVAT, totalWithVAT};
    }
}

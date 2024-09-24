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
    private final MaterialService materialService;
    private final LaborService laborService;
    public ProjectService() {
        this.projectRepository = new ProjectRepositoryImpl();
        this.laborService= new LaborService();
        this.materialService= new MaterialService();
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
    public void updateProjectStatus(Long projectId, ProjectStatus newStatus) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            System.out.println("Error: Project with ID " + projectId + " does not exist.");
            return;
        }
        projectRepository.updateProjectStatus(projectId, newStatus);
        System.out.println("Project status updated to " + newStatus + " for project ID " + projectId);
    }
    public double[] calculateTotalCost(Project project, double marginRate) {
        //Calculate total cost for materials
        double[] materialTotals = materialService.calculateTotalCost(project);
        double totalMaterialsWithoutVAT = materialTotals[0];
        double totalMaterialsWithVAT = materialTotals[1];

        //  Calculate total cost for labor
        double[] laborTotals = laborService.calculateTotalCost(project);
        double totalLaborWithoutVAT = laborTotals[0];
        double totalLaborWithVAT = laborTotals[1];

        //  Total cost before VAT
        double totalCostBeforeVAT = totalMaterialsWithoutVAT + totalLaborWithoutVAT;

        //  Total cost with VAT
        double totalCostWithVAT = totalMaterialsWithVAT + totalLaborWithVAT;

        //Calculate margin based on the total cost with VAT
        double totalMargin = totalCostWithVAT * marginRate;

        //Calculate the final total cost (with VAT and margin)
        double finalTotalCost = totalCostWithVAT + totalMargin;

        // Output the results
        System.out.println("Total cost with VAT: " + totalCostWithVAT);
        System.out.println("Total margin: " + totalMargin);
        System.out.println("Final total cost: " + finalTotalCost);

        // Return the results as an array
        return new double[]{totalCostBeforeVAT, totalCostWithVAT, totalMargin, finalTotalCost};
    }
}

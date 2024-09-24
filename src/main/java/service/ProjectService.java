package main.java.service;

import main.java.entities.Client;
import main.java.entities.Project;
import main.java.enums.ProjectStatus;
import main.java.exception.ProjectValidationException;
import main.java.repository.ProjectRepository;
import main.java.repository.impl.ProjectRepositoryImpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MaterialService materialService;
    private final LaborService laborService;
    private final ClientService clientService;
    private Long currentProjectId;

    public ProjectService() {
        this.projectRepository = new ProjectRepositoryImpl();
        this.materialService = new MaterialService();
        this.laborService = new LaborService();
        this.clientService = new ClientService();
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
        // Calculate total cost for materials
        double[] materialTotals = materialService.calculateTotalCost(project);
        double totalMaterialsWithoutVAT = materialTotals[0];
        double totalMaterialsWithVAT = materialTotals[1];

        // Calculate total cost for labor
        double[] laborTotals = laborService.calculateTotalCost(project);
        double totalLaborWithoutVAT = laborTotals[0];
        double totalLaborWithVAT = laborTotals[1];

        // Total cost before VAT
        double totalCostBeforeVAT = totalMaterialsWithoutVAT + totalLaborWithoutVAT;

        // Total cost with VAT
        double totalCostWithVAT = totalMaterialsWithVAT + totalLaborWithVAT;

        // Calculate margin based on the total cost with VAT
        double totalMargin = totalCostWithVAT * marginRate;

        // Calculate the final total cost (with VAT and margin)
        double finalTotalCost = totalCostWithVAT + totalMargin;

        // Apply professional client discount if applicable
        if (project.getClient() != null && project.getClient().getIsProfessional()) {
            finalTotalCost = applyProfessionalClientDiscount(finalTotalCost);
        }

        // Output the results
        System.out.println("Total cost with VAT: " + totalCostWithVAT);
        System.out.println("Total margin: " + totalMargin);
        System.out.println("Final total cost after discount (if applicable): " + finalTotalCost);

        // Return the results as an array
        return new double[]{totalCostBeforeVAT, totalCostWithVAT, totalMargin, finalTotalCost};
    }

    private double applyProfessionalClientDiscount(double finalTotalCost) {
        // Convert finalTotalCost to BigDecimal for calculation
        BigDecimal totalCostBD = BigDecimal.valueOf(finalTotalCost);
        BigDecimal discountRate = BigDecimal.valueOf(0.3); // 30% discount

        // Calculate the final cost after applying the discount
        return totalCostBD.multiply(BigDecimal.ONE.subtract(discountRate)).doubleValue();
    }
    public HashMap<Client, List<Project>> findClientProjects() {
        HashMap<Client, List<Project>> clientProjectsMap = new HashMap<>();
        List<Client> clients = clientService.getAllClients();

        for (Client client : clients) {
            List<Project> projects = projectRepository.findProjectsByClient(client.getId());
            if (!projects.isEmpty()) {
                clientProjectsMap.put(client, projects);
            }
        }

        return clientProjectsMap;
    }

    // Get the current project_id
    public Long getCurrentProjectId() {
        if (currentProjectId == null) {
            throw new IllegalStateException("No current project is set.");
        }
        return currentProjectId;
    }

    public void setCurrentProjectId(Long projectId) {
        this.currentProjectId = projectId;
    }
}

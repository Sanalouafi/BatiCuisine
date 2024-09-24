package main.java.menu;
import main.java.entities.Client;
import main.java.entities.Project;
import main.java.enums.ProjectStatus;
import main.java.service.ProjectService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProjectMenu {
    private final Scanner scanner;
    private final ProjectService projectService;

    public ProjectMenu() {
        this.scanner = new Scanner(System.in);
        this.projectService = new ProjectService();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Project Management Menu ---");
            System.out.println("1. Create a New Project");
            System.out.println("2. Update a Project");
            System.out.println("3. Delete a Project");
            System.out.println("4. View All Projects");
            System.out.println("5. Find Projects by Status");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    createProject();
                    break;
                case 2:
                    updateProject();
                    break;
                case 3:
                    deleteProject();
                    break;
                case 4:
                    viewAllProjects();
                    break;
                case 5:
                    findProjectsByStatus();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createProject() {
        System.out.print("Enter the project name: ");
        String projectName = scanner.nextLine();
        System.out.print("Enter the profit margin: ");
        BigDecimal profitMargin = scanner.nextBigDecimal();
        System.out.print("Enter the total cost: ");
        BigDecimal totalCost = scanner.nextBigDecimal();
        System.out.print("Enter the client ID: ");
        Long clientId = scanner.nextLong();
        scanner.nextLine(); 
        Project project = new Project();
        project.setName(projectName);
        project.setProfitMargin(profitMargin);
        project.setTotalCost(totalCost);
        project.setClient(new Client());
        project.setStatus(ProjectStatus.Pending);

        projectService.save(project);
    }

    private void updateProject() {
        System.out.print("Enter the project ID to update: ");
        Long projectId = scanner.nextLong();
        scanner.nextLine(); 
        Optional<Project> projectOptional = projectService.findById(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            System.out.println("Current project details: " + project);

            System.out.print("Enter the new project name (leave blank for no change): ");
            String newName = scanner.nextLine();
            if (!newName.trim().isEmpty()) {
                project.setName(newName);
            }

            System.out.print("Enter the new profit margin (leave blank for no change): ");
            String profitMarginInput = scanner.nextLine();
            if (!profitMarginInput.trim().isEmpty()) {
                project.setProfitMargin(new BigDecimal(profitMarginInput));
            }

            System.out.print("Enter the new total cost (leave blank for no change): ");
            String totalCostInput = scanner.nextLine();
            if (!totalCostInput.trim().isEmpty()) {
                project.setTotalCost(new BigDecimal(totalCostInput));
            }

            System.out.print("Enter the new status (leave blank for no change): ");
            String newStatusInput = scanner.nextLine();
            if (!newStatusInput.trim().isEmpty()) {
                project.setStatus(ProjectStatus.valueOf(newStatusInput.toUpperCase()));
            }

            projectService.update(project);
        } else {
            System.out.println("Project with ID " + projectId + " does not exist.");
        }
    }

    private void deleteProject() {
        System.out.print("Enter the project ID to delete: ");
        Long projectId = scanner.nextLong();
        scanner.nextLine();
        projectService.deleteById(projectId);
    }

    private void viewAllProjects() {
        List<Project> projects = projectService.findAll();
        System.out.println("All Projects:");
        for (Project project : projects) {
            System.out.println(project);
        }
    }

    private void findProjectsByStatus() {
        System.out.print("Enter the project status (e.g., Confirmed, Pending,Canceled): ");
        String statusInput = scanner.nextLine();
        ProjectStatus status = ProjectStatus.valueOf(statusInput.toUpperCase());
        List<Project> projects = projectService.findByStatus(status);
        System.out.println("Projects with status " + status + ":");
        for (Project project : projects) {
            System.out.println(project);
        }
    }
}

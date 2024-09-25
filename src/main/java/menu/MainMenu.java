package main.java.menu;

import main.java.entities.*;
import main.java.enums.ComponentType;
import main.java.enums.ProjectStatus;
import main.java.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClientService clientService = new ClientService();
    private static final ProjectService projectService = new ProjectService();
    private static final MaterialService materialService = new MaterialService();
    private static final LaborService laborService = new LaborService();
    private static final QuoteService quoteService = new QuoteService();

    public void displayMenu() {
        while (true) {
            showMainMenu();
            int choice = getValidIntegerInput();

            switch (choice) {
                case 1:
                    createNewProject();
                    break;
                case 2:
                    viewExistingProjects();
                    break;
                case 3:
                    calculateProjectCost();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("=== Main Menu ===");
        System.out.println("1. Create a new project");
        System.out.println("2. View existing projects");
        System.out.println("3. Calculate project cost");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private int getValidIntegerInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private void createNewProject() {
        System.out.println("--- Client Search ---");
        System.out.println("Do you want to search for an existing client or add a new one?");
        System.out.println("1. Search for an existing client");
        System.out.println("2. Add a new client");
        System.out.print("Choose an option: ");
        int clientChoice = getValidIntegerInput();

        Client selectedClient;

        if (clientChoice == 1) {
            selectedClient = searchExistingClient();
            if (selectedClient == null) return; // Exit if client not found
        } else {
            selectedClient = addNewClient();
        }

        System.out.print("Proceed with this client? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            Project newProject = createProject(selectedClient);
            handleMaterialsAndLabors(newProject);
            registerQuote(newProject);
        }
    }

    private Client searchExistingClient() {
        System.out.print("Enter the client's name: ");
        String clientName = scanner.nextLine();
        return clientService.findClientByName(clientName).orElseGet(() -> {
            System.out.println("Client not found!");
            return null;
        });
    }

    private Client addNewClient() {
        System.out.print("Enter client's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter client's address: ");
        String address = scanner.nextLine();
        System.out.print("Enter client's phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Is the client a professional? (true/false): ");
        boolean isProfessional = scanner.nextBoolean();
        scanner.nextLine(); 

        Client newClient = new Client(name, address, phone, isProfessional);
        clientService.createClient(newClient);
        System.out.println("Client created successfully!");
        return newClient;
    }

    private Project createProject(Client client) {
        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine();
        System.out.print("Enter profit margin: ");
        BigDecimal profitMargin = scanner.nextBigDecimal();

        scanner.nextLine(); 

        // Set default status to Pending
        ProjectStatus status = ProjectStatus.Pending;

        // Initialize totalCost to BigDecimal.ZERO
        BigDecimal totalCost = BigDecimal.ZERO;

        // Create the new project with the initialized total cost
        Project newProject = new Project(projectName, profitMargin, totalCost, ProjectStatus.Pending, client);
        projectService.save(newProject);
        System.out.println("Project created successfully!");
        return newProject;
    }
    private void handleMaterialsAndLabors(Project project) {
        BigDecimal finalTotalCost = BigDecimal.ZERO;

        while (true) {
            System.out.println("Do you want to add materials or labor? (1 for Materials, 2 for Labor, 0 to finish)");
            int choice = getValidIntegerInput();

            if (choice == 1) {
                finalTotalCost = addMaterial(project, finalTotalCost);
            } else if (choice == 2) {
                finalTotalCost = addLabor(project, finalTotalCost);
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        // Update the project with the new total cost
        project.setTotalCost(finalTotalCost);
        projectService.update(project);  // Save the updated project
    }
    private BigDecimal addMaterial(Project project, BigDecimal currentTotalCost) {
        System.out.print("Enter material name: ");
        String name = scanner.nextLine();

        // Set a default component type of MATERIAL
        ComponentType type = ComponentType.Materiel;

        // Optionally, ask user if they want to specify a different type
        System.out.print("Do you want to specify a different component type? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Enter component type (e.g., MATERIAL, LABOR): ");
            type = ComponentType.valueOf(scanner.nextLine().toUpperCase());
        }

        System.out.print("Enter unit cost: ");
        BigDecimal unitCost = scanner.nextBigDecimal();
        System.out.print("Enter quantity: ");
        BigDecimal quantity = scanner.nextBigDecimal();
        System.out.print("Enter VAT rate: ");
        BigDecimal vatRate = scanner.nextBigDecimal();
        System.out.print("Enter transport cost: ");
        BigDecimal transportCost = scanner.nextBigDecimal();
        System.out.print("Enter quality coefficient (0 to 1): ");
        BigDecimal qualityCoefficient = scanner.nextBigDecimal();
        scanner.nextLine(); 

        Material material = new Material(name, type, unitCost, quantity, vatRate, project, transportCost, qualityCoefficient);
        materialService.save(material);

        // Calculate and return new total cost
        BigDecimal totalMaterialCost = unitCost.multiply(quantity).add(transportCost);
        System.out.println("Material added successfully! Total Material Cost: " + totalMaterialCost);
        return currentTotalCost.add(totalMaterialCost); // Update and return the new total cost
    }
    private BigDecimal addLabor(Project project, BigDecimal currentTotalCost) {
        System.out.print("Enter labor name: ");
        String name = scanner.nextLine();

        // Set a default component type of LABOR
        ComponentType type = ComponentType.Labor;

        // Optionally, ask user if they want to specify a different type
        System.out.print("Do you want to specify a different component type? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Enter component type (e.g., MATERIAL, LABOR): ");
            type = ComponentType.valueOf(scanner.nextLine().toUpperCase());
        }

        System.out.print("Enter hourly rate: ");
        BigDecimal hourlyRate = scanner.nextBigDecimal();
        System.out.print("Enter hours worked: ");
        BigDecimal hoursWorked = scanner.nextBigDecimal();
        System.out.print("Enter VAT rate: ");
        BigDecimal vatRate = scanner.nextBigDecimal();
        System.out.print("Enter productivity factor (0 to 1): ");
        BigDecimal productivityFactor = scanner.nextBigDecimal();
        scanner.nextLine(); 

        Labor labor = new Labor(name, type, vatRate, project, hourlyRate, hoursWorked, productivityFactor);
        laborService.addLabor(labor);

        // Calculate and return new total cost
        BigDecimal totalLaborCost = hourlyRate.multiply(hoursWorked);
        System.out.println("Labor added successfully! Total Labor Cost: " + totalLaborCost);
        return currentTotalCost.add(totalLaborCost); // Update and return the new total cost
    }    private void registerQuote(Project project) {
        BigDecimal finalTotalCost = project.getTotalCost(); // Get the final total cost after adding materials and labor

        System.out.print("Enter the issue date of the quote (format: YYYY-MM-DD): ");
        String issueDate = scanner.nextLine();
        System.out.print("Enter the validity date of the quote (format: YYYY-MM-DD): ");
        String validityDate = scanner.nextLine();

        Quote quote = new Quote(finalTotalCost, LocalDate.parse(issueDate), LocalDate.parse(validityDate), false, project);
        quoteService.saveQuote(quote);
        System.out.println("Quote saved successfully!");

        // Prompt user to accept the quote
        System.out.print("Do you want to accept the quote? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            quoteService.updateIsAcceptValue(quote.getId());
        }
    }

    private void viewExistingProjects() {
        System.out.println("--- Existing Projects ---");
        List<Project> projects = projectService.findAll();

        if (projects.isEmpty()) {
            System.out.println("No existing projects found.");
            return;
        }

        for (Project project : projects) {
            System.out.println("Project ID: " + project.getId());
            System.out.println("Project Name: " + project.getName());
            System.out.println("Profit Margin: " + project.getProfitMargin());
            System.out.println("Total Cost: " + project.getTotalCost());
            System.out.println("Client: " + project.getClient().getName());
            System.out.println("Status: " + project.getStatus());
            System.out.println("--------------------------");
        }
    }

    private void calculateProjectCost() {
        System.out.print("Enter the project ID to calculate the cost: ");
        Long projectId = scanner.nextLong();
        scanner.nextLine(); 

        Project project = projectService.findById(projectId).orElse(null);
        if (project == null) {
            System.out.println("Project with ID " + projectId + " not found.");
            return;
        }

        System.out.print("Enter the margin rate (as a decimal, e.g., 0.2 for 20%): ");
        double marginRate = scanner.nextDouble();

        double[] costDetails = projectService.calculateTotalCost(project, marginRate);

        System.out.println("--- Cost Calculation Result ---");
        System.out.println("Total Cost without VAT: " + costDetails[0]);
        System.out.println("Total Cost with VAT: " + costDetails[1]);
        System.out.println("Total Margin: " + costDetails[2]);
        System.out.println("Final Total Cost: " + costDetails[3]);
    }
}
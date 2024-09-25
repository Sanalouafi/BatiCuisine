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
            int choice = getValidIntegerInput("Choose an option: ");

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
    }

    private int getValidIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer: ");
            }
        }
    }

    private String getValidStringInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        while (input.trim().isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            input = scanner.nextLine();
        }
        return input;
    }

    private BigDecimal getValidBigDecimalInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid decimal number: ");
            }
        }
    }

    private boolean getValidBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt + " (true/false): ");
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Invalid input. Please enter 'true' or 'false'.");
        }
    }

    private void createNewProject() {
        System.out.println("--- Client Search ---");
        System.out.println("Do you want to search for an existing client or add a new one?");
        System.out.println("1. Search for an existing client");
        System.out.println("2. Add a new client");
        System.out.print("Choose an option: ");
        int clientChoice = getValidIntegerInput("Choose an option: ");

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
        String clientName = getValidStringInput("Enter the client's name: ");
        return clientService.findClientByName(clientName).orElseGet(() -> {
            System.out.println("Client not found!");
            return null;
        });
    }

    private Client addNewClient() {
        String name = getValidStringInput("Enter client's name: ");
        String address = getValidStringInput("Enter client's address: ");
        String phone = getValidPhoneNumber("Enter client's phone number (10 digits): ");
        boolean isProfessional = getValidBooleanInput("Is the client a professional?");

        Client newClient = new Client(name, address, phone, isProfessional);
        clientService.createClient(newClient);
        System.out.println("Client created successfully!");
        return newClient;
    }

    private String getValidPhoneNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String phone = scanner.nextLine();
            if (phone.matches("\\d{10}")) {
                return phone;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
            }
        }
    }

    private Project createProject(Client client) {
        String projectName = getValidStringInput("Enter project name: ");
        BigDecimal profitMargin = getValidBigDecimalInput("Enter profit margin: ");

        ProjectStatus status = ProjectStatus.Pending;
        BigDecimal totalCost = BigDecimal.ZERO;

        Project newProject = new Project(projectName, profitMargin, totalCost, status, client);
        projectService.save(newProject);
        System.out.println("Project created successfully!");
        return newProject;
    }

    private void handleMaterialsAndLabors(Project project) {
        BigDecimal finalTotalCost = BigDecimal.ZERO;

        while (true) {
            System.out.println("Do you want to add materials or labor? (1 for Materials, 2 for Labor, 0 to finish)");
            int choice = getValidIntegerInput("Choose an option: ");

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

        project.setTotalCost(finalTotalCost);
        projectService.update(project);  // Save the updated project
    }

    private BigDecimal addMaterial(Project project, BigDecimal currentTotalCost) {
        String name = getValidStringInput("Enter material name: ");
        ComponentType type = ComponentType.Materiel;

        System.out.print("Do you want to specify a different component type? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Enter component type (e.g., MATERIAL, LABOR): ");
            type = ComponentType.valueOf(scanner.nextLine().toUpperCase());
        }

        BigDecimal unitCost = getValidBigDecimalInput("Enter unit cost: ");
        BigDecimal quantity = getValidBigDecimalInput("Enter quantity: ");
        BigDecimal vatRate = getValidBigDecimalInput("Enter VAT rate: ");
        BigDecimal transportCost = getValidBigDecimalInput("Enter transport cost: ");
        BigDecimal qualityCoefficient = getValidBigDecimalInput("Enter quality coefficient (0 to 1): ");

        Material material = new Material(name, type, unitCost, quantity, vatRate, project, transportCost, qualityCoefficient);
        materialService.save(material);

        BigDecimal totalMaterialCost = unitCost.multiply(quantity).add(transportCost);
        System.out.println("Material added successfully! Total Material Cost: " + totalMaterialCost);
        return currentTotalCost.add(totalMaterialCost);
    }

    private BigDecimal addLabor(Project project, BigDecimal currentTotalCost) {
        String name = getValidStringInput("Enter labor name: ");
        ComponentType type = ComponentType.Labor;

        System.out.print("Do you want to specify a different component type? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Enter component type (e.g., MATERIAL, LABOR): ");
            type = ComponentType.valueOf(scanner.nextLine().toUpperCase());
        }

        BigDecimal hourlyRate = getValidBigDecimalInput("Enter hourly rate: ");
        BigDecimal hoursWorked = getValidBigDecimalInput("Enter hours worked: ");
        BigDecimal vatRate = getValidBigDecimalInput("Enter VAT rate: ");
        BigDecimal productivityFactor = getValidBigDecimalInput("Enter productivity factor (0 to 1): ");

        Labor labor = new Labor(name, type, vatRate, project, hourlyRate, hoursWorked, productivityFactor);
        laborService.addLabor(labor);

        BigDecimal totalLaborCost = hourlyRate.multiply(hoursWorked);
        System.out.println("Labor added successfully! Total Labor Cost: " + totalLaborCost);
        return currentTotalCost.add(totalLaborCost);
    }

    private void registerQuote(Project project) {
        BigDecimal finalTotalCost = project.getTotalCost(); // Get final total cost

        System.out.print("Enter the issue date of the quote (format: YYYY-MM-DD): ");
        String issueDate = scanner.nextLine();
        System.out.print("Enter the validity date of the quote (format: YYYY-MM-DD): ");
        String validityDate = scanner.nextLine();

        Quote quote = new Quote(finalTotalCost, LocalDate.parse(issueDate), LocalDate.parse(validityDate), false, project);
        quoteService.saveQuote(quote);
        System.out.println("Quote saved successfully!");

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

        double marginRate = getValidBigDecimalInput("Enter the margin rate (as a decimal, e.g., 0.2 for 20%): ").doubleValue();

        double[] costDetails = projectService.calculateTotalCost(project, marginRate);

        System.out.println("--- Cost Calculation Result ---");
        System.out.println("Total Cost without VAT: " + costDetails[0]);
        System.out.println("Total Cost with VAT: " + costDetails[1]);
        System.out.println("Total Margin: " + costDetails[2]);
        System.out.println("Final Total Cost: " + costDetails[3]);
    }
}
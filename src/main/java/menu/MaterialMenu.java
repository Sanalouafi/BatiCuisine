package main.java.menu;

import main.java.entities.Material;
import main.java.entities.Project;
import main.java.enums.ComponentType;
import main.java.service.MaterialService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MaterialMenu {

    private final MaterialService materialService;
    private final Scanner scanner;

    public MaterialMenu() {
        this.materialService = new MaterialService();
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("=== Material Management Menu ===");
            System.out.println("1. Add Material");
            System.out.println("2. Update Material");
            System.out.println("3. Delete Material");
            System.out.println("4. View Material by ID");
            System.out.println("5. View All Materials");
            System.out.println("6. Calculate Total Cost for Project");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> addMaterial();
                case 2 -> updateMaterial();
                case 3 -> deleteMaterial();
                case 4 -> viewMaterialById();
                case 5 -> viewAllMaterials();
                case 6 -> calculateTotalCost();
                case 0 -> System.out.println("Exiting Material Menu.");
                default -> System.out.println("Invalid option, please try again.");
            }
        } while (choice != 0);
    }

    private void addMaterial() {
        Material material = getMaterialInput();
        materialService.save(material);
    }

    private void updateMaterial() {
        System.out.print("Enter the Material ID to update: ");
        Long id = scanner.nextLong();
        scanner.nextLine();  // Consume newline
        Optional<Material> materialOpt = materialService.findById(id);
        if (materialOpt.isPresent()) {
            Material material = getMaterialInput();
            material.setId(id);  // Ensure the ID stays the same
            materialService.update(material);
        } else {
            System.out.println("Material not found.");
        }
    }

    private void deleteMaterial() {
        System.out.print("Enter the Material ID to delete: ");
        Long id = scanner.nextLong();
        materialService.delete(id);
    }

    private void viewMaterialById() {
        System.out.print("Enter the Material ID to view: ");
        Long id = scanner.nextLong();
        Optional<Material> materialOpt = materialService.findById(id);
        materialOpt.ifPresentOrElse(
                material -> System.out.println("Material: " + material),
                () -> System.out.println("Material not found.")
        );
    }

    private void viewAllMaterials() {
        List<Material> materials = materialService.findAll();
        if (materials.isEmpty()) {
            System.out.println("No materials found.");
        } else {
            materials.forEach(material -> System.out.println("Material: " + material));
        }
    }

    private void calculateTotalCost() {
        System.out.print("Enter Project ID to calculate total cost: ");
        Long projectId = scanner.nextLong();
        // Assume that a valid Project object is passed (you can adjust based on how you handle Project)
        Project project = new Project();
        project.setId(projectId);

        double[] totalCosts = materialService.calculateTotalCost(project);
        System.out.printf("Total Cost without VAT: %.2f\n", totalCosts[0]);
        System.out.printf("Total Cost with VAT: %.2f\n", totalCosts[1]);
    }

    // Method to get user input for a Material object
    private Material getMaterialInput() {
        System.out.print("Enter Material name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Material type (e.g., WOOD, METAL): ");
        String type = scanner.nextLine().toUpperCase(); // Assume valid input
        System.out.print("Enter Unit cost: ");
        BigDecimal unitCost = scanner.nextBigDecimal();
        System.out.print("Enter Quantity: ");
        BigDecimal quantity = scanner.nextBigDecimal();
        System.out.print("Enter VAT rate: ");
        BigDecimal vatRate = scanner.nextBigDecimal();
        System.out.print("Enter Transport cost: ");
        BigDecimal transportCost = scanner.nextBigDecimal();
        System.out.print("Enter Quality coefficient (0 to 1): ");
        BigDecimal qualityCoefficient = scanner.nextBigDecimal();
        System.out.print("Enter Project ID: ");
        Long projectId = scanner.nextLong();
        scanner.nextLine();  // Consume newline

        Project project = new Project();
        project.setId(projectId);

        return new Material(name, ComponentType.valueOf(type), unitCost, quantity, vatRate, project, transportCost, qualityCoefficient);
    }
}

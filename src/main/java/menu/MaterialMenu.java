package main.java.menu;

import main.java.entities.Material;
import main.java.enums.ComponentType;
import main.java.exception.MaterialValidationException;
import main.java.service.LaborService;
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

    public void displayMenu() {
        while (true) {
            System.out.println("Material Management Menu:");
            System.out.println("1. Add Material");
            System.out.println("2. Update Material");
            System.out.println("3. Delete Material");
            System.out.println("4. View Material");
            System.out.println("5. View All Materials");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addMaterial();
                    break;
                case 2:
                    updateMaterial();
                    break;
                case 3:
                    deleteMaterial();
                    break;
                case 4:
                    viewMaterial();
                    break;
                case 5:
                    viewAllMaterials();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addMaterial() {
        try {
            System.out.print("Enter material name: ");
            String name = scanner.nextLine();

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

            Material material = new Material(name, ComponentType.Materiel, unitCost, quantity, vatRate, null, transportCost, qualityCoefficient);
            materialService.save(material);
            System.out.println("Material added successfully!");
        } catch (MaterialValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error adding material: " + e.getMessage());
        }
    }

    private void updateMaterial() {
        try {
            System.out.print("Enter material ID to update: ");
            Long id = scanner.nextLong();
            scanner.nextLine();

            Optional<Material> optionalMaterial = materialService.findById(id);
            if (optionalMaterial.isPresent()) {
                Material material = optionalMaterial.get();

                System.out.print("Enter new name (leave blank to keep current): ");
                String name = scanner.nextLine();
                if (!name.trim().isEmpty()) {
                    material.setName(name);
                }

                System.out.print("Enter new unit cost (leave blank to keep current): ");
                String unitCostInput = scanner.nextLine();
                if (!unitCostInput.trim().isEmpty()) {
                    material.setUnitCost(new BigDecimal(unitCostInput));
                }

                System.out.print("Enter new quantity (leave blank to keep current): ");
                String quantityInput = scanner.nextLine();
                if (!quantityInput.trim().isEmpty()) {
                    material.setQuantity(new BigDecimal(quantityInput));
                }

                System.out.print("Enter new VAT rate (leave blank to keep current): ");
                String vatRateInput = scanner.nextLine();
                if (!vatRateInput.trim().isEmpty()) {
                    material.setVatRate(new BigDecimal(vatRateInput));
                }

                System.out.print("Enter new transport cost (leave blank to keep current): ");
                String transportCostInput = scanner.nextLine();
                if (!transportCostInput.trim().isEmpty()) {
                    material.setTransportCost(new BigDecimal(transportCostInput));
                }

                System.out.print("Enter new quality coefficient (0 to 1, leave blank to keep current): ");
                String qualityCoefficientInput = scanner.nextLine();
                if (!qualityCoefficientInput.trim().isEmpty()) {
                    material.setQualityCoefficient(new BigDecimal(qualityCoefficientInput));
                }

                materialService.update(material);
                System.out.println("Material updated successfully!");
            } else {
                System.out.println("Material not found.");
            }
        } catch (MaterialValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating material: " + e.getMessage());
        }
    }

    private void deleteMaterial() {
        System.out.print("Enter material ID to delete: ");
        Long id = scanner.nextLong();
        materialService.delete(id);
        System.out.println("Material deleted successfully!");
    }

    private void viewMaterial() {
        System.out.print("Enter material ID to view: ");
        Long id = scanner.nextLong();
        Optional<Material> material = materialService.findById(id);
        material.ifPresentOrElse(
                m -> System.out.println("Material: " + m),
                () -> System.out.println("Material not found.")
        );
    }

    private void viewAllMaterials() {
        List<Material> materials = materialService.findAll();
        if (materials.isEmpty()) {
            System.out.println("No materials found.");
        } else {
            materials.forEach(System.out::println);
        }
    }
}

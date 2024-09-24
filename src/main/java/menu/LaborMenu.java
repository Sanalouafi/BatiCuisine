package main.java.menu;

import main.java.entities.Labor;
import main.java.entities.Project;
import main.java.enums.ComponentType;
import main.java.service.LaborService;
import main.java.service.ProjectService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class LaborMenu {

    private final LaborService laborService;
    private final Scanner scanner;
    private final ProjectService projectService;
    public LaborMenu() {
        this.laborService = new LaborService();
        this.scanner = new Scanner(System.in);
        this.projectService = new ProjectService();
    }

    public void displayMenu() {
        while (true) {
            System.out.println("1. Add Labor");
            System.out.println("2. Update Labor");
            System.out.println("3. Delete Labor");
            System.out.println("4. View Labor");
            System.out.println("5. View All Labors");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addLabor();
                    break;
                case 2:
                    updateLabor();
                    break;
                case 3:
                    deleteLabor();
                    break;
                case 4:
                    viewLabor();
                    break;
                case 5:
                    viewAllLabors();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addLabor() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter VAT rate: ");
        BigDecimal vatRate = new BigDecimal(scanner.nextLine());
        System.out.print("Enter hourly rate: ");
        BigDecimal hourlyRate = new BigDecimal(scanner.nextLine());
        System.out.print("Enter hours worked: ");
        BigDecimal hoursWorked = new BigDecimal(scanner.nextLine());
        System.out.print("Enter productivity factor (0 to 1): ");
        BigDecimal productivityFactor = new BigDecimal(scanner.nextLine());

        ComponentType type = ComponentType.Labor;
        Long projectId = projectService.getCurrentProjectId();

        Optional<Project> projectOptional = projectService.findById(projectId);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();

            Labor labor = new Labor(name, type, vatRate, project, hourlyRate, hoursWorked, productivityFactor);
            laborService.addLabor(labor);
        } else {
            System.out.println("No project found with ID: " + projectId);
        }
    }

    private void updateLabor() {
        System.out.print("Enter labor ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<Labor> optionalLabor = laborService.getLaborById(id);

        if (optionalLabor.isPresent()) {
            Labor labor = optionalLabor.get();

            System.out.print("Enter new name (current: " + labor.getName() + "): ");
            String name = scanner.nextLine();
            if (!name.trim().isEmpty()) {
                labor.setName(name);
            }

            System.out.print("Enter new VAT rate (current: " + labor.getVatRate() + "): ");
            String vatRateInput = scanner.nextLine();
            if (!vatRateInput.trim().isEmpty()) {
                try {
                    labor.setVatRate(new BigDecimal(vatRateInput));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid VAT rate input, keeping the current value.");
                }
            }

            System.out.print("Enter new hourly rate (current: " + labor.getHourlyRate() + "): ");
            String hourlyRateInput = scanner.nextLine();
            if (!hourlyRateInput.trim().isEmpty()) {
                try {
                    labor.setHourlyRate(new BigDecimal(hourlyRateInput));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid hourly rate input, keeping the current value.");
                }
            }

            System.out.print("Enter new hours worked (current: " + labor.getHoursWorked() + "): ");
            String hoursWorkedInput = scanner.nextLine();
            if (!hoursWorkedInput.trim().isEmpty()) {
                try {
                    labor.setHoursWorked(new BigDecimal(hoursWorkedInput));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid hours worked input, keeping the current value.");
                }
            }

            System.out.print("Enter new productivity factor (0 to 1, current: " + labor.getProductivityFactor() + "): ");
            String productivityFactorInput = scanner.nextLine();
            if (!productivityFactorInput.trim().isEmpty()) {
                try {
                    labor.setProductivityFactor(new BigDecimal(productivityFactorInput));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid productivity factor input, keeping the current value.");
                }
            }
            labor.setType(ComponentType.Labor);
            laborService.updateLabor(labor);
        } else {
            System.out.println("Labor not found.");
        }
    }


    private void deleteLabor() {
        System.out.print("Enter labor ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());
        laborService.deleteLabor(id);
    }

    private void viewLabor() {
        System.out.print("Enter labor ID to view: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<Labor> optionalLabor = laborService.getLaborById(id);
        optionalLabor.ifPresentOrElse(
                labor -> System.out.println(labor),
                () -> System.out.println("Labor not found.")
        );
    }

    private void viewAllLabors() {
        List<Labor> labors = laborService.getAllLabors();
        if (labors.isEmpty()) {
            System.out.println("No labors found.");
        } else {
            labors.forEach(System.out::println);
        }
    }
}

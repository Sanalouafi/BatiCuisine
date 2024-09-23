package main.java.menu;

import main.java.service.LaborService;
import main.java.service.MaterialService;

import java.util.Scanner;

public class MainMenu {

    private final Scanner scanner;

    public MainMenu() {

        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Client Management");
            System.out.println("2. Labor Management");
            System.out.println("3. Material Management");

            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    ClientMenu clientMenu = new ClientMenu();
                    clientMenu.displayMenu();
                    break;
                case 2:
                    LaborMenu laborMenu = new LaborMenu();
                    laborMenu.displayMenu();
                    break;
                case 3:
                    MaterialMenu materialMenu = new MaterialMenu();
                    materialMenu.displayMenu();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }
}

package main.java.menu;

import main.java.entities.Client;
import main.java.exception.ClientValidationException;
import main.java.service.ClientService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientMenu {

    private final ClientService clientService;
    private final Scanner scanner;

    public ClientMenu() {
        this.clientService = new ClientService();
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n--- Client Menu ---");
            System.out.println("1. Add Client");
            System.out.println("2. Update Client");
            System.out.println("3. Delete Client");
            System.out.println("4. View Client");
            System.out.println("5. View All Clients");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    addClient();
                    break;
                case 2:
                    updateClient();
                    break;
                case 3:
                    deleteClient();
                    break;
                case 4:
                    viewClient();
                    break;
                case 5:
                    viewAllClients();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void addClient() {
        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            System.out.print("Enter phone: ");
            String phone = scanner.nextLine();
            System.out.print("Is professional (true/false): ");
            boolean isProfessional = scanner.nextBoolean();
            scanner.nextLine(); // Consume newline

            Client client = new Client(name, address, phone, isProfessional);
            clientService.createClient(client);
            System.out.println("Client added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please try again.");
        }
    }

    private void updateClient() {
        System.out.print("Enter client ID to update: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); 

        Optional<Client> optionalClient = clientService.findClientById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            System.out.print("Enter new name (leave blank to keep current): ");
            String name = scanner.nextLine();
            if (!name.trim().isEmpty()) {
                client.setName(name);
            }

            System.out.print("Enter new address (leave blank to keep current): ");
            String address = scanner.nextLine();
            if (!address.trim().isEmpty()) {
                client.setAddress(address);
            }

            System.out.print("Enter new phone (leave blank to keep current): ");
            String phone = scanner.nextLine();
            if (!phone.trim().isEmpty()) {
                client.setPhone(phone);
            }

            System.out.print("Is professional (true/false, leave blank to keep current): ");
            String isProfessionalInput = scanner.nextLine();
            if (!isProfessionalInput.trim().isEmpty()) {
                client.setIsProfessional(Boolean.parseBoolean(isProfessionalInput));
            }

            try {
                clientService.updateClient(client);
                System.out.println("Client updated successfully.");
            } catch (ClientValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Client not found.");
        }
    }

    private void deleteClient() {
        System.out.print("Enter client ID to delete: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); 

        try {
            clientService.deleteClient(id);
            System.out.println("Client deleted successfully.");
        } catch (ClientValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewClient() {
        System.out.print("Enter client ID to view: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); 

        Optional<Client> optionalClient = clientService.findClientById(id);
        if (optionalClient.isPresent()) {
            System.out.println(optionalClient.get());
        } else {
            System.out.println("Client not found.");
        }
    }

    private void viewAllClients() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
        } else {
            clients.forEach(System.out::println);
        }
    }
}

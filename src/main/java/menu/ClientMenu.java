package main.java.menu;
import java.util.Scanner;
import java.util.Optional;
import main.java.entities.Client;
import main.java.service.ClientService;

public class ClientMenu {
    private final Scanner scanner;
    private final ClientService clientService;

    public ClientMenu() {
        scanner = new Scanner(System.in);
        clientService = new ClientService();
    }

    public void showMenu() {
        System.out.println("--- Client Management ---");
        System.out.println("Would you like to find an existing client or add a new one?");
        System.out.println("1. Find an existing client");
        System.out.println("2. Add a new client");
        System.out.print("Choose an option: ");

        int option = scanner.nextInt();
        scanner.nextLine(); 

        switch (option) {
            case 1:
                searchExistingClient();
                break;
            case 2:
                addNewClient();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void searchExistingClient() {
        System.out.print("Enter the client's name: ");
        String name = scanner.nextLine();

        Optional<Client> client = clientService.findClientByName(name);
        if (client.isPresent()) {
            System.out.println("Client found!");
            System.out.println("Name: " + client.get().getName());
            System.out.println("Address: " + client.get().getAddress());
            System.out.println("Phone: " + client.get().getPhone());
            System.out.println("Is Professional: " + (client.get().getIsProfessional() ? "Yes" : "No"));
        } else {
            System.out.println("Client not found.");
        }
    }

    private void addNewClient() {
        System.out.print("Enter the client's name: ");
        String name = scanner.nextLine();

        System.out.print("Enter the client's address: ");
        String address = scanner.nextLine();

        System.out.print("Enter the client's phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Is the client a professional? (true/false): ");
        boolean isProfessional = scanner.nextBoolean();
        scanner.nextLine(); 

        Client newClient = new Client();
        newClient.setName(name);
        newClient.setAddress(address);
        newClient.setPhone(phone);
        newClient.setIsProfessional(isProfessional);

        clientService.createClient(newClient);
        System.out.println("Client added successfully!");
    }
}

package main.java.menu;

import main.java.entities.Project;
import main.java.entities.Quote;
import main.java.enums.ProjectStatus;
import main.java.exception.QuoteValidationException;
import main.java.service.ProjectService;
import main.java.service.QuoteService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class QuoteMenu {

    private final QuoteService quoteService;
    private final ProjectService projectService;
    private final Scanner scanner;

    public QuoteMenu() {
        this.quoteService = new QuoteService();
        this.projectService = new ProjectService();
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n--- Quote Management Menu ---");
            System.out.println("1. Create a Quote");
            System.out.println("2. View Quote by ID");
            System.out.println("3. View All Quotes");
            System.out.println("4. Update a Quote");
            System.out.println("5. Delete a Quote");
            System.out.println("6. Accept a Quote");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    createQuote();
                    break;
                case 2:
                    viewQuoteById();
                    break;
                case 3:
                    viewAllQuotes();
                    break;
                case 4:
                    updateQuote();
                    break;
                case 5:
                    deleteQuote();
                    break;
                case 6:
                    acceptQuote();
                    break;
                case 0:
                    System.out.println("Exiting Quote Menu...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);
    }

    private void createQuote() {
        try {
            Quote quote = new Quote();

            System.out.print("Enter estimated amount: ");
            BigDecimal estimatedAmount = scanner.nextBigDecimal();
            quote.setEstimatedAmount(estimatedAmount);

            System.out.print("Enter issue date (YYYY-MM-DD): ");
            LocalDate issueDate = LocalDate.parse(scanner.next());
            quote.setIssueDate(issueDate);

            System.out.print("Enter validity date (YYYY-MM-DD): ");
            LocalDate validityDate = LocalDate.parse(scanner.next());
            quote.setValidityDate(validityDate);

            System.out.print("Is the quote accepted? (true/false): ");
            boolean isAccepted = scanner.nextBoolean();
            quote.setIsAccepted(isAccepted);

            System.out.print("Enter project ID: ");
            Long projectId = scanner.nextLong();
            Optional<Project> optionalProject = projectService.findById(projectId);
            if (optionalProject.isEmpty()) {
                System.out.println("Project with ID " + projectId + " does not exist.");
                return;
            }
            Project project = optionalProject.get();
            quote.setProject(project);
            quote.setProject(project);

            quoteService.saveQuote(quote);
            System.out.println("Quote created successfully!");

        } catch (QuoteValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error creating quote: " + e.getMessage());
        }
    }

    private void viewQuoteById() {
        try {
            System.out.print("Enter Quote ID: ");
            Long id = scanner.nextLong();
            Quote quote = quoteService.findQuoteById(id);
            System.out.println(quote);
        } catch (QuoteValidationException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error retrieving quote: " + e.getMessage());
        }
    }

    private void viewAllQuotes() {
        try {
            List<Quote> quotes = quoteService.findAllQuotes();
            if (quotes.isEmpty()) {
                System.out.println("No quotes found.");
            } else {
                for (Quote quote : quotes) {
                    System.out.println(quote);
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving quotes: " + e.getMessage());
        }
    }

    private void updateQuote() {
        try {
            System.out.print("Enter Quote ID: ");
            Long id = scanner.nextLong();

            Quote quote = quoteService.findQuoteById(id);

            System.out.print("Enter new estimated amount: ");
            BigDecimal estimatedAmount = scanner.nextBigDecimal();
            quote.setEstimatedAmount(estimatedAmount);

            System.out.print("Enter new issue date (YYYY-MM-DD): ");
            LocalDate issueDate = LocalDate.parse(scanner.next());
            quote.setIssueDate(issueDate);

            System.out.print("Enter new validity date (YYYY-MM-DD): ");
            LocalDate validityDate = LocalDate.parse(scanner.next());
            quote.setValidityDate(validityDate);

            System.out.print("Is the quote accepted? (true/false): ");
            boolean isAccepted = scanner.nextBoolean();
            quote.setIsAccepted(isAccepted);

            System.out.print("Enter new project ID: ");
            Long projectId = scanner.nextLong();
            Optional<Project> optionalProject = projectService.findById(projectId);
            if (optionalProject.isEmpty()) {
                System.out.println("Project with ID " + projectId + " does not exist.");
                return;
            }
            Project project = optionalProject.get();
            quote.setProject(project);
            quote.setProject(project);

            quoteService.updateQuote(quote);
            System.out.println("Quote updated successfully!");

        } catch (QuoteValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating quote: " + e.getMessage());
        }
    }

    private void deleteQuote() {
        try {
            System.out.print("Enter Quote ID: ");
            Long id = scanner.nextLong();
            quoteService.deleteQuoteById(id);
            System.out.println("Quote deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting quote: " + e.getMessage());
        }
    }

    private void acceptQuote() {
        try {
            System.out.print("Enter Quote ID: ");
            Long id = scanner.nextLong();
            quoteService.updateIsAcceptValue(id);
            System.out.println("Quote accepted and project status updated to CONFIRMED.");
        } catch (QuoteValidationException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error accepting quote: " + e.getMessage());
        }
    }
}

package main.java.service;

import main.java.entities.Quote;
import main.java.exception.QuoteValidationException;
import main.java.repository.QuoteRepository;
import main.java.repository.impl.QuoteRepositoryImpl;
import main.java.enums.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final ProjectService projectService;

    public QuoteService() {
        this.quoteRepository =new QuoteRepositoryImpl();
        this.projectService= new ProjectService();
    }

    public void saveQuote(Quote quote) {
        validateQuote(quote);
        quoteRepository.save(quote);
    }

    public Quote findQuoteById(Long id) {
        Quote quote = quoteRepository.findById(id);
        if (quote == null) {
            throw new QuoteValidationException("Quote with ID " + id + " not found.");
        }
        return quote;
    }

    public List<Quote> findAllQuotes() {
        return quoteRepository.findAll();
    }

    public void updateQuote(Quote quote) {
        if (quote.getId() == null) {
            throw new QuoteValidationException("Quote ID cannot be null for update.");
        }
        validateQuote(quote);
        quoteRepository.update(quote);
    }

    public void deleteQuoteById(Long id) {
        quoteRepository.deleteById(id);
    }
    public void updateIsAcceptValue(Long quoteId) {
        Quote quote = findQuoteById(quoteId);

        if (quote.getIsAccepted()) {
            throw new QuoteValidationException("Quote is already accepted.");
        }

        // Update the isAccepted field to true
        quote.setIsAccepted(true);
        quoteRepository.update(quote);

        Long projectId = quote.getProject().getId();
        projectService.updateProjectStatus(projectId, ProjectStatus.Confirmed);

        System.out.println("Quote acceptance updated and Project status set to CONFIRMED for Quote ID: " + quoteId);
    }

    private void validateQuote(Quote quote) {
        if (quote.getEstimatedAmount() == null || quote.getEstimatedAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new QuoteValidationException("Estimated amount cannot be null or negative.");
        }
        if (quote.getIssueDate() == null || quote.getIssueDate().isAfter(LocalDate.now())) {
            throw new QuoteValidationException("Issue date cannot be null or in the future.");
        }
        if (quote.getValidityDate() == null || quote.getValidityDate().isBefore(quote.getIssueDate())) {
            throw new QuoteValidationException("Validity date cannot be null or before the issue date.");
        }
        if (quote.getIsAccepted() == null) {
            throw new QuoteValidationException("Acceptance status cannot be null.");
        }
        if (quote.getProject() == null || quote.getProject().getId() == null) {
            throw new QuoteValidationException("Project cannot be null and must have a valid ID.");
        }
    }
}

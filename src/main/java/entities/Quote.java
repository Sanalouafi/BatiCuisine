package main.java.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Quote {

    private Long id;
    private BigDecimal estimatedAmount;
    private LocalDate issueDate;
    private LocalDate validityDate;
    private Boolean isAccepted;
    private Project project;

    // Constructors
    public Quote() {}

    public Quote(BigDecimal estimatedAmount, LocalDate issueDate, LocalDate validityDate, Boolean isAccepted, Project project) {
        this.estimatedAmount = estimatedAmount;
        this.issueDate = issueDate;
        this.validityDate = validityDate;
        this.isAccepted = isAccepted;
        this.project = project;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        if (estimatedAmount == null || estimatedAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Estimated amount cannot be negative");
        }
        this.estimatedAmount = estimatedAmount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        if (issueDate == null || issueDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Issue date cannot be null or in the future");
        }
        this.issueDate = issueDate;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(LocalDate validityDate) {
        if (validityDate == null || validityDate.isBefore(issueDate)) {
            throw new IllegalArgumentException("Validity date cannot be null or before issue date");
        }
        this.validityDate = validityDate;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        if (isAccepted == null) {
            throw new IllegalArgumentException("Acceptance status cannot be null");
        }
        this.isAccepted = isAccepted;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        this.project = project;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", estimatedAmount=" + estimatedAmount +
                ", issueDate=" + issueDate +
                ", validityDate=" + validityDate +
                ", isAccepted=" + isAccepted +
                ", project=" + project +
                '}';
    }
}


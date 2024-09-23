package main.java.entities;

import main.java.enums.ProjectStatus;

import java.math.BigDecimal;

public class Project {

    private Long id;
    private String name;
    private BigDecimal profitMargin;
    private BigDecimal totalCost;
    private ProjectStatus status;
    private Client client;

    // Constructors
    public Project() {}

    public Project(String name, BigDecimal profitMargin, BigDecimal totalCost, ProjectStatus status, Client client) {
        this.name = name;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.status = status;
        this.client = client;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public BigDecimal getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(BigDecimal profitMargin) {
        if (profitMargin == null || profitMargin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Profit margin cannot be negative");
        }
        this.profitMargin = profitMargin;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        if (totalCost == null || totalCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total cost cannot be negative");
        }
        this.totalCost = totalCost;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        this.client = client;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profitMargin=" + profitMargin +
                ", totalCost=" + totalCost +
                ", status=" + status +
                ", client=" + client +
                '}';
    }
}


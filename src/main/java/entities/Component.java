package main.java.entities;

import main.java.enums.ComponentType;

import java.math.BigDecimal;

public abstract class Component {

    private Long id;
    private String name;
    private ComponentType type;
    private BigDecimal vatRate;
    private Project project;

    // Constructors
    public Component() {}

    public Component(String name, ComponentType type, BigDecimal vatRate, Project project) {
        this.name = name;
        this.type = type;
        this.vatRate = vatRate;
        this.project = project;
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

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }



    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        if (vatRate == null || vatRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("VAT rate cannot be negative");
        }
        this.vatRate = vatRate;
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
        return "Component{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", vatRate=" + vatRate +
                ", project=" + project +
                '}';
    }
}



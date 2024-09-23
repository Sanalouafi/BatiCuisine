package main.java.entities;

import main.java.enums.ComponentType;

import java.math.BigDecimal;

public class Material extends Component {

    private BigDecimal transportCost;
    private BigDecimal qualityCoefficient;

    // Constructors
    public Material() {}

    public Material(String name, ComponentType type, BigDecimal unitCost, BigDecimal quantity, BigDecimal vatRate, Project project, BigDecimal transportCost, BigDecimal qualityCoefficient) {
        super(name, type, unitCost, quantity, vatRate, project);
        this.transportCost = transportCost;
        this.qualityCoefficient = qualityCoefficient;
    }

    // Getters and Setters
    public BigDecimal getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(BigDecimal transportCost) {
        if (transportCost == null || transportCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transport cost cannot be negative");
        }
        this.transportCost = transportCost;
    }

    public BigDecimal getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(BigDecimal qualityCoefficient) {
        if (qualityCoefficient == null || qualityCoefficient.compareTo(BigDecimal.ZERO) < 0 || qualityCoefficient.compareTo(BigDecimal.ONE) > 1) {
            throw new IllegalArgumentException("Quality coefficient must be between 0 and 1");
        }
        this.qualityCoefficient = qualityCoefficient;
    }

    @Override
    public String toString() {
        return "Material{" +
                "transportCost=" + transportCost +
                ", qualityCoefficient=" + qualityCoefficient +
                "} " + super.toString();
    }
}


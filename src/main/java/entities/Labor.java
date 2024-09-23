package main.java.entities;

import main.java.enums.ComponentType;

import java.math.BigDecimal;

public class Labor extends Component {

    private BigDecimal hourlyRate;
    private BigDecimal hoursWorked;
    private BigDecimal productivityFactor;

    // Constructors
    public Labor() {}

    public Labor(String name, ComponentType type, BigDecimal unitCost, BigDecimal quantity, BigDecimal vatRate, Project project, BigDecimal hourlyRate, BigDecimal hoursWorked, BigDecimal productivityFactor) {
        super(name, type, unitCost, quantity, vatRate, project);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.productivityFactor = productivityFactor;
    }

    // Getters and Setters
    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(BigDecimal hoursWorked) {
        if (hoursWorked == null || hoursWorked.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Hours worked cannot be negative");
        }
        this.hoursWorked = hoursWorked;
    }

    public BigDecimal getProductivityFactor() {
        return productivityFactor;
    }

    public void setProductivityFactor(BigDecimal productivityFactor) {
        if (productivityFactor == null || productivityFactor.compareTo(BigDecimal.ZERO) < 0 || productivityFactor.compareTo(BigDecimal.ONE) > 1) {
            throw new IllegalArgumentException("Productivity factor must be between 0 and 1");
        }
        this.productivityFactor = productivityFactor;
    }

    @Override
    public String toString() {
        return "Labor{" +
                "hourlyRate=" + hourlyRate +
                ", hoursWorked=" + hoursWorked +
                ", productivityFactor=" + productivityFactor +
                "} " + super.toString();
    }
}


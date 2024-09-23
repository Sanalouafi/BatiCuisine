package main.java.entities;
public class Client {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private Boolean isProfessional;

    // Constructors
    public Client() {}

    public Client(String name, String address, String phone, Boolean isProfessional) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.isProfessional = isProfessional;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        this.phone = phone;
    }

    public Boolean getIsProfessional() {
        return isProfessional;
    }

    public void setIsProfessional(Boolean isProfessional) {
        if (isProfessional == null) {
            throw new IllegalArgumentException("Professional status cannot be null");
        }
        this.isProfessional = isProfessional;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", isProfessional=" + isProfessional +
                '}';
    }
}


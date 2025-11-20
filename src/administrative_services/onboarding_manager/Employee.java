package administrative_services.onboarding_manager;

import java.time.LocalDate;

public class Employee {

    private final int id;
    private String name;
    private String email;
    private LocalDate startDate;
    private String status = "ACTIVE";

    public Employee(int id, String name, String email, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}

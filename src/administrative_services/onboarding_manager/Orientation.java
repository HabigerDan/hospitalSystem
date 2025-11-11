package administrative_services.onboarding_manager;

import java.time.LocalDateTime;
import java.util.UUID;

public class Orientation {

    private final UUID id;
    private final int employeeId;
    private LocalDateTime dateTime;
    private String location;
    private boolean placeholder;

    public Orientation(UUID id, int employeeId, LocalDateTime dateTime, String location, boolean placeholder) {
        this.id = id;
        this.employeeId = employeeId;
        this.dateTime = dateTime;
        this.location = location;
        this.placeholder = placeholder;
    }

    public UUID getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }

    public boolean isPlaceholder() {
        return placeholder;
    }

    public void setDateTime(LocalDateTime dt) {
        this.dateTime = dt;
    }

    public void setLocation(String loc) {
        this.location = loc;
    }

    public void setPlaceholder(boolean ph) {
        this.placeholder = ph;
    }
}

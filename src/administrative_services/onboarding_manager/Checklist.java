package administrative_services.onboarding_manager;
import java.util.UUID;

public class Checklist {

    private final UUID id;
    private final int employeeId;

    public Checklist(UUID id, int employeeId) {
        this.id = id;
        this.employeeId = employeeId;
    }

    public UUID getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}

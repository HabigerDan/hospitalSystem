package administrative_services.onboarding_manager;
import java.time.LocalDate;
import java.util.UUID;

public class ChecklistItem {

    private final UUID id;
    private final UUID checklistId;
    private final ItemCode code;
    private LocalDate dueDate;
    private LocalDate completedOn;

    public ChecklistItem(UUID id, UUID checklistId, ItemCode code, LocalDate dueDate) {
        this.id = id;
        this.checklistId = checklistId;
        this.code = code;
        this.dueDate = dueDate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getChecklistId() {
        return checklistId;
    }

    public ItemCode getCode() {
        return code;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void markDone(LocalDate when) {
        this.completedOn = when;
    }

    public boolean isDone() {
        return completedOn != null;
    }
}

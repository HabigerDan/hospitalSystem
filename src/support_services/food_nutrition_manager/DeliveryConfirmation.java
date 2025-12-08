package food_nutrition_manager;

import java.time.LocalDateTime;

public class DeliveryConfirmation {
    private int confirmationId;
    private LocalDateTime timestamp;
    private String status;

    public DeliveryConfirmation(int confirmationId, LocalDateTime timestamp, String status) {
        this.confirmationId = confirmationId;
        this.timestamp = timestamp;
        this.status = status;
    }

    public int getConfirmationId() {
        return confirmationId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }
}
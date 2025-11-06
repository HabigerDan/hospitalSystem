package src;
import java.time.LocalDateTime;

public interface confirmation {
    String getEquipmentID();
    LocalDateTime getTimestamp();
    status getStatus();
    String getMessage();
}
package waste_disposal_manager;

import java.time.LocalDateTime;

public class wasteRecord {

    private int recordID;
    private int wasteID;
    private String location;
    private int quantity;
    private LocalDateTime timestamp;

    public wasteRecord(int recordID, int wasteID, String location, int quantity) {
        this.recordID = recordID;
        this.wasteID = wasteID;
        this.location = location;
        this.quantity = quantity;
        this.timestamp = LocalDateTime.now();
    }

    public int getRecordID() {
        return recordID;
    }

    public int getWasteID() {
        return wasteID;
    }

    public String getLocation() {
        return location;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "WasteRecord{" +
                "recordID=" + recordID +
                ", wasteID=" + wasteID +
                ", location='" + location + '\'' +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
package waste_disposal_manager;

public class waste {

    private int wasteID;
    private wasteType wasteType;
    private int quantity;
    private String location;
    private String status;

    public waste(int wasteID, wasteType wasteType, int quantity, String location, String status) {
        this.wasteID = wasteID;
        this.wasteType = wasteType;
        this.quantity = quantity;
        this.location = location;
        this.status = status;
    }

    public int getWasteID() {
        return wasteID;
    }

    public void setWasteID(int wasteID) {
        this.wasteID = wasteID;
    }

    public wasteType getWasteType() {
        return wasteType;
    }

    public void setWasteType(wasteType wasteType) {
        this.wasteType = wasteType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return "Waste{" +
                "wasteID=" + wasteID +
                ", wasteType=" + (wasteType != null ? wasteType.getTypeName() : "null") +
                ", quantity=" + quantity +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
package supportServices.Waste_Disposal_Manager;

public class waste {

    private String waste;
    private String wasteID;
    private int quantity;

    public waste(String waste, String wasteID, int quantity) {
        this.waste = waste;
        this.wasteID = wasteID;
        this.quantity = quantity;
    }

    // Getters:
    public String getWaste() {
        return waste;
    }

    public String getWasteID() {
        return wasteID;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters:
    public void setWaste(String waste) {
        this.waste = waste;
    }

    public void setWasteID(String wasteID) {
        this.wasteID = wasteID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
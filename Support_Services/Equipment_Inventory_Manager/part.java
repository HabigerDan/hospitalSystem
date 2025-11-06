package Equipment_Inventory_Manager;

public class part {

    private String partID;
    private String name;
    private int quantity;

    public part(String partID, String name, int quantity) {
        this.partID = partID;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters:
    public String getPartID() {
        return partID;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters:
    public void setPartID(String partID) {
        this.partID = partID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
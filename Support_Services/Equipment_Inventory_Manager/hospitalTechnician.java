package Equipment_Inventory_Manager;

public class hospitalTechnician {

    private String technicianID;
    private String name;
    private String role;

    public hospitalTechnician(String technicianID, String name, String role) {
        this.technicianID = technicianID;
        this.name = name;
        this.role = role;
    }

    // Getters:
    public String getTechnicianID() {
        return technicianID;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    // Setters:
    public void setTechnicianID(String technicianID) {
        this.technicianID = technicianID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
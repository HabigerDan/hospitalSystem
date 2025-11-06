package supportServices.Waste_Disposal_Manager;

public class cleaningStaff {
    
    private String staffID;
    private String name;
    private String role;

    public cleaningStaff(String staffID, String name, String role) {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
    }

    // Getters:
    public String getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    // Setters:
    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void getRole(String role) {
        this.role = role;
    }
}
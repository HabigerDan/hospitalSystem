package Equipment_Inventory_Manager;
import java.util.List;

public class repairRecord {

    private String repairID;
    private String technicianID;
    private String date;
    private String description;
    private List<part> partsUsed; 

    public repairRecord(String repairID, String technicianID, String date, String description, List<part> partsUsed) {
        this.repairID = repairID;
        this.technicianID = technicianID;
        this.date = date;
        this.description = description;
        this.partsUsed = partsUsed;
    }

    // Getters:
    public String getRepairID() {
        return repairID;
    }

    public String getTechnicianID() {
        return technicianID;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public List<part> getPartsUsed() {
        return partsUsed;
    }

    // Setters:
    public void setRepairID(String repairID) {
        this.repairID = repairID;
    }

    public void setTechnicianID(String technicianID) {
        this.technicianID = technicianID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPartsUsed(List<part> partsUsed) {
        this.partsUsed = partsUsed;
    }

    // Helper methods:
    public void addPartUsed(part p) {
        partsUsed.add(p);
    }

    public void removePartUsed(part p) {
        partsUsed.remove(p);
    }
}
package Equipment_Inventory_Manager;
import java.util.List;

public class equipment {

    private String equipmentID;
    private String equipmentType;
    private String location;
    private List<repairRecord> repairRecords;

    public equipment(String equipmentID, String equipmentType, String location, List<repairRecord> repairRecords) {
        this.equipmentID = equipmentID;
        this.equipmentType = equipmentType;
        this.location = location;
        this.repairRecords = repairRecords;
    }

    // Getters:
    public String getEquipmentID() {
        return equipmentID;
    
    }
    public String getEquipmentType() {
        return equipmentType;
    }

    public String getLocation() {
        return location;
    }

    public List<repairRecord> getRepairRecords() {
        return repairRecords;
    }

    // Setters:
    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRepairRecords(List<repairRecord> repairRecords) {
        this.repairRecords = repairRecords;
    }
}
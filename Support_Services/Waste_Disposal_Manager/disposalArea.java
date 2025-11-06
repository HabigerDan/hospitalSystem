package supportServices.Waste_Disposal_Manager;

public class disposalArea {

    private String areaID;
    private String name;
    private String type;
    private int capacity;

    public disposalArea(String areaID, String name, String type, int capacity) {
        this.areaID = areaID;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
    }

    // Getters:
    public String getAreaID() {
        return areaID;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getCapactiy() {
        return capacity;
    }

    // Setters:
    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
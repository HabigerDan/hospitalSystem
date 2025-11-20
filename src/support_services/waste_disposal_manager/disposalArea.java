package waste_disposal_manager;

public class disposalArea {

    private int areaID;
    private String name;
    private int capacity;

    public disposalArea(int areaID, String name, int capacity) {
        this.areaID = areaID;
        this.name = name;
        this.capacity = capacity;
    }

    public int getAreaID() {
        return areaID;
    }

    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
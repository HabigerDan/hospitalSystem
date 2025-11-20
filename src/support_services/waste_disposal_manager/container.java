package waste_disposal_manager;

public class container {

    private int containerID;
    private String name;
    private int capacity;
    private int currentLoad;
    private String location;

    public container(int containerID, String name, int capacity, int currentLoad, String location) {
        this.containerID = containerID;
        this.name = name;
        this.capacity = capacity;
        this.currentLoad = currentLoad;
        this.location = location;
    }

    public int getContainerID() {
        return containerID;
    }

    public void setContainerID(int containerID) {
        this.containerID = containerID;
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

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isFull() {
        return currentLoad >= capacity;
    }

    @Override
    public String toString() {
        return "Container{" +
                "containerID=" + containerID +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", currentLoad=" + currentLoad +
                ", location='" + location + '\'' +
                '}';
    }
}
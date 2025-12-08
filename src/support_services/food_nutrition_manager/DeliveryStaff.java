package food_nutrition_manager;

public class DeliveryStaff {
    private int staffId;
    private String name;
    private boolean available;

    public DeliveryStaff(int staffId, String name, boolean available) {
        this.staffId = staffId;
        this.name = name;
        this.available = available;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
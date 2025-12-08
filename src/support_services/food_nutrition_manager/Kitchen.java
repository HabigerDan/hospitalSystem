package food_nutrition_manager;

public class Kitchen {
    private int kitchenId;
    private String status;

    public Kitchen(int kitchenId, String status) {
        this.kitchenId = kitchenId;
        this.status = status;
    }

    public int getKitchenId() {
        return kitchenId;
    }

    public String getStatus() {
        return status;
    }
}
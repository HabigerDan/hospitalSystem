package food_nutrition_manager;

public class Meal {
    private int mealId;
    private String name;
    private String nutritionInfo;

    public Meal(int mealId, String name, String nutritionInfo) {
        this.mealId = mealId;
        this.name = name;
        this.nutritionInfo = nutritionInfo;
    }

    public int getMealId() {
        return mealId;
    }
    public String getName() {
        return name;
    }

    public String getNutritionInfo() {
        return nutritionInfo;
    }
}
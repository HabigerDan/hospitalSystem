package nursing_services.schedule;

import java.util.HashMap;
import java.util.Map;

public class Pharmacy {
    Map<String, Integer> stock = new HashMap<>();

    public boolean given(String medicationID) {
        int quantity = stock.getOrDefault(medicationID, 0);
        if (quantity <= 0) return false;
        stock.put(medicationID, quantity - 1);
        return true;
    }

    public void restock(String medicationID, int amount) {
        stock.put(medicationID, stock.getOrDefault(medicationID, 0) + amount);
    }
}

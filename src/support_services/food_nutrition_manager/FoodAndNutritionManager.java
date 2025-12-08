package food_nutrition_manager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class FoodAndNutritionManager {

    private static int orderCounter = 5000;
    private static int confirmationCounter = 6000;

    private int managerId;
    private String name;

    private static final String DATA_PATH = "src/support_services/food_nutrition_manager/data/";

    private static void writeToFile(String fileName, String data) {
        try {
            File dir = new File(DATA_PATH);
            if (!dir.exists()) dir.mkdirs();

            FileWriter writer = new FileWriter(DATA_PATH + fileName, true);
            writer.write(data + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("File write error.");
        }
    }

    private static String findManagerName(int managerId) {
        File file = new File(DATA_PATH + "managers.txt");
        if (!file.exists()) return null;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.contains("ManagerID: " + managerId)) {
                    return line.substring(line.indexOf("Name: ") + 6);
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void createMealOrder(Scanner scanner) {
        System.out.print("Patient ID: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Patient Name: ");
        String patientName = scanner.nextLine();

        System.out.print("Room Number: ");
        String room = scanner.nextLine();

        System.out.print("Meal Name: ");
        String mealName = scanner.nextLine();

        System.out.print("Delivery Staff Name: ");
        String staffName = scanner.nextLine();

        Patient patient = new Patient(patientId, patientName, room);
        Meal meal = new Meal(1, mealName, "N/A");
        Kitchen kitchen = new Kitchen(1, "Available");
        DeliveryStaff staff = new DeliveryStaff(1, staffName, true);

        MealOrder order = new MealOrder(++orderCounter,
                LocalDateTime.now().toString(), "Scheduled");
        order.setPatient(patient);
        order.setMeal(meal);
        order.setKitchen(kitchen);
        order.assignDeliveryStaff(staff);

        writeToFile("orders.txt",
            "OrderID: " + order.getOrderId() +
            ", PatientID: " + patientId +
            ", Meal: " + mealName +
            ", Staff: " + staffName +
            ", Status: Scheduled");

        System.out.println("Meal order created.");
    }

    private void confirmDelivery(Scanner scanner) {
        System.out.print("Enter Order ID to confirm delivery: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        File file = new File(DATA_PATH + "orders.txt");
        if (!file.exists()) {
            System.out.println("No orders found.");
            return;
        }

        StringBuilder updated = new StringBuilder();
        boolean found = false;

        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.contains("OrderID: " + orderId)) {
                    line = line.replace("Status: Scheduled", "Status: Delivered");
                    found = true;
                }
                updated.append(line).append("\n");
            }
        } catch (Exception ignored) {}

        if (!found) {
            System.out.println("Order not found.");
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(updated.toString());
        } catch (IOException e) {
            System.out.println("Error updating order.");
        return;
    }

    DeliveryConfirmation confirmation =
            new DeliveryConfirmation(++confirmationCounter,
                    LocalDateTime.now(), "Delivered");

    writeToFile("confirmations.txt",
            "ConfirmationID: " + confirmation.getConfirmationId() +
            ", OrderID: " + orderId +
            ", Time: " + confirmation.getTimestamp());

    System.out.println("Delivery confirmed and order updated.");
    }

    private void viewOrders() {
        File file = new File(DATA_PATH + "orders.txt");
        if (!file.exists()) {
            System.out.println("No orders found.");
            return;
        }
        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                System.out.println(s.nextLine());
            }
        } catch (Exception ignored) {}
    }

    private void showMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Create Meal Order");
        System.out.println("2. Confirm Meal Delivery");
        System.out.println("3. View All Orders");
        System.out.println("4. Update Kitchen Status");
        System.out.println("5. View Kitchen Status");
        System.out.println("6. Logout");
        System.out.print("Choice: ");
    }

    private void runCLI() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Manager ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String existingName = findManagerName(id);

        if (existingName != null) {
            this.managerId = id;
            this.name = existingName;
            System.out.println("Welcome back, " + name);
        } else {
            System.out.print("Enter Manager Name: ");
            this.name = scanner.nextLine();
            this.managerId = id;

            writeToFile("managers.txt",
                    "ManagerID: " + managerId + ", Name: " + name);
            System.out.println("Welcome, " + name);
        }

        boolean running = true;

        while (running) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createMealOrder(scanner);
                case 2 -> confirmDelivery(scanner);
                case 3 -> viewOrders();
                case 4 -> updateKitchenStatus(scanner);
                case 5 -> viewKitchenStatus();
                case 6 -> {
                    running = false;
                    System.out.println("Logged out.");
                }
                default -> System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }

    private void updateKitchenStatus(Scanner scanner) {
        System.out.print("Enter Kitchen ID: ");
        int kitchenId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new kitchen status (Available / Busy / Closed): ");
        String status = scanner.nextLine();

        Kitchen kitchen = new Kitchen(kitchenId, status);

        writeToFile("kitchens.txt",
                "KitchenID: " + kitchenId +
                ", Status: " + status +
                ", UpdatedBy: Manager " + managerId);

        System.out.println("Kitchen status updated.");
    }

    private void viewKitchenStatus() {
        File file = new File(DATA_PATH + "kitchens.txt");
        if (!file.exists()) {
            System.out.println("No kitchen status records found.");
            return;
        }

        try (Scanner s = new Scanner(file)) {
            System.out.println("\n--- Kitchen Status ---");
            while (s.hasNextLine()) {
                System.out.println(s.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error reading kitchen data.");
        }
    }

    public static void main(String[] args) {
        new FoodAndNutritionManager().runCLI();
    }
}
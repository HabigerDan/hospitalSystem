package support_services.equipment_inventory_manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class equipmentInventoryManager {

    private String systemID;
    private static Scanner scanner = new Scanner(System.in);
    private static hospitalTechnician hospitalTech;
    private static inventory inventory = new inventory(new ArrayList<>());
    private static List<equipment> equipmentList = new ArrayList<>();

    private static final String DATA_FOLDER = "src/support_services/equipment_inventory_manager/data/";
    private static final String TECH_FILE = DATA_FOLDER + "technicians.txt";
    private static final String INVENTORY_FILE = DATA_FOLDER + "inventory.txt";
    private static final String EQUIPMENT_FILE = DATA_FOLDER + "equipment.txt";


    public static void main(String[] args) {
        new java.io.File(DATA_FOLDER).mkdirs();

        loadTechnicians();
        loadInventory();
        loadEquipment();

        System.out.println("\n\n\nAre you a Hospital Technician? Y(yes) OR N(no):");
        String loginTechnician = scanner.next();

        if (loginTechnician.equalsIgnoreCase("Y")) {
            loginTechnician();
            setTechnicianDetails();
            displayTechnicianDetails();
            mainMenu();
        } else {
            System.out.println("\n\n\n--------------------Logging Out--------------------");
        }

        saveInventory();
        saveEquipment();
        saveTechnicians();
    }

    public equipmentInventoryManager(String systemID) {
        this.systemID = systemID;
    }

    public static void loginTechnician() {
        System.out.println("\n\n--------------------Welcome Hospital Technician--------------------");
    }

    public static void setTechnicianDetails() {

        System.out.println("\nEnter your Technician ID:");
        String id = scanner.next();

        // Check if tech already exists
        hospitalTech = findTechnicianByID(id);

        if (hospitalTech != null) {
            System.out.println("Technician recognized. Logging in...");
            return;
        }

        // If new technician, register them
        System.out.println("\nEnter your Technician Name:");
        String name = scanner.next();
        System.out.println("\nEnter your Technician Role:");
        String role = scanner.next();
        hospitalTech = new hospitalTechnician(id, name, role);

        // Add to technician file
        saveTechnicians();
    }

    public static void displayTechnicianDetails() {
        System.out.println("\n-----Technician Information-----");
        System.out.println("Technician ID: " + hospitalTech.getTechnicianID());
        System.out.println("Technician Name: " + hospitalTech.getName());
        System.out.println("Technician Role: " + hospitalTech.getRole());
        System.out.println("--------------------------------\n");
    }

    public static void mainMenu() {
        while (true) {
            System.out.println("Select an option:");
            System.out.println("1 - Add Part to Inventory");
            System.out.println("2 - View Inventory");
            System.out.println("3 - Add Equipment");
            System.out.println("4 - View Equipment");
            System.out.println("5 - Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addPartToInventory();
                case 2 -> displayInventory();
                case 3 -> addEquipment();
                case 4 -> displayEquipment();
                case 5 -> {
                    saveInventory();
                    saveEquipment();
                    saveTechnicians();
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void addPartToInventory() {
        System.out.println("Enter Part ID:");
        String id = scanner.nextLine();

        // Check for duplicate part
        for (part p : inventory.getParts()) {
            if (p.getPartID().equals(id)) {
                System.out.println("Part already exists. Updating quantity...");
                p.setQuantity(p.getQuantity() + 1);
                saveInventory();
                return;
            }
        }

        System.out.println("Enter Part Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Quantity:");
        int qty = scanner.nextInt();
        scanner.nextLine();

        part newPart = new part(id, name, qty);
        inventory.addPart(newPart);
        saveInventory();
        System.out.println("Part added to inventory.\n");
    }

    public static void displayInventory() {
        System.out.println("\n---Inventory Parts---");
        for (part p : inventory.getParts()) {
            System.out.println("ID: " + p.getPartID() + ", Name: " + p.getName() + ", Quantity: " + p.getQuantity());
        }
        System.out.println("--------------------\n");
    }

    public static void addEquipment() {
        System.out.println("Enter Equipment ID:");
        String id = scanner.nextLine();

        for (equipment e : equipmentList) {
            if (e.getEquipmentID().equals(id)) {
                System.out.println("Equipment already exists. Operation canceled.\n");
                return;
            }
        }

        System.out.println("Enter Equipment Type:");
        String type = scanner.nextLine();
        System.out.println("Enter Equipment Location:");
        String location = scanner.nextLine();

        equipment newEquipment = new equipment(id, type, location, new ArrayList<>());
        equipmentList.add(newEquipment);
        saveEquipment();
        System.out.println("Equipment added.\n");
    }

    public static void displayEquipment() {
        System.out.println("\n---Equipment List---");
        for (equipment e : equipmentList) {
            System.out.println("ID: " + e.getEquipmentID() + ", Type: " + e.getEquipmentType() + ", Location: " + e.getLocation());
        }
        System.out.println("--------------------\n");
    }

    // ---------- FILE SAVE / LOAD METHODS -------------

    public static void loadInventory() {
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                inventory.addPart(new part(data[0], data[1], Integer.parseInt(data[2])));
            }
        } catch (Exception ignored) {}
    }

    public static void saveInventory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            for (part p : inventory.getParts()) {
                writer.println(p.getPartID() + "," + p.getName() + "," + p.getQuantity());
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    public static void loadEquipment() {
        try (BufferedReader br = new BufferedReader(new FileReader(EQUIPMENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                equipmentList.add(new equipment(data[0], data[1], data[2], new ArrayList<>()));
            }
        } catch (Exception ignored) {}
    }

    public static void saveEquipment() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EQUIPMENT_FILE))) {
            for (equipment e : equipmentList) {
                writer.println(e.getEquipmentID() + "," + e.getEquipmentType() + "," + e.getLocation());
            }
        } catch (IOException e) {
            System.out.println("Error saving equipment.");
        }
    }

    public static void loadTechnicians() {
        try (BufferedReader br = new BufferedReader(new FileReader(TECH_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                new hospitalTechnician(data[0], data[1], data[2]);
            }
        } catch (Exception ignored) {}
    }

    public static void saveTechnicians() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TECH_FILE, true))) {
            writer.println(hospitalTech.getTechnicianID() + "," + hospitalTech.getName() + "," + hospitalTech.getRole());
        } catch (IOException e) {
            System.out.println("Error saving technician.");
        }
    }

    public static hospitalTechnician findTechnicianByID(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(TECH_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id)) {
                    return new hospitalTechnician(data[0], data[1], data[2]);
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
}
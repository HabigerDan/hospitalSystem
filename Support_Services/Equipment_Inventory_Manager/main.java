package Equipment_Inventory_Manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static hospitalTechnician hospitalTech;
    private static inventory inventory = new inventory(new ArrayList<>());
    private static List<equipment> equipmentList = new ArrayList<>();

    public static void main(String [] args) {
        System.out.println("\n\n\nAre you a Hospital Technician? Y(yes) OR N(no):");
        String loginTechnician = scanner.next();

        if (loginTechnician.equals("Y") || loginTechnician.equals("y")) {
            loginTechnician();
            setTechnicianDetails();
            displayTechnicianDetails();
            mainMenu();
        } else {
            System.out.println("\n\n\n--------------------Logging Out--------------------");
        }
    }

    // Logs in as technician
    public static void loginTechnician() {
        System.out.println("\n\n\n\n\n\n--------------------Welcome Hospital Technician--------------------");
    }

    // Sets technician details
    public static void setTechnicianDetails() {
        System.out.println("\n\n\nEnter your Technician ID:");
        String setTechnicianID = scanner.next();
        System.out.println("\n\n\nEnter your Technician Name:");
        String setTechnicianName = scanner.next();
        System.out.println("\n\n\nEnter your Technician Role:");
        String setTechnicianRole = scanner.next();
        hospitalTech = new hospitalTechnician(setTechnicianID, setTechnicianName, setTechnicianRole);
    }

    // Displays technician details
    public static void displayTechnicianDetails() {
        System.out.println("\n\n\n-----Technician Information-----");
        System.out.println("Technician ID: " + hospitalTech.getTechnicianID());
        System.out.println("Technician Name: " + hospitalTech.getName());
        System.out.println("Technician Role: " + hospitalTech.getRole());
        System.out.println("--------------------------------\n\n\n");
    }

    // Main menu ffor different action technician can take
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
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Add a part to inventory
    public static void addPartToInventory() {
        System.out.println("Enter Part ID:");
        String id = scanner.nextLine();
        System.out.println("Enter Part Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Quantity:");
        int qty = scanner.nextInt();
        scanner.nextLine();

        part newPart = new part(id, name, qty);
        inventory.addPart(newPart);
        System.out.println("Part added to inventory.\n");
    }

    // Display all parts
    public static void displayInventory() {
        System.out.println("\n---Inventory Parts---");
        for (part p : inventory.getParts()) {
            System.out.println("ID: " + p.getPartID() + ", Name: " + p.getName() + ", Quantity: " + p.getQuantity());
        }
        System.out.println("--------------------\n");
    }

    // Add new equipment
    public static void addEquipment() {
        System.out.println("Enter Equipment ID:");
        String id = scanner.nextLine();
        System.out.println("Enter Equipment Type:");
        String type = scanner.nextLine();
        System.out.println("Enter Equipment Location:");
        String location = scanner.nextLine();

        equipment newEquipment = new equipment(id, type, location, new ArrayList<>());
        equipmentList.add(newEquipment);
        System.out.println("Equipment added.\n");
    }

    // Display all equipment
    public static void displayEquipment() {
        System.out.println("\n---Equipment List---");
        for (equipment e : equipmentList) {
            System.out.println("ID: " + e.getEquipmentID() + ", Type: " + e.getEquipmentType() + ", Location: " + e.getLocation());
        }
        System.out.println("--------------------\n");
    }
}
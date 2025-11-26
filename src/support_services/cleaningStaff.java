package support_services;

import java.io.*;
import java.util.*;
import room_cleanliness_manager.roomCleanlinessManager;
import waste_disposal_manager.wasteDisposalManager;

public class cleaningStaff {

    private String username;
    private String role; // e.g., "Room Cleaner", "Waste Handler", etc.

    private static final String DATA_FOLDER = "data/";
    private static final String STAFF_FILE = DATA_FOLDER + "cleaning_staff.txt";

    public cleaningStaff(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return username + "," + role;
    }

    public void save() {
        try {
            File folder = new File(DATA_FOLDER);
            if (!folder.exists()) folder.mkdirs();

            try (FileWriter fw = new FileWriter(STAFF_FILE, true)) {
                fw.write(this.toString() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<cleaningStaff> loadStaff() {
        List<cleaningStaff> list = new ArrayList<>();
        try {
            File folder = new File(DATA_FOLDER);
            if (!folder.exists()) folder.mkdirs();

            File file = new File(STAFF_FILE);
            if (!file.exists()) return list;

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    list.add(new cleaningStaff(parts[0], parts[1]));
                }
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static cleaningStaff getStaffByUsername(String username) {
        for (cleaningStaff cs : loadStaff()) {
            if (cs.getUsername().equalsIgnoreCase(username)) {
                return cs;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Cleaning Staff CLI ===");
        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        cleaningStaff staff = cleaningStaff.getStaffByUsername(username);

        if (staff == null) {
            System.out.print("Username not found. Enter your role (e.g., Room Cleaner, Waste Handler): ");
            String role = sc.nextLine();
            staff = new cleaningStaff(username, role);
            staff.save();
            System.out.println("Staff added successfully!");
        }

        System.out.println("Welcome " + staff.getUsername() + " (" + staff.getRole() + ")");

        System.out.println("Choose module:");
        System.out.println("1. Room Cleanliness Manager");
        System.out.println("2. Waste Disposal Manager");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        if (choice == 1) {
            roomCleanlinessManager.main(null);
        } else if (choice == 2) {
            wasteDisposalManager.main(null);
        } else {
            System.out.println("Invalid choice. Exiting.");
        }
    }
}
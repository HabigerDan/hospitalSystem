package room_cleanliness_manager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class roomCleanlinessManager {

    private static final String DATA_FOLDER = "room_cleanliness_manager/data/";
    private static final String ROOMS_FILE = DATA_FOLDER + "rooms.txt";
    private static final String SUPPLIES_FILE = DATA_FOLDER + "supplies.txt";

    public static void main(String[] args) {
        createDataFolder();

        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("1. View Room Schedule");
            System.out.println("2. Clean Room");
            System.out.println("3. Update Supply Quantity");
            System.out.println("4. Add Room");
            System.out.println("5. Check Supply Quantity");
            System.out.println("6. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    List<room> rooms = getRoomSchedule();
                    for (room r : rooms) {
                        System.out.println("Room: " + r.getRoomID() + " | Status: " + r.getStatus() +
                                " | Last Cleaned: " + r.getLastCleaned());
                    }
                    break;

                case 2:
                    System.out.print("Enter Room ID: ");
                    String roomId = sc.nextLine();
                    cleanRoom(roomId);
                    break;

                case 3:
                    System.out.print("Supply ID: ");
                    String sId = sc.nextLine();
                    System.out.print("Name: ");
                    String sName = sc.nextLine();
                    System.out.print("Current Quantity: ");
                    int curQty = sc.nextInt();
                    System.out.print("Minimum Required: ");
                    int minReq = sc.nextInt();
                    System.out.print("Add/Subtract Quantity (±): ");
                    int update = sc.nextInt();

                    supply s = new supply(sId, sName, curQty, minReq);
                    updateSuppliesStatus(s, update);
                    break;

                case 4:
                    addRoom();
                    break;

                case 5:
                    checkSupplyQuantity();
                    break;

                case 6:
                    return;
                }
            }
    }

    private static void createDataFolder() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static List<room> getRoomSchedule() {
        List<room> rooms = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ROOMS_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String rID = parts[0];
                String rStatus = parts[1];
                String bID = parts[2];
                String bStatus = parts[3];
                LocalDateTime last = LocalDateTime.parse(parts[4]);

                bed b = new bed(bID, bStatus);
                room r = new room(rID, rStatus, b, last);

                rooms.add(r);
            }

        } catch (Exception e) {
            System.out.println("No rooms file found. Initializing empty schedule.");
        }

        return rooms;
    }

    public static void cleanRoom(String roomId) {
        List<room> rooms = getRoomSchedule();
        boolean found = false;

        for (room r : rooms) {
            if (r.getRoomID().equals(roomId)) {
                r.updateStatus("Clean");
                r.updateLastCleaned(LocalDateTime.now());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Room not found.");
            return;
        }

        updateRoomSchedule(rooms);
        System.out.println("Room " + roomId + " cleaned and schedule updated!");
    }

    public static void updateRoomSchedule(List<room> updatedRooms) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOMS_FILE))) {

            for (room r : updatedRooms) {
                pw.println(
                    r.getRoomID() + "," +
                    r.getStatus() + "," +
                    r.getBed().getBedID() + "," +
                    r.getBed().getStatus() + "," +
                    r.getLastCleaned()
                );
            }

            System.out.println("Room schedule updated successfully!");

        } catch (Exception e) {
            System.out.println("Error updating room schedule: " + e.getMessage());
        }
    }

    public static void addRoom() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter new Room ID: ");
        String roomId = sc.nextLine();

        System.out.print("Enter Bed ID: ");
        String bedId = sc.nextLine();

        bed b = new bed(bedId, "Dirty");
        room newRoom = new room(roomId, "Dirty", b, LocalDateTime.now());

        List<room> rooms = getRoomSchedule();
        rooms.add(newRoom);

        updateRoomSchedule(rooms);

        System.out.println("Room " + roomId + " added and marked DIRTY.");
    }

    public static void checkSupplyQuantity() {
        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIES_FILE))) {

            String line;
            System.out.println("\n--- SUPPLY STATUS ---");

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String id = parts[0];
                String name = parts[1];
                int qty = Integer.parseInt(parts[2]);
                int min = Integer.parseInt(parts[3]);

                System.out.println("Supply: " + name +
                                " | ID: " + id +
                                " | Quantity: " + qty +
                                " | Minimum Required: " + min +
                                (qty < min ? "  <-- LOW!" : ""));
            }

            System.out.println("----------------------\n");

        } catch (Exception e) {
            System.out.println("No supplies found.");
        }
    }

    public static void updateSuppliesStatus(supply s, int quantity) {
        int updated = s.getCurrentQuantity() + quantity;

        String result = s.getSupplyID() + "," +
                        s.getName() + "," +
                        updated + "," +
                        s.getMinimumRequired();

        try (PrintWriter pw = new PrintWriter(new FileWriter(SUPPLIES_FILE, true))) {
            pw.println(result);
        } catch (Exception e) {
            System.out.println("Error saving supply.");
        }

        System.out.println("Supply updated! New quantity: " + updated +
                (updated < s.getMinimumRequired() ? " (LOW!)" : ""));
    }
}
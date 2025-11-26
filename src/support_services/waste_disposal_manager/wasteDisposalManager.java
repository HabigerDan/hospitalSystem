package waste_disposal_manager;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class wasteDisposalManager {

    private ArrayList<waste> pendingWaste = new ArrayList<>();
    private ArrayList<container> containers = new ArrayList<>();

    private static final String BASE_PATH =
            "waste_disposal_manager/data/";

    private static final String PENDING_FILE = BASE_PATH + "pending_waste.txt";
    private static final String DISPOSAL_LOG = BASE_PATH + "disposal_log.txt";
    private static final String SYSTEM_LOG = BASE_PATH + "system_log.txt";
    private static final String CONTAINERS_FILE = BASE_PATH + "containers.txt";
    private static final String WASTE_RECORDS_FILE = BASE_PATH + "waste_records.txt";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void main(String[] args) {
        wasteDisposalManager manager = new wasteDisposalManager();
        manager.loadPendingWaste();
        manager.loadContainers();

        Scanner sc = new Scanner(System.in);

        mainLoop:
        while (true) {
            System.out.println("\n--- Waste Disposal Manager ---");
            System.out.println("1. Add Waste");
            System.out.println("2. View Pending Waste");
            System.out.println("3. Transport Waste (to Disposal Area)");
            System.out.println("4. Update Waste Status (mark all Processed)");
            System.out.println("5. Identify Waste by Location");
            System.out.println("6. Add Container");
            System.out.println("7. View Containers");
            System.out.println("8. View Disposal Records");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            String input = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1:
                    manager.menuAddWaste(sc);
                    break;
                case 2:
                    manager.viewPendingWaste();
                    break;
                case 3:
                    manager.menuTransportWaste(sc);
                    break;
                case 4:
                    manager.updateWasteStatus();
                    break;
                case 5:
                    System.out.print("Enter location to search: ");
                    String loc = sc.nextLine();
                    System.out.println(manager.identifyWaste(loc));
                    break;
                case 6:
                    manager.menuAddContainer(sc);
                    break;
                case 7:
                    manager.viewContainers();
                    break;
                case 8:
                    manager.viewDisposalRecords();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break mainLoop;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }

    private void menuAddWaste(Scanner sc) {
        try {
            System.out.print("Enter waste ID (int): ");
            int id = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter waste type name: ");
            String typeName = sc.nextLine().trim();

            System.out.print("Enter waste category: ");
            String category = sc.nextLine().trim();

            System.out.print("Enter quantity (int): ");
            int qty = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter location: ");
            String location = sc.nextLine().trim();

            wasteType wt = new wasteType(typeName, category);
            collectWaste(wt, qty, id, location);
        } catch (Exception e) {
            System.out.println("Error adding waste: " + e.getMessage());
        }
    }

    private void menuTransportWaste(Scanner sc) {
        if (pendingWaste.isEmpty()) {
            System.out.println("No pending waste to transport.");
            return;
        }

        try {
            System.out.print("Enter disposal area ID (int): ");
            int areaID = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter disposal area name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter area capacity (int): ");
            int cap = Integer.parseInt(sc.nextLine().trim());

            disposalArea area = new disposalArea(areaID, name, cap);
            transportWaste(area);
        } catch (Exception e) {
            System.out.println("Error transporting waste: " + e.getMessage());
        }
    }

    private void menuAddContainer(Scanner sc) {
        try {
            System.out.print("Enter container ID (int): ");
            int cid = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter container name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter capacity (int): ");
            int capacity = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter current load (int): ");
            int currentLoad = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter location: ");
            String location = sc.nextLine().trim();

            container c = new container(cid, name, capacity, currentLoad, location);
            containers.add(c);
            saveContainers();
            logToSystem("Added container " + cid + " (" + name + ") at " + location);
            System.out.println("Container added and saved.");
        } catch (Exception e) {
            System.out.println("Error adding container: " + e.getMessage());
        }
    }


    public String identifyWaste(String location) {
        for (waste w : pendingWaste) {
            if (w.getLocation().equalsIgnoreCase(location)) {
                return "Found: " + w.toString();
            }
        }
        return "No waste found at that location.";
    }

    public void collectWaste(wasteType wasteType, int quantity, int id, String location) {
        waste w = new waste(id, wasteType, quantity, location, "Pending");
        pendingWaste.add(w);
        savePendingWaste();
        logToSystem("Added waste ID " + id + " at " + location);
        System.out.println("Waste added and saved to file.");
    }

    public void collectWaste(wasteType unused, int quantity) {
    }

    public void transportWaste(disposalArea destination) {
        if (pendingWaste.isEmpty()) {
            System.out.println("No waste to transport.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DISPOSAL_LOG, true))) {
            for (waste w : pendingWaste) {
                String line = "Transported to " + destination.getName() + ": " + w.toString();
                writer.write(line);
                writer.newLine();

                // create a WasteRecord for each transported Waste
                int recordId = nextRecordID();
                wasteRecord rec = new wasteRecord(recordId, w.getWasteID(), w.getLocation(), w.getQuantity());
                appendWasteRecord(rec);
            }
        } catch (Exception e) {
            System.out.println("Error writing disposal log: " + e.getMessage());
        }

        pendingWaste.clear();
        savePendingWaste();
        logToSystem("Waste transported to " + destination.getName());
        System.out.println("Waste transported and logged.");
    }

    public void logDisposal(String location, wasteType wastetype, int quantity) {
        String msg = "Logged disposal -> Location: " + location +
                ", Type: " + wastetype.getTypeName() +
                ", Quantity: " + quantity;
        logToSystem(msg);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DISPOSAL_LOG, true))) {
            writer.write(msg);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing disposal log: " + e.getMessage());
        }
    }

    public void updateWasteStatus() {
        if (pendingWaste.isEmpty()) {
            System.out.println("No pending waste.");
            return;
        }

        for (waste w : pendingWaste) {
            w.updateStatus("Processed");
        }

        savePendingWaste();
        logToSystem("All pending waste marked as 'Processed'.");
        System.out.println("All pending waste marked as 'Processed'.");
    }

    private void savePendingWaste() {
        ensureBasePathExists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PENDING_FILE))) {
            for (waste w : pendingWaste) {
                writer.write(
                        w.getWasteID() + "," +
                        safeForCsv(w.getWasteType().getTypeName()) + "," +
                        safeForCsv(w.getWasteType().getCategory()) + "," +
                        w.getQuantity() + "," +
                        safeForCsv(w.getLocation()) + "," +
                        safeForCsv(w.getStatus())
                );
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving pending waste file: " + e.getMessage());
        }
    }

    private void loadPendingWaste() {
        File file = new File(PENDING_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(PENDING_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 6);
                if (parts.length < 6) continue;

                int id = Integer.parseInt(parts[0]);
                String typeName = parts[1];
                String category = parts[2];
                int qty = Integer.parseInt(parts[3]);
                String location = parts[4];
                String status = parts[5];

                wasteType wt = new wasteType(typeName, category);
                waste w = new waste(id, wt, qty, location, status);
                pendingWaste.add(w);
            }
        } catch (Exception e) {
            System.out.println("Error loading pending waste file: " + e.getMessage());
        }
    }

    private void saveContainers() {
        ensureBasePathExists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONTAINERS_FILE))) {
            for (container c : containers) {
                writer.write(c.getContainerID() + "," +
                        safeForCsv(c.getName()) + "," +
                        c.getCapacity() + "," +
                        c.getCurrentLoad() + "," +
                        safeForCsv(c.getLocation()));
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving containers file: " + e.getMessage());
        }
    }

    private void loadContainers() {
        File file = new File(CONTAINERS_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(CONTAINERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue;

                int cid = Integer.parseInt(parts[0]);
                String name = parts[1];
                int capacity = Integer.parseInt(parts[2]);
                int currentLoad = Integer.parseInt(parts[3]);
                String location = parts[4];

                container c = new container(cid, name, capacity, currentLoad, location);
                containers.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error loading containers file: " + e.getMessage());
        }
    }

    private int nextRecordID() {
        File f = new File(WASTE_RECORDS_FILE);
        if (!f.exists()) return 1;
        int max = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 2);
                if (p.length > 0) {
                    try {
                        int id = Integer.parseInt(p[0]);
                        if (id > max) max = id;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return max + 1;
    }

    private void appendWasteRecord(wasteRecord rec) {
        ensureBasePathExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WASTE_RECORDS_FILE, true))) {
            writer.write(rec.getRecordID() + "," +
                    rec.getWasteID() + "," +
                    safeForCsv(rec.getLocation()) + "," +
                    rec.getQuantity() + "," +
                    rec.getTimestamp().format(DATE_FORMAT));
            writer.newLine();
        } catch (Exception e) {
            System.out.println("Error appending waste record: " + e.getMessage());
        }
    }

    public void viewPendingWaste() {
        if (pendingWaste.isEmpty()) {
            System.out.println("No pending waste.");
            return;
        }

        System.out.println("\n--- Pending Waste ---");
        for (waste w : pendingWaste) {
            System.out.println(w);
        }
    }

    public void viewContainers() {
        if (containers.isEmpty()) {
            System.out.println("No containers registered.");
            return;
        }

        System.out.println("\n--- Containers ---");
        for (container c : containers) {
            System.out.println(c);
        }
    }

    public void viewDisposalRecords() {
        File f = new File(WASTE_RECORDS_FILE);
        if (!f.exists()) {
            System.out.println("No disposal records.");
            return;
        }

        System.out.println("\n--- Disposal Records ---");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 5);
                if (p.length < 5) continue;
                System.out.println("RecordID=" + p[0] +
                        ", WasteID=" + p[1] +
                        ", Location=" + p[2] +
                        ", Quantity=" + p[3] +
                        ", Time=" + p[4]);
            }
        } catch (Exception e) {
            System.out.println("Error reading disposal records: " + e.getMessage());
        }
    }

    private void logToSystem(String message) {
        ensureBasePathExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SYSTEM_LOG, true))) {
            String entry = LocalDateTime.now().format(DATE_FORMAT) + " - " + message;
            writer.write(entry);
            writer.newLine();
        } catch (Exception e) {
            System.out.println("Error writing system log: " + e.getMessage());
        }
    }

    private String safeForCsv(String s) {
        if (s == null) return "";
        return s.replace("\n", " ").replace("\r", " ").replace(",", ";");
    }

    private void ensureBasePathExists() {
        File base = new File(BASE_PATH);
        if (!base.exists()) {
            base.mkdirs();
        }
    }
}
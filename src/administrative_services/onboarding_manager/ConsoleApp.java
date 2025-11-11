package administrative_services.onboarding_manager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    private static final Scanner sc = new Scanner(System.in);
    private static final OnboardingService svc = new OnboardingService();
    private static final String DATA_DIR = "data";

    public static void main(String[] args) {
        CsvSnapshot.loadAll(svc, DATA_DIR);
        System.out.println("Onboarding CLI (auto-saves CSV to ./data)");
        while (true) {
            printMenu();
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        createEmployee();
                        CsvSnapshot.saveAll(svc, DATA_DIR);
                    }
                    case "2" ->
                        listEmployees();
                    case "3" ->
                        showItems();
                    case "4" -> {
                        markItemDoneByNumber();
                        CsvSnapshot.saveAll(svc, DATA_DIR);
                    }
                    case "5" -> {
                        scheduleOrientation();
                        CsvSnapshot.saveAll(svc, DATA_DIR);
                    }
                    case "6" ->
                        listOrientations();
                    case "7" ->
                        isReady();
                    case "0" -> {
                        CsvSnapshot.saveAll(svc, DATA_DIR);
                        System.out.println("Saved.");
                        return;
                    }
                    default ->
                        System.out.println("Invalid.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("---- MENU ----");
        System.out.println("1) Create employee");
        System.out.println("2) List employees");
        System.out.println("3) Show checklist items");
        System.out.println("4) Mark item done (by number)");
        System.out.println("5) Schedule orientation");
        System.out.println("6) List orientations");
        System.out.println("7) Is employee ready?");
        System.out.println("0) Save & Exit");
    }

    private static LocalDate readDateOrToday(String label) {
        System.out.print(label + " (YYYY-MM-DD, blank = today): ");
        String s = sc.nextLine().trim();
        return s.isEmpty() ? LocalDate.now() : LocalDate.parse(s);
    }

    private static LocalDateTime readDateTimeOrToday(String label) {
        System.out.print(label + " (YYYY-MM-DD or YYYY-MM-DDTHH:MM, blank = today): ");
        String s = sc.nextLine().trim();
        if (s.isEmpty()) {
            return LocalDate.now().atStartOfDay();
        }
        if (s.length() == 10) {
            return LocalDate.parse(s).atStartOfDay();
        }
        return LocalDateTime.parse(s);
    }

    private static int askEmployeeId() {
        System.out.print("Employee id (number): ");
        return Integer.parseInt(sc.nextLine().trim());
    }

    // actions 
    private static void createEmployee() {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        LocalDate start = readDateOrToday("Start date");
        Employee e = svc.createEmployee(name, email, start);
        System.out.println("Created employee id=" + e.getId());
    }

    private static void listEmployees() {
        List<Employee> list = svc.listEmployees();
        if (list.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        for (Employee e : list) {
            System.out.println(e.getId() + " | " + e.getName() + " | " + e.getEmail() + " | start " + e.getStartDate());
        }
    }

    private static void showItems() {
        int id = askEmployeeId();
        List<ChecklistItem> items = svc.listItems(id);
        if (items.isEmpty()) {
            System.out.println("No items.");
            return;
        }
        for (ChecklistItem it : items) {
            String done = it.isDone() ? ("DONE on " + it.getCompletedOn()) : "PENDING";
            System.out.println(it.getCode() + " | due " + it.getDueDate() + " | " + done);
        }
    }

    // blank date => today
    private static void markItemDoneByNumber() {
        int id = askEmployeeId();
        List<ChecklistItem> items = svc.listItems(id);
        if (items.isEmpty()) {
            System.out.println("No items.");
            return;
        }

        System.out.println("Select an item to mark done:");
        for (int i = 0; i < items.size(); i++) {
            ChecklistItem it = items.get(i);
            String status = it.isDone() ? ("DONE on " + it.getCompletedOn()) : "PENDING";
            System.out.printf("%d) %s | due %s | %s%n", i + 1, it.getCode(), it.getDueDate(), status);
        }
        System.out.print("Enter number (or 0 to cancel): ");
        int choice = Integer.parseInt(sc.nextLine().trim());
        if (choice == 0) {
            System.out.println("Cancelled.");
            return;
        }
        if (choice < 1 || choice > items.size()) {
            System.out.println("Invalid number.");
            return;
        }

        ChecklistItem picked = items.get(choice - 1);
        if (picked.isDone()) {
            System.out.println("That item is already DONE.");
            return;
        }

        LocalDate when = readDateOrToday("Completed on");
        svc.markItemDone(id, picked.getCode(), when);
        System.out.println("Marked " + picked.getCode() + " done on " + when + ".");
    }

    private static void scheduleOrientation() {
        int id = askEmployeeId();
        LocalDateTime when = readDateTimeOrToday("Date/Time");
        System.out.print("Location: ");
        String loc = sc.nextLine().trim();
        System.out.print("Placeholder? (y/n): ");
        boolean ph = sc.nextLine().trim().equalsIgnoreCase("y");
        svc.scheduleOrientation(id, when, loc, ph);
        System.out.println("Orientation saved for " + when + ".");
    }

    // Show ALL orientations across all employees
    private static void listOrientations() {
        var employees = svc.listEmployees();
        boolean any = false;
        for (Employee e : employees) {
            var list = svc.listOrientations(e.getId());
            for (Orientation o : list) {
                any = true;
                System.out.println(
                        o.getDateTime() + " @ " + o.getLocation()
                        + (o.isPlaceholder() ? " (placeholder)" : "")
                        + " | emp " + e.getId() + " - " + e.getName()
                );
            }
        }
        if (!any) {
            System.out.println("No orientations.");
        }
    }

    private static void isReady() {
        int id = askEmployeeId();
        System.out.println(svc.isReady(id) ? "READY" : "NOT READY");
    }
}

package administrative_services.security_manager;

import administrative_services.onboarding_manager.Employee;
import administrative_services.onboarding_manager.OnboardingService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static OnboardingService onboardingService;
    private static AccessManager accessManager;
    private static Scanner scanner;
    private static final String USER = "Admin";

    public static void main(String[] args) {
        onboardingService = new OnboardingService();
        accessManager = new AccessManager(onboardingService);
        scanner = new Scanner(System.in);

        mainMenu();
        onboardingService.save();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n===== SECURITY SYSTEM =====");
            System.out.println("1. Employees");
            System.out.println("2. Badges");
            System.out.println("3. Process Termination");
            System.out.println("0. Exit");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    employeeMenu();
                    break;
                case 2:
                    badgeMenu();
                    break;
                case 3:
                    processTermination();
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void employeeMenu() {
        while (true) {
            System.out.println("\n----- EMPLOYEES -----");
            System.out.println("1. View");
            System.out.println("2. Add");
            System.out.println("0. Back");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    viewEmployees();
                    break;
                case 2:
                    addEmployee();
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void badgeMenu() {
        while (true) {
            System.out.println("\n----- BADGES -----");
            System.out.println("1. View");
            System.out.println("2. Assign");
            System.out.println("3. Change Role");
            System.out.println("0. Back");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    viewBadges();
                    break;
                case 2:
                    assignBadge();
                    break;
                case 3:
                    changeRole();
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void viewEmployees() {
        List<Employee> list = onboardingService.listEmployees();
        if (list.isEmpty()) {
            System.out.println("No employees.");
            return;
        }

        System.out.println("\n--- EMPLOYEES ---");
        for (Employee e : list) {
            String status;
            if (e.getStatus().equals("TERMINATED")) {
                status = "TERMINATED";
            } else {
                Badge b = accessManager.getEmployeeBadge(e.getId());
                status = b != null ? b.getRole().getDisplayName() : "NO BADGE";
            }
            System.out.println(e.getId() + " | " + e.getName() + " | " + status);
        }
    }

    private static void viewBadges() {
        HashMap<String, Badge> badges = accessManager.getBadges();
        if (badges.isEmpty()) {
            System.out.println("No badges.");
            return;
        }

        System.out.println("\n--- BADGES ---");
        for (Badge b : badges.values()) {
            Employee e = onboardingService.getEmployee(b.getEmployeeId());
            String name = e != null ? e.getName() : "Unknown";
            System.out.println(b.getBadgeId() + " | " + name + " | " +
                    b.getRole().getDisplayName() + " | " + b.getStatus());
        }
    }

    private static void addEmployee() {
        System.out.println("\n--- ADD EMPLOYEE ---");

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name required.");
            return;
        }

        System.out.print("Email [Enter=auto]: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty())
            email = name.toLowerCase().replace(" ", ".") + "@hospital.com";

        System.out.print("Start date [Enter=today]: ");
        String dateStr = scanner.nextLine().trim();
        LocalDate startDate = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr);

        Employee emp = onboardingService.createEmployee(name, email, startDate);
        System.out.println("Created employee ID: " + emp.getId());

        if (confirm("Assign badge now?")) {
            assignBadgeToEmployee(emp.getId());
        }
    }

    private static void assignBadge() {
        System.out.println("\n--- ASSIGN BADGE ---");

        int empId = selectEmployeeWithoutBadge();
        if (empId == -1)
            return;

        assignBadgeToEmployee(empId);
    }

    private static void assignBadgeToEmployee(int empId) {
        Employee emp = onboardingService.getEmployee(empId);
        System.out.println("Employee: " + emp.getName());

        Role role = selectRole();
        if (role == null)
            return;

        System.out.println("\nAccess for " + role.getDisplayName() + ":");
        for (String a : role.getDefaultAccess()) {
            System.out.println("  - " + a);
        }

        if (!confirm("Assign?"))
            return;

        try {
            Badge badge = accessManager.assignBadge(empId, role, USER);
            System.out.println("Badge assigned: " + badge.getBadgeId());
        } catch (AccessManager.ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void changeRole() {
        System.out.println("\n--- CHANGE ROLE ---");

        int empId = selectEmployeeWithBadge();
        if (empId == -1)
            return;

        Badge badge = accessManager.getEmployeeBadge(empId);
        System.out.println("Current role: " + badge.getRole().getDisplayName());

        Role newRole = selectRole();
        if (newRole == null)
            return;

        if (newRole == badge.getRole()) {
            System.out.println("Same role selected.");
            return;
        }

        if (!confirm("Change role? (will reset access)"))
            return;

        try {
            accessManager.changeRole(empId, newRole, USER);
            System.out.println("Role changed.");
        } catch (AccessManager.ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void processTermination() {
        System.out.println("\n--- PROCESS TERMINATION ---");

        int empId = selectEmployeeWithBadge();
        if (empId == -1)
            return;

        Employee emp = onboardingService.getEmployee(empId);
        Badge badge = accessManager.getEmployeeBadge(empId);

        System.out.println("\nEmployee: " + emp.getName());
        System.out.println("Badge: " + badge.getBadgeId());
        System.out.println("Role: " + badge.getRole().getDisplayName());

        System.out.println("\nThis will:");
        System.out.println("  - Revoke badge");
        System.out.println("  - Revoke all access");
        System.out.println("  - Mark employee as TERMINATED");

        if (!confirm("Process termination?"))
            return;

        accessManager.processLastDay(empId, USER, false);
        System.out.println("\nTermination processed.");
    }

    // helpers

    private static int selectEmployeeWithBadge() {
        HashMap<String, Badge> badges = accessManager.getBadges();
        String[] names = new String[badges.size()];
        int[] ids = new int[badges.size()];
        int count = 0;

        for (Badge b : badges.values()) {
            if (b.isActive()) {
                Employee e = onboardingService.getEmployee(b.getEmployeeId());
                if (e != null && !e.getStatus().equals("TERMINATED")) {
                    names[count] = e.getName() + " - " + b.getRole().getDisplayName();
                    ids[count++] = b.getEmployeeId();
                }
            }
        }

        if (count == 0) {
            System.out.println("No employees with badges.");
            return -1;
        }

        String[] opts = new String[count];
        System.arraycopy(names, 0, opts, 0, count);

        int c = selectFromOptions("Select employee:", opts);
        return c == -1 ? -1 : ids[c - 1];
    }

    private static int selectEmployeeWithoutBadge() {
        List<Employee> list = onboardingService.listEmployees();
        String[] names = new String[list.size()];
        int[] ids = new int[list.size()];
        int count = 0;

        for (Employee e : list) {
            if (e.getStatus().equals("TERMINATED"))
                continue;
            if (accessManager.getEmployeeBadge(e.getId()) == null) {
                names[count] = e.getName();
                ids[count++] = e.getId();
            }
        }

        if (count == 0) {
            System.out.println("All employees have badges.");
            return -1;
        }

        String[] opts = new String[count];
        System.arraycopy(names, 0, opts, 0, count);

        int c = selectFromOptions("Select employee:", opts);
        return c == -1 ? -1 : ids[c - 1];
    }

    private static Role selectRole() {
        Role[] roles = Role.values();
        String[] names = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            names[i] = roles[i].getDisplayName();
        }

        int c = selectFromOptions("Select role:", names);
        return c == -1 ? null : roles[c - 1];
    }

    private static int selectFromOptions(String prompt, String[] options) {
        System.out.println("\n" + prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Cancel");

        int c = readInt("Select: ");
        return (c < 1 || c > options.length) ? -1 : c;
    }

    private static boolean confirm(String msg) {
        System.out.println(msg + " (1=Yes, 0=No)");
        return readInt("") == 1;
    }

    private static int readInt(String prompt) {
        if (!prompt.isEmpty())
            System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
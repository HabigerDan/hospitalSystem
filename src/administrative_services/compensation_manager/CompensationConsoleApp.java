package administrative_services.compensation_manager;

import administrative_services.onboarding_manager.Employee;
import administrative_services.onboarding_manager.OnboardingService;
import administrative_services.security_manager.Role;
import administrative_services.security_manager.Badge;
import administrative_services.security_manager.AccessManager;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CompensationConsoleApp {
    private static OnboardingService onboardingService;
    private static CompensationManager compensationManager;
    private static AccessManager accessManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        onboardingService = new OnboardingService();
        accessManager = new AccessManager(onboardingService);
        compensationManager = new CompensationManager(onboardingService);
        scanner = new Scanner(System.in);

        mainMenu();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n===== COMPENSATION CONFIGURATION =====");
            System.out.println("1. Configure Base Pay Rates");
            System.out.println("2. Manage Individual Employee Pay");
            System.out.println("3. Configure Shift Differentials");
            System.out.println("4. View All Compensation");
            System.out.println("0. Exit");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    configureBasePayRates();
                    break;
                case 2:
                    manageEmployeePay();
                    break;
                case 3:
                    configureShiftDifferentials();
                    break;
                case 4:
                    viewAllCompensation();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
            }
        }
    }

    private static void configureBasePayRates() {
        while (true) {
            System.out.println("\n----- CONFIGURE BASE PAY RATES -----");
            System.out.println("1. View All Base Rates");
            System.out.println("2. Set Base Rate for Position");
            System.out.println("0. Back");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    viewBasePayRates();
                    break;
                case 2:
                    setBasePayRate();
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void viewBasePayRates() {
        System.out.println("\n--- BASE PAY RATES ---");
        
        Map<Role, Double> rates = compensationManager.getAllBasePayRates();
        if (rates.isEmpty()) {
            System.out.println("No base pay rates configured.");
            return;
        }

        for (Role role : Role.values()) {
            Double rate = rates.get(role);
            if (rate != null) {
                System.out.printf("%s: $%.2f/hr%n", role.getDisplayName(), rate);
            } else {
                System.out.printf("%s: Not set%n", role.getDisplayName());
            }
        }
    }

    private static void setBasePayRate() {
        System.out.println("\n--- SET BASE PAY RATE ---");
        
        Role role = selectRole();
        if (role == null)
            return;

        Double currentRate = compensationManager.getBasePayRate(role);
        if (currentRate != null) {
            System.out.printf("Current rate for %s: $%.2f/hr%n", role.getDisplayName(), currentRate);
        } else {
            System.out.printf("No rate set for %s%n", role.getDisplayName());
        }

        System.out.print("Enter new hourly rate: $");
        double rate = readDouble();
        if (rate <= 0) {
            System.out.println("Invalid rate. Must be positive.");
            return;
        }

        try {
            compensationManager.setBasePayRate(role, rate);
            System.out.printf("Base rate for %s set to $%.2f/hr%n", role.getDisplayName(), rate);
        } catch (CompensationManager.ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void manageEmployeePay() {
        while (true) {
            System.out.println("\n----- MANAGE INDIVIDUAL EMPLOYEE PAY -----");
            System.out.println("1. View All Employee Pay Rates");
            System.out.println("2. Set Employee Pay Rate");
            System.out.println("0. Back");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    viewEmployeePayRates();
                    break;
                case 2:
                    setEmployeePayRate();
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void viewEmployeePayRates() {
        List<Employee> employees = onboardingService.listEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees exist. Add employees through onboarding first.");
            return;
        }

        System.out.println("\n--- EMPLOYEE PAY RATES ---");
        System.out.printf("%-5s %-25s %-20s %-20s %-15s%n", "ID", "Name", "Role", "Status", "Hourly Rate");
        System.out.println("─".repeat(85));

        for (Employee emp : employees) {
            Badge badge = accessManager.getEmployeeBadge(emp.getId());
            String roleName = badge != null ? badge.getRole().getDisplayName() : "No Badge";
            Role role = badge != null ? badge.getRole() : null;
            
            Double customRate = compensationManager.getEmployeePayRate(emp.getId());
            String rateStr;
            
            if (customRate != null) {
                rateStr = String.format("$%.2f/hr (Custom)", customRate);
            } else if (role != null) {
                double effectiveRate = compensationManager.getEffectivePayRate(emp.getId(), role);
                if (effectiveRate > 0) {
                    rateStr = String.format("$%.2f/hr (Base)", effectiveRate);
                } else {
                    rateStr = "Not set";
                }
            } else {
                rateStr = "Not set";
            }
            
            System.out.printf("%-5d %-25s %-20s %-20s %-15s%n", 
                emp.getId(), 
                emp.getName(), 
                roleName,
                emp.getStatus(), 
                rateStr);
        }
    }

    private static void setEmployeePayRate() {
        System.out.println("\n--- SET EMPLOYEE PAY RATE ---");

        int empId = selectActiveEmployee();
        if (empId == -1)
            return;

        Employee emp = getEmployee(empId);
        Badge badge = accessManager.getEmployeeBadge(empId);
        
        if (badge == null) {
            System.out.println("Employee has no badge. Assign a badge first.");
            return;
        }
        
        Role role = badge.getRole();
        System.out.println("Employee: " + emp.getName());
        System.out.println("Role: " + role.getDisplayName());

        Double customRate = compensationManager.getEmployeePayRate(empId);
        double effectiveRate = compensationManager.getEffectivePayRate(empId, role);
        
        if (customRate != null) {
            System.out.printf("Current rate: $%.2f/hr (Custom)%n", customRate);
        } else if (effectiveRate > 0) {
            System.out.printf("Current rate: $%.2f/hr (Base rate for %s)%n", effectiveRate, role.getDisplayName());
        } else {
            System.out.println("Current rate: Not set (no base rate for " + role.getDisplayName() + ")");
        }

        System.out.print("Enter new hourly rate: $");
        double rate = readDouble();
        if (rate <= 0) {
            System.out.println("Invalid rate. Must be positive.");
            return;
        }

        try {
            compensationManager.setEmployeePayRate(empId, rate);
            System.out.printf("Pay rate for %s set to $%.2f/hr (Custom)%n", emp.getName(), rate);
        } catch (CompensationManager.ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void configureShiftDifferentials() {
        while (true) {
            System.out.println("\n----- CONFIGURE SHIFT DIFFERENTIALS -----");
            System.out.println("1. View All Differentials");
            System.out.println("2. Set Shift Differential");
            System.out.println("0. Back");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    viewShiftDifferentials();
                    break;
                case 2:
                    setShiftDifferential();
                    break;
                case 0:
                    return;
            }
        }
    }

    private static void viewShiftDifferentials() {
        System.out.println("\n--- SHIFT DIFFERENTIALS ---");
        
        Map<String, Double> differentials = compensationManager.getAllShiftDifferentials();
        if (differentials.isEmpty()) {
            System.out.println("No shift differentials configured.");
            return;
        }

        for (Map.Entry<String, Double> entry : differentials.entrySet()) {
            System.out.printf("%s: +$%.2f/hr%n", entry.getKey(), entry.getValue());
        }
    }

    private static void setShiftDifferential() {
        System.out.println("\n--- SET SHIFT DIFFERENTIAL ---");

        String shiftType = selectShiftType();
        if (shiftType == null)
            return;

        Double currentDiff = compensationManager.getShiftDifferential(shiftType);
        if (currentDiff != null) {
            System.out.printf("Current differential for %s: +$%.2f/hr%n", shiftType, currentDiff);
        } else {
            System.out.printf("No differential set for %s%n", shiftType);
        }

        System.out.print("Enter differential amount: $");
        double amount = readDouble();
        if (amount < 0) {
            System.out.println("Invalid amount. Cannot be negative.");
            return;
        }

        try {
            compensationManager.setShiftDifferential(shiftType, amount);
            System.out.printf("Shift differential for %s set to +$%.2f/hr%n", shiftType, amount);
        } catch (CompensationManager.ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllCompensation() {
        System.out.println("\n===== ALL COMPENSATION DATA =====");
        
        // Base rates
        System.out.println("\n--- BASE PAY RATES BY POSITION ---");
        Map<Role, Double> baseRates = compensationManager.getAllBasePayRates();
        if (baseRates.isEmpty()) {
            System.out.println("No base pay rates configured.");
        } else {
            for (Role role : Role.values()) {
                Double rate = baseRates.get(role);
                if (rate != null) {
                    System.out.printf("%s: $%.2f/hr%n", role.getDisplayName(), rate);
                }
            }
        }

        // Employee rates
        System.out.println("\n--- INDIVIDUAL EMPLOYEE PAY RATES ---");
        List<Employee> employees = onboardingService.listEmployees();
        Map<Integer, Double> empRates = compensationManager.getAllEmployeePayRates();
        
        if (employees.isEmpty()) {
            System.out.println("No employees exist.");
        } else {
            boolean hasData = false;
            for (Employee emp : employees) {
                if (!emp.getStatus().equals("TERMINATED")) {
                    Badge badge = accessManager.getEmployeeBadge(emp.getId());
                    if (badge != null) {
                        Role role = badge.getRole();
                        Double customRate = empRates.get(emp.getId());
                        double effectiveRate = compensationManager.getEffectivePayRate(emp.getId(), role);
                        
                        if (customRate != null) {
                            System.out.printf("%s (%s): $%.2f/hr (Custom)%n", 
                                emp.getName(), role.getDisplayName(), customRate);
                            hasData = true;
                        } else if (effectiveRate > 0) {
                            System.out.printf("%s (%s): $%.2f/hr (Base)%n", 
                                emp.getName(), role.getDisplayName(), effectiveRate);
                            hasData = true;
                        }
                    }
                }
            }
            if (!hasData) {
                System.out.println("No pay rates configured.");
            }
        }

        // Shift differentials
        System.out.println("\n--- SHIFT DIFFERENTIALS ---");
        Map<String, Double> differentials = compensationManager.getAllShiftDifferentials();
        if (differentials.isEmpty()) {
            System.out.println("No shift differentials configured.");
        } else {
            for (Map.Entry<String, Double> entry : differentials.entrySet()) {
                System.out.printf("%s: +$%.2f/hr%n", entry.getKey(), entry.getValue());
            }
        }
    }

    // Helper methods

    private static Role selectRole() {
        Role[] roles = Role.values();
        String[] names = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            names[i] = roles[i].getDisplayName();
        }

        int c = selectFromOptions("Select position:", names);
        return c == -1 ? null : roles[c - 1];
    }

    private static String selectShiftType() {
        String[] shiftTypes = {"Night", "Evening", "Weekend", "Holiday"};
        int c = selectFromOptions("Select shift type:", shiftTypes);
        return c == -1 ? null : shiftTypes[c - 1];
    }

    private static int selectActiveEmployee() {
        List<Employee> list = onboardingService.listEmployees();
        String[] names = new String[list.size()];
        int[] ids = new int[list.size()];
        int count = 0;

        for (Employee e : list) {
            if (!e.getStatus().equals("TERMINATED")) {
                names[count] = e.getName() + " - " + e.getStatus();
                ids[count++] = e.getId();
            }
        }

        if (count == 0) {
            System.out.println("No active employees. Add employees through onboarding first.");
            return -1;
        }

        String[] opts = new String[count];
        System.arraycopy(names, 0, opts, 0, count);

        int c = selectFromOptions("Select employee:", opts);
        return c == -1 ? -1 : ids[c - 1];
    }

    private static Employee getEmployee(int id) {
        for (Employee e : onboardingService.listEmployees()) {
            if (e.getId() == id)
                return e;
        }
        return null;
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

    private static int readInt(String prompt) {
        if (!prompt.isEmpty())
            System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private static double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1.0;
        }
    }
}
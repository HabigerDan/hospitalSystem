package administrative_services.compensation_manager;

import administrative_services.onboarding_manager.Employee;
import administrative_services.onboarding_manager.OnboardingService;
import administrative_services.security_manager.Role;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CompensationManager {
    private OnboardingService onboardingService;
    private HashMap<Role, Double> basePayRates;
    private HashMap<Integer, Double> employeePayRates;
    private HashMap<String, Double> shiftDifferentials;

    private static final String DATA_DIR = "src/administrative_services/data/";
    private static final String BASE_PAY_FILE = DATA_DIR + "base_pay_rates.csv";
    private static final String EMPLOYEE_PAY_FILE = DATA_DIR + "employee_pay_rates.csv";
    private static final String DIFFERENTIALS_FILE = DATA_DIR + "shift_differentials.csv";

    public CompensationManager(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
        this.basePayRates = new HashMap<>();
        this.employeePayRates = new HashMap<>();
        this.shiftDifferentials = new HashMap<>();

        File dir = new File(DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();

        loadBasePayRates();
        loadEmployeePayRates();
        loadShiftDifferentials();
    }

    // Base Pay Rates
    public void setBasePayRate(Role role, double hourlyRate) throws ValidationException {
        if (hourlyRate <= 0) {
            throw new ValidationException("Rate must be positive");
        }
        basePayRates.put(role, hourlyRate);
        save();
    }

    public Double getBasePayRate(Role role) {
        return basePayRates.get(role);
    }

    public HashMap<Role, Double> getAllBasePayRates() {
        return basePayRates;
    }

    // Employee Pay Rates
    public void setEmployeePayRate(int employeeId, double hourlyRate) throws ValidationException {
        Employee emp = getEmployee(employeeId);
        if (emp == null) {
            throw new ValidationException("Employee not found");
        }
        if (hourlyRate <= 0) {
            throw new ValidationException("Rate must be positive");
        }
        employeePayRates.put(employeeId, hourlyRate);
        save();
    }

    public Double getEmployeePayRate(int employeeId) {
        return employeePayRates.get(employeeId);
    }

    public double getEffectivePayRate(int employeeId, Role role) {
        Double customRate = employeePayRates.get(employeeId);
        if (customRate != null) {
            return customRate;
        }

        Double baseRate = basePayRates.get(role);
        return baseRate != null ? baseRate : 0.0;
    }

    public HashMap<Integer, Double> getAllEmployeePayRates() {
        return employeePayRates;
    }

    // Shift Differentials
    public void setShiftDifferential(String shiftType, double amount) throws ValidationException {
        if (amount < 0) {
            throw new ValidationException("Differential cannot be negative");
        }
        shiftDifferentials.put(shiftType, amount);
        save();
    }

    public Double getShiftDifferential(String shiftType) {
        return shiftDifferentials.get(shiftType);
    }

    public HashMap<String, Double> getAllShiftDifferentials() {
        return shiftDifferentials;
    }

    // Helper
    private Employee getEmployee(int id) {
        for (Employee e : onboardingService.listEmployees()) {
            if (e.getId() == id)
                return e;
        }
        return null;
    }

    // Load/Save
    private void loadBasePayRates() {
        File file = new File(BASE_PAY_FILE);
        if (!file.exists())
            return;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = r.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] p = line.split(",");
                if (p.length >= 2) {
                    Role role = Role.valueOf(p[0].trim());
                    double rate = Double.parseDouble(p[1].trim());
                    basePayRates.put(role, rate);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading base pay rates");
        }
    }

    private void loadEmployeePayRates() {
        File file = new File(EMPLOYEE_PAY_FILE);
        if (!file.exists())
            return;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = r.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] p = line.split(",");
                if (p.length >= 2) {
                    int empId = Integer.parseInt(p[0].trim());
                    double rate = Double.parseDouble(p[1].trim());
                    employeePayRates.put(empId, rate);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading employee pay rates");
        }
    }

    private void loadShiftDifferentials() {
        File file = new File(DIFFERENTIALS_FILE);
        if (!file.exists())
            return;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = r.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] p = line.split(",");
                if (p.length >= 2) {
                    String shiftType = p[0].trim();
                    double amount = Double.parseDouble(p[1].trim());
                    shiftDifferentials.put(shiftType, amount);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading shift differentials");
        }
    }

    private void save() {
        saveBasePayRates();
        saveEmployeePayRates();
        saveShiftDifferentials();
    }

    private void saveBasePayRates() {
        try (PrintWriter w = new PrintWriter(new FileWriter(BASE_PAY_FILE))) {
            w.println("Role,HourlyRate");
            for (Map.Entry<Role, Double> entry : basePayRates.entrySet()) {
                w.println(entry.getKey().name() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error saving base pay rates");
        }
    }

    private void saveEmployeePayRates() {
        try (PrintWriter w = new PrintWriter(new FileWriter(EMPLOYEE_PAY_FILE))) {
            w.println("EmployeeID,HourlyRate");
            for (Map.Entry<Integer, Double> entry : employeePayRates.entrySet()) {
                w.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error saving employee pay rates");
        }
    }

    private void saveShiftDifferentials() {
        try (PrintWriter w = new PrintWriter(new FileWriter(DIFFERENTIALS_FILE))) {
            w.println("ShiftType,Amount");
            for (Map.Entry<String, Double> entry : shiftDifferentials.entrySet()) {
                w.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error saving shift differentials");
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String msg) {
            super(msg);
        }
    }
}
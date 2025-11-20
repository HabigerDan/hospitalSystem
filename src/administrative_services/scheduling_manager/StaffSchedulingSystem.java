package administrative_services.scheduling_manager;

import administrative_services.onboarding_manager.Employee;
import administrative_services.onboarding_manager.OnboardingService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaffSchedulingSystem {
    private Scanner scanner;
    private OnboardingService onboardingService;
    private ScheduleManager manager;

    private static final String[] SHIFT_TIMES = {
            "06:00-14:00 (Morning)", "14:00-22:00 (Evening)", "22:00-06:00 (Night)",
            "08:00-16:00 (Day)", "09:00-17:00 (Standard)"
    };

    public static void main(String[] args) {
        new StaffSchedulingSystem().run();
    }

    public StaffSchedulingSystem() {
        scanner = new Scanner(System.in);
        onboardingService = new OnboardingService();
        manager = new ScheduleManager(onboardingService);
    }

    public void run() {
        System.out.println("=== Staff Scheduling ===\n");
        mainMenu();
        scanner.close();
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n===== SCHEDULING =====");
            System.out.println("1. View Schedules");
            System.out.println("2. Create Schedule");
            System.out.println("3. Add Shift");
            System.out.println("4. View Shifts");
            System.out.println("5. Remove Shift");
            System.out.println("0. Exit");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    viewSchedules();
                    break;
                case 2:
                    createSchedule();
                    break;
                case 3:
                    addShift();
                    break;
                case 4:
                    viewShifts();
                    break;
                case 5:
                    removeShift();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void viewSchedules() {
        List<Schedule> list = manager.getSchedules();
        if (list.isEmpty()) {
            System.out.println("No schedules.");
            return;
        }

        System.out.println("\n--- SCHEDULES ---");
        for (Schedule s : list) {
            int count = manager.getShiftsForSchedule(s.getPeriodId()).size();
            System.out.println(s.getPeriodId() + " | " + s.getStartDate() + " to " +
                    s.getEndDate() + " | " + count + " shifts");
        }
    }

    private void createSchedule() {
        System.out.println("\n--- CREATE SCHEDULE ---");
        LocalDate start = readDate("Start date [Enter=today]: ");
        LocalDate end = readDate("End date [Enter=+7 days]: ");
        if (end.isBefore(start))
            end = start.plusDays(7);

        Schedule s = manager.createSchedule(start, end);
        System.out.println("Created: " + s.getPeriodId());
    }

    private void viewShifts() {
        Schedule s = selectSchedule();
        if (s == null)
            return;

        List<Shift> list = manager.getShiftsForSchedule(s.getPeriodId());
        if (list.isEmpty()) {
            System.out.println("No shifts.");
            return;
        }

        System.out.println("\n--- SHIFTS ---");
        for (Shift sh : list) {
            String empName = "UNASSIGNED";
            if (sh.isAssigned()) {
                Employee emp = manager.getEmployeeById(sh.getEmployeeId());
                if (emp != null)
                    empName = emp.getName();
            }
            System.out.println(sh.getShiftId() + " | " + sh.getDate() + " | " +
                    sh.getStartTime() + "-" + sh.getEndTime() + " | " + empName);
        }
    }

    private void addShift() {
        Schedule s = selectSchedule();
        if (s == null)
            return;

        System.out.println("\n--- ADD SHIFT ---");

        LocalDate date = readDate("Date [Enter=today]: ");

        int timeChoice = selectFromOptions("Shift time:", SHIFT_TIMES);
        if (timeChoice == -1)
            return;

        LocalTime start, end;
        switch (timeChoice) {
            case 1:
                start = LocalTime.of(6, 0);
                end = LocalTime.of(14, 0);
                break;
            case 2:
                start = LocalTime.of(14, 0);
                end = LocalTime.of(22, 0);
                break;
            case 3:
                start = LocalTime.of(22, 0);
                end = LocalTime.of(6, 0);
                break;
            case 4:
                start = LocalTime.of(8, 0);
                end = LocalTime.of(16, 0);
                break;
            default:
                start = LocalTime.of(9, 0);
                end = LocalTime.of(17, 0);
                break;
        }

        // select employee
        Employee emp = selectEmployee();

        Shift shift = manager.addShift(s.getPeriodId(), date, start, end);

        if (emp != null) {
            String error = manager.assignEmployee(shift, emp);
            if (error != null) {
                System.out.println("Shift added but: " + error);
            } else {
                System.out.println("Shift added: " + emp.getName() + " on " + date);
            }
        } else {
            System.out.println("Shift added (unassigned)");
        }
    }

    private void removeShift() {
        Schedule s = selectSchedule();
        if (s == null)
            return;

        Shift shift = selectShift(s.getPeriodId());
        if (shift == null)
            return;

        if (!confirm("Remove shift?"))
            return;

        manager.removeShift(shift.getShiftId());
        System.out.println("Removed.");
    }

    // helpers
    private Schedule selectSchedule() {
        List<Schedule> list = manager.getSchedules();
        if (list.isEmpty()) {
            System.out.println("No schedules. Create one first.");
            return null;
        }

        String[] opts = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            opts[i] = list.get(i).getPeriodId() + " (" + list.get(i).getStartDate() + ")";
        }

        int c = selectFromOptions("Select schedule:", opts);
        return c == -1 ? null : list.get(c - 1);
    }

    private Shift selectShift(String scheduleId) {
        List<Shift> list = manager.getShiftsForSchedule(scheduleId);
        if (list.isEmpty()) {
            System.out.println("No shifts.");
            return null;
        }

        String[] opts = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Shift sh = list.get(i);
            String who = sh.isAssigned() ? (manager.getEmployeeById(sh.getEmployeeId()) != null
                    ? manager.getEmployeeById(sh.getEmployeeId()).getName()
                    : "?") : "UNASSIGNED";
            opts[i] = sh.getDate() + " " + sh.getStartTime() + "-" + sh.getEndTime() + " (" + who + ")";
        }

        int c = selectFromOptions("Select shift:", opts);
        return c == -1 ? null : list.get(c - 1);
    }

    private Employee selectEmployee() {
        List<Employee> list = manager.getEmployees();
        List<Employee> active = new ArrayList<>();

        for (Employee e : list) {
            if (!e.getStatus().equals("TERMINATED")) {
                active.add(e);
            }
        }

        if (active.isEmpty()) {
            System.out.println("No employees. Add through onboarding first.");
            return null;
        }

        String[] opts = new String[active.size()];
        for (int i = 0; i < active.size(); i++) {
            opts[i] = active.get(i).getName();
        }

        int c = selectFromOptions("Select employee (0 for unassigned):", opts);
        return c == -1 ? null : active.get(c - 1);
    }

    private int selectFromOptions(String prompt, String[] options) {
        System.out.println("\n" + prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Cancel");

        int c = readInt("Select: ");
        return (c < 1 || c > options.length) ? -1 : c;
    }

    private boolean confirm(String msg) {
        System.out.println(msg + " (1=Yes, 0=No)");
        return readInt("") == 1;
    }

    private int readInt(String prompt) {
        if (!prompt.isEmpty())
            System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private LocalDate readDate(String prompt) {
        System.out.print(prompt);
        String in = scanner.nextLine().trim();
        if (in.isEmpty())
            return LocalDate.now();
        try {
            return LocalDate.parse(in);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}
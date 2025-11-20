package administrative_services.scheduling_manager;

import administrative_services.onboarding_manager.Employee;
import administrative_services.onboarding_manager.OnboardingService;
import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScheduleManager {
    private OnboardingService onboardingService;
    private List<Schedule> schedules;
    private List<Shift> shifts;

    private static final String DATA_DIR = "hospitalSystem/src/administrative_services/data/";
    private static final String SCHEDULES_FILE = DATA_DIR + "schedules.csv";
    private static final String SHIFTS_FILE = DATA_DIR + "shifts.csv";

    public ScheduleManager(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
        this.schedules = new ArrayList<>();
        this.shifts = new ArrayList<>();

        File dir = new File(DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();

        loadSchedules();
        loadShifts();
    }

    public List<Employee> getEmployees() {
        return onboardingService.listEmployees();
    }

    public Employee getEmployeeById(int id) {
        return onboardingService.getEmployee(id);
    }

    public Schedule createSchedule(LocalDate start, LocalDate end) {
        String id = "SCH-" + UUID.randomUUID().toString().substring(0, 6);
        Schedule s = new Schedule(id, start, end);
        schedules.add(s);
        saveSchedules();
        return s;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public Shift addShift(String scheduleId, LocalDate date, LocalTime start, LocalTime end) {
        String id = "SHF-" + UUID.randomUUID().toString().substring(0, 6);
        Shift shift = new Shift(id, scheduleId, date, start, end);
        shifts.add(shift);
        saveShifts();
        return shift;
    }

    public List<Shift> getShiftsForSchedule(String scheduleId) {
        List<Shift> result = new ArrayList<>();
        for (Shift s : shifts) {
            if (s.getScheduleId().equals(scheduleId))
                result.add(s);
        }
        return result;
    }

    public void removeShift(String shiftId) {
        shifts.removeIf(s -> s.getShiftId().equals(shiftId));
        saveShifts();
    }

    public String assignEmployee(Shift shift, Employee employee) {
        // check overlap
        for (Shift s : shifts) {
            if (s.getShiftId().equals(shift.getShiftId()))
                continue;
            if (s.getEmployeeId() != employee.getId())
                continue;
            if (!s.getDate().equals(shift.getDate()))
                continue;

            if (s.getStartTime().isBefore(shift.getEndTime()) &&
                    shift.getStartTime().isBefore(s.getEndTime())) {
                return "Overlaps with another shift";
            }
        }

        // check 40 hour limit
        int weekHours = getWeekHours(employee.getId(), shift.getDate());
        if (weekHours + shift.getHours() > 40) {
            return "Exceeds 40 hour limit";
        }

        shift.setEmployeeId(employee.getId());
        saveShifts();
        return null;
    }

    private int getWeekHours(int employeeId, LocalDate date) {
        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);
        int total = 0;

        for (Shift s : shifts) {
            if (s.getEmployeeId() == employeeId &&
                    !s.getDate().isBefore(weekStart) &&
                    !s.getDate().isAfter(weekEnd)) {
                total += s.getHours();
            }
        }
        return total;
    }

    // load/save
    private void loadSchedules() {
        File file = new File(SCHEDULES_FILE);
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
                if (p.length >= 3) {
                    schedules.add(new Schedule(p[0], LocalDate.parse(p[1]), LocalDate.parse(p[2])));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading schedules");
        }
    }

    private void loadShifts() {
        File file = new File(SHIFTS_FILE);
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
                if (p.length >= 5) {
                    Shift s = new Shift(p[0], p[1], LocalDate.parse(p[2]),
                            LocalTime.parse(p[3]), LocalTime.parse(p[4]));
                    if (p.length > 5 && !p[5].isEmpty()) {
                        try {
                            s.setEmployeeId(Integer.parseInt(p[5].trim()));
                        } catch (NumberFormatException e) {
                            // ignore bad data
                        }
                    }
                    shifts.add(s);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading shifts");
        }
    }

    private void saveSchedules() {
        try (PrintWriter w = new PrintWriter(new FileWriter(SCHEDULES_FILE))) {
            w.println("PeriodId,StartDate,EndDate");
            for (Schedule s : schedules) {
                w.println(s.getPeriodId() + "," + s.getStartDate() + "," + s.getEndDate());
            }
        } catch (IOException e) {
            System.err.println("Error saving schedules");
        }
    }

    private void saveShifts() {
        try (PrintWriter w = new PrintWriter(new FileWriter(SHIFTS_FILE))) {
            w.println("ShiftId,ScheduleId,Date,StartTime,EndTime,EmployeeId");
            for (Shift s : shifts) {
                w.println(s.getShiftId() + "," + s.getScheduleId() + "," + s.getDate() + "," +
                        s.getStartTime() + "," + s.getEndTime() + "," +
                        (s.isAssigned() ? s.getEmployeeId() : ""));
            }
        } catch (IOException e) {
            System.err.println("Error saving shifts");
        }
    }
}
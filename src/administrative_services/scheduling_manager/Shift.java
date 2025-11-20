package administrative_services.scheduling_manager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class Shift {
    private String shiftId;
    private String scheduleId;
    private int employeeId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public Shift(String shiftId, String scheduleId, LocalDate date,
            LocalTime startTime, LocalTime endTime) {
        this.shiftId = shiftId;
        this.scheduleId = scheduleId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employeeId = -1;
    }

    public String getShiftId() {
        return shiftId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isAssigned() {
        return employeeId > 0;
    }

    public void setEmployeeId(int id) {
        this.employeeId = id;
    }

    public int getHours() {
        long hours = Duration.between(startTime, endTime).toHours();
        return hours < 0 ? (int) (hours + 24) : (int) hours;
    }
}
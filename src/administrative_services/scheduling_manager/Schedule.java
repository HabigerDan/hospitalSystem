package administrative_services.scheduling_manager;

import java.time.LocalDate;

public class Schedule {
    private String periodId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Schedule(String periodId, LocalDate startDate, LocalDate endDate) {
        this.periodId = periodId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getPeriodId() {
        return periodId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
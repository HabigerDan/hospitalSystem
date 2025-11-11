package nursing_services.schedule;

public class MedicationSchedule {
    final String scheduleID;
    final Patient patient;
    final Medication medication;
    final String scheduledTime;
    MedStatus status = MedStatus.PENDING;
    String administeredBy = "N/A";
    String Notes = "";

    public MedicationSchedule(String scheduleID, Patient patient, Medication medication, String scheduledTime) {
        this.scheduleID = scheduleID;
        this.patient = patient;
        this.medication = medication;
        this.scheduledTime = scheduledTime;
    }

    @Override
    public String toString() {
        return scheduleID + " | " + medication + " | " + scheduledTime + " | Status: " + status;
    }
}

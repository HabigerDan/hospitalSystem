package medical_services.schedule;

import java.time.LocalDateTime;

import java.util.UUID;

public class Appointment {

    private final UUID appointmentID;
    private final UUID doctorID;
    private final UUID patientID;

    private LocalDateTime dateTime;
    private String purpose;
    private AppointmentStatus status;

    public Appointment(UUID doctorID, UUID patientID, LocalDateTime dateTime, String purpose) {
        this.appointmentID = UUID.randomUUID();
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.dateTime = dateTime;
        this.purpose = purpose;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public UUID getAppointmentID() {
        return appointmentID;
    }

    public UUID getDoctorID() {
        return doctorID;
    }

    public UUID getPatientID() {
        return patientID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    @Override
    public String toString() {
        return "Appointment{" +
               "id=" + appointmentID +
               ", dateTime=" + dateTime +
               ", purpose='" + purpose + '\'' +
               ", status=" + status +
               '}';
    }
}


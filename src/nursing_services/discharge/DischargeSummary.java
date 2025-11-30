package nursing_services.discharge;

import nursing_services.schedule.Patient;
import nursing_services.schedule.Nurse;

public class DischargeSummary {

    private final Patient patient;
    private final Nurse nurse;
    private final boolean medicationsCleared;
    private final boolean vitalsSafe;
    private final boolean doctorApproved;
    private final VitalSigns vitalSigns;
    private final String woundInfo;
    private final String painInfo;
    private final String dischargeNotes;
    private final FollowUpAppointment followUp;

    public DischargeSummary(Patient patient,
                            Nurse nurse,
                            boolean medicationsCleared,
                            boolean vitalsSafe,
                            boolean doctorApproved,
                            VitalSigns vitalSigns,
                            String woundInfo,
                            String painInfo,
                            String dischargeNotes,
                            FollowUpAppointment followUp) {
        this.patient = patient;
        this.nurse = nurse;
        this.medicationsCleared = medicationsCleared;
        this.vitalsSafe = vitalsSafe;
        this.doctorApproved = doctorApproved;
        this.vitalSigns = vitalSigns;
        this.woundInfo = woundInfo;
        this.painInfo = painInfo;
        this.dischargeNotes = dischargeNotes;
        this.followUp = followUp;
    }

    public Patient getPatient() {
        return patient;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public boolean isMedicationsCleared() {
        return medicationsCleared;
    }

    public boolean isVitalsSafe() {
        return vitalsSafe;
    }

    public boolean isDoctorApproved() {
        return doctorApproved;
    }

    public VitalSigns getVitalSigns() {
        return vitalSigns;
    }

    public String getWoundInfo() {
        return woundInfo;
    }

    public String getPainInfo() {
        return painInfo;
    }

    public String getDischargeNotes() {
        return dischargeNotes;
    }

    public FollowUpAppointment getFollowUp() {
        return followUp;
    }

    @Override
    public String toString() {
        String patientName = patient.getName();
        String room = patient.getRoom();
        String nurseName = nurse.getName();

        String medsText = medicationsCleared ? "YES" : "NO";
        String vitalsText = vitalsSafe ? "YES" : "NO";
        String doctorText = doctorApproved ? "YES" : "NO";
        String vitalsStr = vitalSigns != null ? vitalSigns.toString() : "N/A";
        String followUpText = followUp != null ? followUp.toString() : "No follow up scheduled.";

        return "DISCHARGE SUMMARY\n"
                + "Patient: " + patientName + " (Room: " + room + ")\n"
                + "Nurse: " + nurseName + "\n"
                + "Medications cleared: " + medsText + "\n"
                + "Vitals safe: " + vitalsText + "\n"
                + "Doctor approved discharge: " + doctorText + "\n"
                + "Vital Signs: " + vitalsStr + "\n"
                + "Wound Information: " + woundInfo + "\n"
                + "Pain Information: " + painInfo + "\n"
                + "Follow-up: " + followUpText + "\n"
                + "Discharge Notes:\n" + dischargeNotes + "\n";
    }
}

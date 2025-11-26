package medical_services.request_test_and_update_patient_record;

public class MedicalRecord {

    private String diagnosis;
    private String allergies;
    private String notes;

    public MedicalRecord() {
        this.diagnosis = "";
        this.allergies = "";
        this.notes = "";
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Diagnosis: " + (diagnosis.isBlank() ? "(none)" : diagnosis) + "\n" +
               "Allergies: " + (allergies.isBlank() ? "(none)" : allergies) + "\n" +
               "Notes: " + (notes.isBlank() ? "(none)" : notes);
    }
}
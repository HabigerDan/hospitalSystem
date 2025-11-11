package nursing_services.schedule;

public class Medication {
    final String medicationID;
    final String name;
    final String dosage; 
    final String sideEffects;

    public Medication(String medicationID, String name, String dosage, String sideEffects) {
        this.medicationID = medicationID;
        this.name = name;
        this.dosage = dosage;
        this.sideEffects = sideEffects;
    }

    @Override
    public String toString() {
        return name + " (" + dosage + ")";
    }
}

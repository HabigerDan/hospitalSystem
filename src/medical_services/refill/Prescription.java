package medical_services.refill;

import java.util.UUID;
import medical_services.request_test_and_update_patient_record.Doctor;
import medical_services.request_test_and_update_patient_record.Patient;

public class Prescription {
	
	 private final UUID prescriptionID;
	 private final Patient patient;
	 private final Doctor prescribingDoctor;
	 private String medicationName;
	 private String dosage;
	 private boolean active;
	 private boolean eligibleForRefill;
	 
	 public Prescription(
			 Patient patient,
             Doctor prescribingDoctor,
             String medicationName,
             String dosage,
             boolean active,
             boolean eligibleForRefill) {
		 	 this.prescriptionID = UUID.randomUUID();
			 this.patient = patient;
			 this.prescribingDoctor = prescribingDoctor;
			 this.medicationName = medicationName;
			 this.dosage = dosage;
			 this.active = active;
			 this.eligibleForRefill = eligibleForRefill;
}
	 
	  public UUID getPrescriptionID() {
	        return prescriptionID;
	    }

	    public Patient getPatient() {
	        return patient;
	    }

	    public Doctor getPrescribingDoctor() {
	        return prescribingDoctor;
	    }

	    public String getMedicationName() {
	        return medicationName;
	    }

	    public void setMedicationName(String medicationName) {
	        this.medicationName = medicationName;
	    }

	    public String getDosage() {
	        return dosage;
	    }

	    public void setDosage(String dosage) {
	        this.dosage = dosage;
	    }

	    public boolean isActive() {
	        return active;
	    }

	    public void setActive(boolean active) {
	        this.active = active;
	    }

	    public boolean isEligibleForRefill() {
	        return eligibleForRefill;
	    }

	    public void setEligibleForRefill(boolean eligibleForRefill) {
	        this.eligibleForRefill = eligibleForRefill;
	    }

}

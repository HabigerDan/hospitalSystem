package medical_services.request_test_and_update_patient_record;

import java.time.LocalDate;


import java.util.UUID;

public class Patient {
	 final UUID patientID;
	 final String fullName;
	 final LocalDate dateOfBirth;
	 private MedicalRecord medicalRecord;
	
	public Patient(UUID patientID, String fullName, LocalDate dateOfBirth)
	{
		this.patientID = patientID;
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
		this.medicalRecord = new MedicalRecord();
	}
	
	public UUID getPatientID()
	{
		return patientID;
	}
	public String getFullName()
	{
		return fullName;
	}
	public LocalDate getDateOfBirth()
	{
		return dateOfBirth;
	}
	
	 public MedicalRecord getMedicalRecord() 
	 {
	    return medicalRecord;
	 }
	
	@Override public String toString() 
	{
		return fullName + " ( DOB:" + dateOfBirth + ") ";
	}

}

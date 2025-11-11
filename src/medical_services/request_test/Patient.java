package medical_services.request_test;

import java.time.LocalDate;

import java.util.UUID;

public class Patient {
	 final UUID patientID;
	 final String fullName;
	 final LocalDate dateOfBirth;
	
	public Patient(UUID patientID, String fullName, LocalDate dateOfBirth)
	{
		this.patientID = patientID;
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
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
	@Override public String toString() 
	{
		return fullName + " ( DOB:" + dateOfBirth + ") ";
	}

}

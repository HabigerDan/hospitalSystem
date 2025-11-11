package medical_services.request_test;

import java.util.UUID;

public class Doctor {
	
	final UUID doctorID;
	 final String name;
	 String speciality;
	public Doctor(UUID doctorID, String name, String speciality)
	{
		this.doctorID = doctorID;
		this.name = name;
		this.speciality = speciality;
		
		
	}
	
	public UUID getDoctorID()
	{
		return doctorID;
		
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getSpeciality()
	{
		return speciality;
	}
	
	public void setSpeciality(String speciality)
	{
		this.speciality = speciality;
	}

}

package medical_services.request_test;

import java.util.UUID;

public class LabDept {
	 final UUID labID;
	 final String name;
	
	LabDept(UUID labID, String name)
	{
		this.labID = labID;
		this.name = name;
	}
	
	public UUID getlabID()
	{
		return labID;
	}
	
	public String getName()
	{
		return name;
	}


}

package medical_services.request_test_and_update_patient_record;

public class TestType {
	private final String testCode;
	private String name;
	private String description;
	
	public TestType(String testCode, String name, String description)
	{
		this.testCode = testCode;
		this.name = name;
		this.description = description;
	}
	
	public String getTestCode()
	{
		return testCode;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	@Override public String toString() 
	{
		return testCode + " - " + name;
		
	}

}

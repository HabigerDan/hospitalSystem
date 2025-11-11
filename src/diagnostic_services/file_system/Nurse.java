package diagnostic_services.file_system;

import java.util.Random;

public class Nurse implements Person, HealthCareProvider {
	private int id = 0;
	private String firstName = "";
	private String lastName = "";

	@Override
	public int getId() {
		if (this.id != 0) {
			return this.id;
		} else {
			return 0;
		}
	}

	@Override
	public String getName() {
		return this.firstName + " " + this.lastName;
	}
	
	public Nurse(String firstName, String lastName) {
		Random rand = new Random();
		this.id = rand.nextInt(10000) + 1000;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Nurse(String firstName, String lastName, Integer id) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}

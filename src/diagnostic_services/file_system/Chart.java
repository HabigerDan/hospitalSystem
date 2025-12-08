package diagnostic_services.file_system;

import java.util.ArrayList;
import roles.*;

public class Chart {
	private Patient patient;
	private Integer chartId;
	private HealthCareProvider user;
	private String date = "Dec 3, 2025";
	private final ArrayList<HealthCareProvider> others = new ArrayList<>();
	private String allergies;
	private String notes;

	public Chart() {}

	public Chart(HealthCareProvider user, Patient patient, String notes) {
		this.user = user;
		this.patient = patient;
		this.others.addAll(others);
		this.notes = notes;
	}

	public void addNote(String str) {
		notes = str;
	}

	public void addAllergies(String all) {
		allergies = all;
	}

	public void addOthers(HealthCareProvider other) {
		others.add(other);
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setUser(HealthCareProvider person) {
		this.user = person;
	}

	public void setChartId(int id) {
		this.chartId = id;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getId() {
		return this.chartId;
	}

	@Override
	public String toString() {
		return " Chart Id: " + chartId + "\n" +
				" Patient: " + patient + "\n" +
				" Issued by: " + user + "\n" +
				" Date Issued: " + date + "\n" +
				" Others: " + others + "\n" +
				" Allergies: " + allergies + "\n" +
				" Notes: " + notes + "\n" +
				"--------------------------------------------------------------------------- \n";
	}
}

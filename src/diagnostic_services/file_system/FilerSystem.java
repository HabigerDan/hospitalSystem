package diagnostic_services.file_system;

import diagnostic_services.iteration_2.Ambulance;
import diagnostic_services.iteration_2.PCR;
import diagnostic_services.iteration_2.Sample;
import diagnostic_services.iteration_2.Status;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;
import roles.*;

public class FilerSystem {
	private final LinkedHashMap<Integer, Doctor> doctors = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, Nurse> nurses = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, Patient> patients = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, Chart> charts = new LinkedHashMap<>();
  private final LinkedHashMap<Integer, LabTech> labTechs = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, roles.EMT> emts = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, Sample> samples = new LinkedHashMap<>();
	private final LinkedHashMap<Integer, PCR> pcrs = new LinkedHashMap<>();

	private static final String PATH_DOC = "./src/diagnostic_services/file_system/Data/doctors.json";
	private static final String PATH_NUR = "./src/diagnostic_services/file_system/Data/nurses.json";
	private static final String PATH_PAT = "./src/diagnostic_services/file_system/Data/patients.json";
	private static final String PATH_LAB = "./src/diagnostic_services/file_system/Data/labtechs.json";
	private static final String PATH_EMT = "./src/diagnostic_services/file_system/Data/emts.json";
	private static final String PATH_SAM = "./src/diagnostic_services/file_system/Data/samples.json";
	private static final String PATH_PCR = "./src/diagnostic_services/file_system/Data/pcrs.json";


	public FilerSystem() {
		this.loadStaff();
		this.loadPatients();
		this.loadSamples();
		this.loadPCR();
	}

	private void loadPatients() {
		File file = new File(PATH_PAT);
		try {
			Scanner sc = new Scanner(file);
			this.parseChartJson(sc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String cleanVal(String str) {
		return str.strip()
			.replace(",", "")
			.replace("\"", "")
			.replace("[", "")
			.replace("]", "");
	}

	private void parseChartJson(Scanner sc) {
		String firstName = "";
		String lastName = "";
		Integer id = 0;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] split;

			if (line.contains("firstName")) {
				split = line.split(":");
				firstName = cleanVal(split[1]);
			}
			if (line.contains("lastName")) {
				split = line.split(":");
				lastName = cleanVal(split[1]);
			}
			if (line.contains("id")) {
				split = line.split(":");
				id = Integer.valueOf(cleanVal(split[1]));
			}

			if (line.contains("prevCharts")) {
				Chart chart = new Chart();

				while (sc.hasNextLine()) {
					line = sc.nextLine();

					if (line.contains("}")) {
						charts.put(chart.getId(), chart);
					}

					if (line.contains("{")) {
						chart = new Chart();
						line = sc.nextLine();
					}

					if (line.contains("]") && !line.contains("[")) {
						break;
					}

					chart.setPatient(new Patient(firstName, lastName, id));


					if (line.contains("chartId")) {
						split = line.split(":");
						chart.setChartId(Integer.parseInt(split[1].strip().replace(",", "")));

					} else if (line.contains("userIdIss")) {
						split = line.split(":");
						if(doctors.get(Integer.valueOf(cleanVal(split[1]))) != null) {
							chart.setUser(doctors.get(Integer.valueOf(cleanVal(split[1]))));
						} else if (nurses.get(Integer.valueOf(cleanVal(split[1]))) != null) {
							chart.setUser(nurses.get(Integer.valueOf(cleanVal(split[1]))));
						}

					} else if (line.contains("dateIss")) {
						split = line.split(":");
						chart.setDate(split[1]);

					} else if (line.contains("others")) {
						split = line.split(":");
						split = split[1].split(",");
						for (int i = 0; i < split.length; i++) {
							String otherId = cleanVal(split[i]);
							if (otherId.isEmpty()) {
								continue;
							}

							int p = Integer.parseInt(otherId);
							if (doctors.get(p) != null) {
								chart.addOthers(doctors.get(p));
							} else if (nurses.get(p) != null) {
								chart.addOthers(nurses.get(p));
							}
						}

					} else if (line.contains("allergies")) {
						split = line.split(":");
						chart.addAllergies(split[1]);

					} else if (line.contains("additionalNotes")) {
						split = line.split(":");
						chart.addNote(split[1]);
					}
				}
			}

			if (line.contains("}") && firstName != null) {
				Patient patient = new Patient(firstName, lastName, id);
				patients.put(id, patient);
			}
		}
	}

	private void loadPCR() {
		File file = new File("./src/diagnostic_services/file_system/Data/pcrs.json");

		try {
			Scanner sc = new Scanner(file);
			this.parsePCR(sc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void parseSamples(Scanner sc) {
		int id = 0;
		String sampleType = "";
		int patientId = 0;
		LinkedHashMap<String, String> result = new LinkedHashMap<>();
		Status status = Status.PENDING;

		while (sc.hasNextLine()) {
			String str = sc.nextLine();

			if (str.contains("id\":")) {
				id = Integer.parseInt(cleanVal(str.split(":")[1]));

			} else if (str.contains("sampleType\":")) {
				sampleType = this.cleanVal(str.split(":")[1]);

			} else if (str.contains("status\":")) {
				status = Status.valueOf(cleanVal(str.split(":")[1]));

			} else if (str.contains("patient\":")) {
				patientId = Integer.parseInt(cleanVal(str.split(":")[1]));

			} else if (str.contains("results\":")) {
				String temp = str.substring(13);
				String[] tempList = temp.split(",");

				for (String pair : tempList) {
					if (pair.contains(":")) {
						String key = cleanVal(pair.split(":")[0]);
						String value = cleanVal(pair.split(":")[1]);
						result.put(key, value);
					}
				}
			}

			if (str.contains("}")) {
				Patient patient = patients.get(patientId); patientId = 0;
				Sample sample = new Sample(id, sampleType, status, patient, result);
				samples.put(samples.size() + 1, sample);
				result = new LinkedHashMap<>();
			}
		}
	}

	private void loadStaff() {
		File file = new File(PATH_DOC);

		for(int i = 0; i < 5; i++) {
			switch (i) {
				case 0 -> file = new File(PATH_DOC);
				case 1 -> file = new File(PATH_NUR);
				case 2 -> file = new File(PATH_LAB);
				case 3 -> file = new File(PATH_EMT);
			}
			try {
				Scanner sc = new Scanner(file);
				this.parsePeople(sc, i);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void parsePeople(Scanner sc, int type) {
		String firstName = "";
		String lastName = "";
		int id = 0;

		while (sc.hasNextLine()) {
			String str = sc.nextLine();

			if (str.contains("firstName")) {
				firstName = cleanVal(str.split(":")[1]);
			}
			if (str.contains("lastName")) {
				lastName = cleanVal(str.split(":")[1]);
			}
			if (str.contains("id")) {
				id = Integer.parseInt(cleanVal(str.split(":")[1]));
			}

			if (str.contains("}")) {
				switch(type) {
					case 0: // Doctor
						Doctor doctor = new Doctor(firstName, lastName, id);
						doctors.put(id, doctor);
						break;
					case 1: // Nurse
						Nurse nurse = new Nurse(firstName, lastName, id);
						nurses.put(id, nurse);
						break;
					case 2: // Patient
						Patient patient = new Patient(firstName, lastName, id);
						patients.put(id, patient);
						break;
					case 3: // LabTech
						LabTech labTech = new LabTech(firstName, lastName, id);
						labTechs.put(id, labTech);
						break;
					case 4: // EMT
						EMT emt = new EMT(firstName, lastName, id);
						emts.put(id, emt);
						break;
				}
			}
		}
	}

	private void loadSamples() {
		File file = new File("./src/diagnostic_services/file_system/Data/samples.json");

		try {
			Scanner sc = new Scanner(file);
			this.parseSamples(sc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void parsePCR(Scanner sc) {
		int id = 0;
			int patientId = 0;
			int emtId1 = 0;
			int emtId2 = 0;
			String notes = "";

		while (sc.hasNextLine()) {
			String str = sc.nextLine();

			if (str.contains("id")) {
				id = Integer.parseInt(cleanVal(str.split(":")[1]));
			}

			if (str.contains("patient")) {
				patientId = Integer.parseInt(cleanVal(str.split(":")[1]));
			}

			if (str.contains("emts")) {
				String[] emtIds = str.split(":")[1].split(",");
				emtId1 = Integer.parseInt(cleanVal(emtIds[0]));
				emtId2 = Integer.parseInt(cleanVal(emtIds[1]));
			}

			if (str.contains("notes")) {
				notes = cleanVal(str.split(":")[1]);
			}

			if (str.contains("}")) {
				EMT emt1 = emts.get(emtId1);
				EMT emt2 = emts.get(emtId2);
				Ambulance ambulance = new Ambulance(emt1, emt2);
				Patient patient = patients.get(patientId);
				PCR pcr = new PCR(id, ambulance, patient, notes);
				pcrs.put(id, pcr);
			}
		}
	}

	public boolean isDoctor(int id) {
		return doctors.containsKey(id);
	}

	public boolean isNurse(int id) {
		return nurses.containsKey(id);
	}

	public boolean isLabTech(int id) {
		return labTechs.containsKey(id);
	}

	public boolean isEMT(int id) {
		return emts.containsKey(id);
	}

	public boolean isHealthCareProvider(int userId) {
		return (doctors.containsKey(userId) || nurses.containsKey(userId));
	}

	public void displaySample() {
		Set<Integer> keys = samples.keySet();
		for(Integer i : keys) {
			System.out.println(samples.get(i));
		}
		System.out.println("Total amount of samples: " + samples.size() + "\n");
	}

	public void displaySample(int id) {
		System.out.println(samples.get(id));
	}

	public void submitChart(int id, Scanner sc) {
		HealthCareProvider user;
		Patient patient;

		if (doctors.containsKey(id)) {
			user = doctors.get(id);
		} else if (nurses.containsKey(id)) {
			user = nurses.get(id);
		} else {
			System.out.println("Invalid user.");
			return;
		}

		while (true) {
				System.out.print("Patient id: ");
				String in = sc.nextLine();
				int patientId;

				patientId = Integer.parseInt(in);
				if (patients.containsKey(patientId)) {
					patient = patients.get(patientId);
					break;
				} else if (in.equalsIgnoreCase("exit")) {
					return;
				} else {
					System.out.println("Invalid patient id, please try again.");
				}
		}

		Chart chart = new Chart(user, patient, PATH_DOC);

		System.out.println("Please enter any allegeries. (Do not use semicolons).");
		String allergies = sc.nextLine();
		chart.addAllergies(allergies);

		System.out.println("Please enter any additional notes. (Do not use semicolons).");
		String notes = sc.nextLine();
		chart.addNote(notes);

		chart.setChartId(charts.size() + 1);
		charts.put(chart.getId(), chart);

		System.out.println("Chart submitted successfully!\n");
	}

  public void submitSample(Scanner sc) {
		String sampleType;
		Patient patient;

		while (true) {
			System.out.println("Input patient Id: ");
			String in = sc.nextLine();
			int patientId = Integer.parseInt(in);

			if (in.equalsIgnoreCase("exit")) {
				return;
			} else if (patients.containsKey(patientId)) {
				patient = patients.get(patientId);
				break;
			} else {
				System.out.println("Invalid patient id, please try again or type 'exit' to cancel.\n");
			}
		}

		System.out.println("Input sample type: ");
		sampleType = sc.nextLine();

		Sample sample = new Sample(samples.size() + 1, sampleType, patient);

		LinkedHashMap<String, String> results = new LinkedHashMap<>();
		while (true) {
				System.out.println("Enter fields you would like to add to result. Press enter to stop.");
				String field = sc.nextLine();
				if (field.isBlank()) {
					break;
				}
				results.put(field, "NULL");
		}
    sample.setResults(results);
    samples.put(sample.getId(), sample);
  }

	public void editSample(int id) {
		Sample sample = samples.get(id);
		sample.setStatus(Status.IN_PROGRESS);
	}

	public void completeSample(int id) {
		Sample sample = samples.get(id);
		sample.setStatus(Status.COMPLETED);
	}

	public void cancelSample(int id) {
		Sample sample = samples.get(id);
		sample.setStatus(Status.CANCELLED);
	}

	public void displayCharts() {
		for (Integer key : charts.keySet()) {
			System.out.println(charts.get(key));
		}

		System.out.println("Total amount of charts: " + charts.size() + "\n");
	}

	public void displayPCR(int id) {
		System.out.println(pcrs.get(id));
		System.out.println();
	}

	public void displayPCR() {
		for(Integer i : pcrs.keySet()) {
			System.out.println(pcrs.get(i));
			System.out.println("-----------------------");
		}
		System.out.println("\n Total amount of pcrs: " + pcrs.size() + "\n");
	}

  public void editPCR(int id, Scanner sc) {
		PCR pcr = pcrs.get(id);

		System.out.println("Enter any notes you would like. Or enter to skip.");
		String notes = sc.nextLine();

		if (!notes.isEmpty()) {
			pcr.setNotes(pcr.getNotes() + " " + notes);
		}

		System.out.print("PCR enter patient id or 'enter' to skip: ");
		String in = sc.nextLine();

		if (!in.isBlank()) {
			int patientId = Integer.parseInt(in);
			Patient patient = patients.get(patientId);
			pcr.setPatient(patient);
		}
  }

  public void createPCR(int emtId, Scanner sc) {
		EMT emt1 = emts.get(emtId);

		while (true) {
			System.out.println("Enter the second EMT's ID: ");

			try {
					emtId = Integer.parseInt(sc.nextLine());
					if (!emts.containsKey(emtId)) {
						System.out.println("Invalid id, please try again.");
						continue;
					}
					break;
			} catch (Exception e) {
					System.out.println("Invalid id, please try again.");
			}
		}

		EMT emt2 = emts.get(emtId);


    PCR pcr = new PCR(emt1, emt2);
		pcr.setId(pcrs.size() + 1);
		pcrs.put(pcr.getId(), pcr);

		System.out.println();
  }

	private class FileWriter {

		public static void writeJsonToFile(String filePath, String json) {
			FileWriter.writeToFile(filePath, json);
		}

		public static void clearFile(String filePath) {
			FileWriter.eraseFileContents(filePath);
		}
	}

	public void saveData() {
		writeDoctors();
		writeNurses();
		writeEMTs();
		writeLabTechs();
		writePatients();
		writeSamples();
		writePCRs();
	}

	private void writeDoctors() {
		Doctors[] _doctors = doctors.values();
		String content = "[\n";

		for (Doctor doctor : _doctors) {
			String json = """
					{
						"firstName": "%s",
						"lastName": "%s",
						"id": %d
					}
					"""
				.formatted(doctor.getFirstName(), doctor.getLastName(), doctor.getId());
			content = content + json;
		}
		content = content + "\n]";
		FileWriter.writeJsonToFile(PATH_DOC, content);
	}

	private void writeNurses() {
		Nurses[] _nurses = nurses.values();
		String content = "[\n";
		for (Nurse nurse : _nurses) {
			String json = """
					{
						"firstName": "%s",
						"lastName": "%s",
						"id": %d
					}
					"""
				.formatted(nurse.getFirstName(), nurse.getLastName(), nurse.getId());
			content = content + json;
		}
		content = content + "\n]";
		FileWriter.writeJsonToFile(PATH_NUR, content);
	}

	private void writeEMTs() {
		EMTs[] _emts = emts.values();
		String content = "[\n";
		for (roles.EMT emt : _emts) {
			String json = """
					{
						"firstName": "%s",
						"lastName": "%s",
						"id": %d
					}
					"""
				.formatted(emt.getFirstName(), emt.getLastName(), emt.getId());
			content = content + json;
		}
		content = content + "\n]";
		FileWriter.writeJsonToFile(PATH_EMT, content);
	}

	private void writeLabTechs() {
		LabTechs[] _labTechs = labTechs.values();
		String content = "[\n";
		for (LabTech labTech : _labTechs) {
			String json = """
					{
						"firstName": "%s",
						"lastName": "%s",
						"id": %d
					}
					"""
				.formatted(labTech.getFirstName(), labTech.getLastName(), labTech.getId());
			content = content + json;
		}
		content = content + "\n]";
		FileWriter.writeJsonToFile(PATH_LAB, content);
	}

	private void writePatients() {
		Chart[] _charts = charts.values();
		for (Chart chart : _charts) {
			Patient patient = chart.getPatient();

		}

		Patients[] _patients = patients.values();
		String content = "[\n";
		for (Patient patient : _patients) {
			String json = """
					{
						"firstName": "%s",
						"lastName": "%s",
						"id": %d,
						"prevCharts": []
					}
					"""
				.formatted(patient.getFirstName(), patient.getLastName(), patient.getId());
			content = content + json;
		}
		content = content + "\n]";
		FileWriter.writeJsonToFile(PATH_PAT, content);
	}

	private void writeSamples() {
		Samples[] _samples = samples.values();
		String content = "[\n";
		for (Sample sample : _samples) {
			String json = """
					{
						"id": %d,
						"sampleType": "%s",
						"status": "%s",
						"patient": %d,
						"results": %s
					}
					"""
				.formatted(
					sample.getId(),
					sample.getSampleType(),
					sample.getStatus().toString(),
					sample.getPatient().getId(),
					sample.getResults().toString()
				);
			content = content + json;
		}
		content = content + "\n]";
		FileWriter.writeJsonToFile(PATH_SAM, content);
	}

	private void writePCRs() {
		PCRs[] _pcrs = pcrs.values();
		String content = "[\n";
		for (PCR pcr : _pcrs) {
			String json = """
					{
						"id": %d,
						"patient": %d,
						"emts": [%d, %d],
						"notes": "%s"
					}
					"""
				.formatted(
					pcr.getId(),
					pcr.getPatient().getId(),
					pcr.getAmbulance().getEmt().get(0).getId(),
					pcr.getAmbulance().getEmt().get(1).getId(),
					pcr.getNotes()
				);
			content = content + json;
		}
		content = content + "\n]";
		FileWriter.writeJsonToFile(PATH_PCR, content);
	}

}

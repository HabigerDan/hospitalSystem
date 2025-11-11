package diagnostic_services.file_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;

public class FilerSystem {
	private LinkedHashMap<Integer, Doctor> doctors = new LinkedHashMap<>();
	private LinkedHashMap<Integer, Nurse> nurses = new LinkedHashMap<>();
	private LinkedHashMap<Integer, Patient> patients = new LinkedHashMap<>();
	private LinkedHashMap<Integer, Chart> charts = new LinkedHashMap<>();
	private static final String PATH_DOC = "./Json/doctors.json";
	private static final String PATH_NUR = "./Json/nurses.json";
	private static final String PATH_PAT = "./Json/patients.json";
	
	public FilerSystem() {
		load();
	}
	
	public void load() {
		File file = new File(PATH_DOC);
		
		for(int i = 0; i < 3; i++) {
			switch (i) {
				case 0: 
					file = new File(PATH_DOC);
					break;
				case 1: 
					file = new File(PATH_NUR);
					break;
				case 2: 
					file = new File(PATH_PAT);
					break;
			}
			try {
				Scanner sc = new Scanner(file);
				this.parseChartJson(sc, i);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String cleanVal(String str) {
		return str.strip()
				.replace(",", "")
				.replace("\"", "")
				.replace("[", "")
				.replace("]", "");
	}
	
	private void parseChartJson(Scanner sc, int i) {
		String firstName = "";
		String lastName = "";
		Integer id = 0;
		while (sc.hasNext()) {
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
				id = Integer.parseInt(cleanVal(split[1]));
				
			}
			
			if (line.contains("prevCharts")) {
				Chart chart = new Chart();
				while (sc.hasNext()) {
					
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
						if(doctors.get(Integer.parseInt(cleanVal(split[1]))) != null) {
							chart.setUser(doctors.get(Integer.parseInt(cleanVal(split[1]))));
						} else if (nurses.get(Integer.parseInt(split[1].strip().replace(",", ""))) != null) {
							chart.setUser(nurses.get(Integer.parseInt(split[1])));
						}
						
					} else if (line.contains("dateIss")) {
						split = line.split(":");
						chart.setDate(split[1]);
					} else if (line.contains("others")) {
						split = line.split(":");
						split = split[1].split(",");
						for (int j = 0; j < split.length; j++) {
							int p = Integer.parseInt(split[j].replace("[", "").replace("]", "").strip());
							
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
				switch(i) {
					case 0:
						Doctor doctor = new Doctor(firstName, lastName, id);
						doctors.put(Integer.valueOf(id), doctor);
						break;
					case 1: 
						Nurse nurse = new Nurse(firstName, lastName, id);
						nurses.put(Integer.valueOf(id), nurse);
					case 2:
						Patient patient = new Patient(firstName, lastName, id);
						patients.put(Integer.valueOf(id), patient);
						break;
				}
			}
		}
	}
	
	public void print() {
		Set<Integer> keys = charts.keySet();
		for(int i = 0; i < charts.size(); i++) {
			Chart chart = charts.get(keys.toArray()[i]);
			System.out.println(chart);
		}
		
		
		System.out.println("Total amount of charts: " + charts.size());
	}

	public boolean isHealthCareProvider(int userId) {
		return (doctors.containsKey(userId) || nurses.containsKey(userId));
	}
}

package medical_services.request_test_and_update_patient_record;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class MedicalSystem {
	
	public static final String TestType = null;
	public final Map<UUID, Doctor> doctors = new HashMap<>();
    public final Map<UUID, Patient> patients = new LinkedHashMap<>();
    public final Map<String, TestType> testTypes = new LinkedHashMap<>();
    LabDept labDept = new LabDept(UUID.randomUUID(), "Lab");
    final List<TestRequest> requests = new ArrayList<>();
    public boolean simulateNotifyFailure = false;  //that is if you click in main that you want to simulate notify failure

   

    public Patient selectPatient(UUID doctorId, UUID patientId) {
        Patient p = patients.get(patientId);
        if (p == null) {
            notifyDoctorError(doctorId, "Patients not found");
            throw new IllegalArgumentException("Patients not found: " + patientId);
        }
        System.out.println("Loaded patient: " + p.fullName + " (" + p.patientID + ")" + "Born on " + p.dateOfBirth);
        return p;
    }
    
    public UUID requestDiagnosticTest(UUID doctorID, UUID patientID, String testCode){
    	if(!doctors.containsKey(doctorID))
    		throw new IllegalArgumentException("Doctor not found");
    	if(!patients.containsKey(patientID))
    		throw new IllegalArgumentException("patient not found");
    	if(!testTypes.containsKey(testCode))
    		throw new IllegalArgumentException("test type not found");
    	
    	UUID requestID = UUID.randomUUID();
    	 TestRequest req = new TestRequest(requestID, patientID, doctorID, labDept.labID, testCode);
         requests.add(req);
         boolean sent = notifyLab(labDept.labID, requestID);
         if (sent)
         {
        	 req.setStatus(RequestStatus.SENT_TO_LAB);
        	 notifyDoctorConfirmation(doctorID, requestID);  	 
         }
         else 
         {
        	 notifyDoctorError(doctorID, "Request did not send");
        	 req.setStatus(RequestStatus.PENDING);
        	 req.setNotes("Notify to lab failed at " + Instant.now());
        	 
         }
         return requestID;



    	
    	
    }
    
    private void notifyDoctorConfirmation(UUID doctorId, UUID requestId) 
    {
        System.out.println("[Doctor " + doctorId + "] Confirmation: request " + requestId + " created & sent.");
    }
    
    private void notifyDoctorError(UUID doctorId, String message) {
        System.out.println("[Doctor " + doctorId + "] ERROR: " + message);
    }
    
    private boolean notifyLab(UUID labDeptID, UUID requestID)
    {
        if (simulateNotifyFailure) {
            System.out.println("[LabDept " + labDept.labID + "] FAILED to receive request " + requestID);
            return false;
        }
    	  System.out.println("[LabDept " + labDeptID + "] Received request " + requestID);
          return true;
  
    }
    
    public List<TestRequest> getRequests() {
    	return requests;
    }
    
    

    public void updatePatientMedicalRecord(Scanner sc) {

    	if (patients.isEmpty()) {
    		System.out.println("No patients exist in the system.");
    		return;
    	}

    	// Display patient list
    	List<Patient> list = new ArrayList<>(patients.values());
    	System.out.println("\n=== Select a Patient ===");
    	for (int i = 0; i < list.size(); i++) {
    		System.out.println((i + 1) + ") " + list.get(i).getFullName() +
                           " (DOB: " + list.get(i).getDateOfBirth() + ")");
    	}

    	// Patient selection
    	Patient p = null;
    	while (p == null) {
    		System.out.print("Enter patient number: ");
    		try {
    			int choice = Integer.parseInt(sc.nextLine());
    			if (choice < 1 || choice > list.size()) {
    				System.out.println("Invalid choice. Try again.");
    			} else {
    				p = list.get(choice - 1);
    			}
    		} catch (Exception e) {
    			System.out.println("Enter a valid number.");
    		}
    	}

    	MedicalRecord rec = p.getMedicalRecord();
    	System.out.println("\n=== Current Medical Record ===");
    	System.out.println(rec);

    	// Select what to update
    	System.out.println("\nWhat would you like to update?");
    	System.out.println("1) Diagnosis");
    	System.out.println("2) Allergies");
    	System.out.println("3) Notes");

    	int updateChoice = 0;
    	while (updateChoice < 1 || updateChoice > 3) {
    		System.out.print("Enter new information: ");
    		try {
    			updateChoice = Integer.parseInt(sc.nextLine());
    		} catch (Exception ignored) {}
    	}

    	System.out.print("Enter new value: ");
    	String newValue = sc.nextLine().trim();
    	while (newValue.isBlank()) {
    		System.out.print("Value cannot be blank. Enter again: ");
    		newValue = sc.nextLine().trim();
    	}

    	switch (updateChoice) {
        	case 1 -> rec.setDiagnosis(newValue);
        	case 2 -> rec.setAllergies(newValue);
        	case 3 -> rec.setNotes(newValue);
    	}

    	System.out.println("\nRecord updated successfully!");
    	System.out.println("=== Updated Record ===");
    	System.out.println(rec);
    }

}

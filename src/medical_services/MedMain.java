package medical_services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import medical_services.request_test.*;

public class MedMain 
{

	public static void main(String[] args) 
	{
        Scanner sc = new Scanner(System.in);
        MedicalSystem sys = new MedicalSystem();

        //A few patients and test types
        UUID pat1 = UUID.randomUUID();
        UUID pat2 = UUID.randomUUID();
        UUID pat3 = UUID.randomUUID();
        sys.patients.put(pat1, new Patient(pat1, "Ayan Asim", LocalDate.of(2002, 5, 12)));
        sys.patients.put(pat2, new Patient(pat2, "Asim Qureshi", LocalDate.of(1980, 6, 6)));
        sys.patients.put(pat3, new Patient(pat3, "Hira Asim", LocalDate.of(1980, 1, 12)));

        sys.testTypes.put("BTEST", new TestType("BTEST", "Blood Test", "Routine/normal"));
        sys.testTypes.put("XRAY_CHEST", new TestType("XRAY_CHEST", "Chest X-Ray", "Imaging"));
        sys.testTypes.put("MRI", new TestType("MRI", "Magnetic Resonance Imaging", "Scanning"));

   
        System.out.println("=== Doctor Sign-in ===");
        System.out.print("Enter your name: ");
        String docName = sc.nextLine().trim();
        System.out.print("Enter your specialty: ");
        String docSpec = sc.nextLine().trim();
        UUID doctorId = UUID.randomUUID();
        sys.doctors.put(doctorId, new Doctor(doctorId, docName, docSpec));
        System.out.println("Welcome, Dr. " + docName + " (" + docSpec + ").");


        System.out.println("\n=== Select a Patient ===");
        List<UUID> patientIndex = new ArrayList<>(sys.patients.keySet());
        for (int i = 0; i < patientIndex.size(); i++) {
            Patient p = sys.patients.get(patientIndex.get(i));
            System.out.println((i + 1) + ") " + p);
        }
        int pChoice = readChoice(sc, "Enter number: ", 1, patientIndex.size());
        UUID selectedPatientId = patientIndex.get(pChoice - 1);
        

        //if I want to simulate a failure to notify the lab
        System.out.print("lab notify failure? (y/N): ");
        sys.simulateNotifyFailure = sc.nextLine().trim().equalsIgnoreCase("y");

    
        sys.selectPatient(doctorId, selectedPatientId);


        System.out.println("\n=== Select Test Type ===");
        List<String> testCodes = new ArrayList<>(sys.testTypes.keySet());
        for (int i = 0; i < testCodes.size(); i++) {
            System.out.println((i + 1) + ") " + sys.testTypes.get(testCodes.get(i)));
        }
        int tChoice = readChoice(sc, "Enter number: ", 1, testCodes.size());
        String selectedTestCode = testCodes.get(tChoice - 1);
   
        UUID reqId = sys.requestDiagnosticTest(doctorId, selectedPatientId, selectedTestCode);

        //final result WITH patient ID written down as well
        TestRequest created = sys.getRequests().stream().filter(r -> r.getRequestId().equals(reqId)).findFirst().get();
        System.out.println("\n=== Result ===");
        System.out.println("Request ID   : " + created.getRequestId());
        System.out.println("Patient ID   : " + created.getPatientId());
        System.out.println("Test Code    : " + created.getTestCode());
        System.out.println("Status       : " + created.getStatus());
        if (created.getNotes() != null) System.out.println("Notes        : " + created.getNotes());

        sc.close();
    }
	


      
        private static int readChoice(Scanner sc, String prompt, int min, int max) {
            while (true) {
                System.out.print(prompt);
                String s = sc.nextLine().trim();
                try {
                    int v = Integer.parseInt(s);
                    if (v >= min && v <= max) return v;
                } catch (NumberFormatException ignored) {}
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            }



	}

}

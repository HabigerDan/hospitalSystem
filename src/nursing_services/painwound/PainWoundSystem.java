package nursing_services.painwound;

import nursing_services.schedule.Patient;
import nursing_services.schedule.Nurse;
import nursing_services.schedule.MedicalSystem;

import java.util.*;

public class PainWoundSystem {

    public final Map<String, Patient> patients = new LinkedHashMap<>();
    public final Map<String, List<PainAssessment>> painLogs = new HashMap<>();
    public final Map<String, List<WoundAssessment>> woundLogs = new HashMap<>();
    public void addPatient(Patient p) {
        String id = p.getPatientID();

        patients.put(id, p);
        painLogs.put(id, new ArrayList<>());
        woundLogs.put(id, new ArrayList<>());
    }

    public void displayPatients() {
        System.out.println("\nPatient List: ");
        int i = 1;
        for (Patient p : patients.values()) {
            System.out.println(i++ + ") " + p);
        }
    }

    public Patient selectPatient(int index) {
        return patients.values().stream().toList().get(index - 1);
    }

    public void loadExistingPatients(MedicalSystem ms) {
        for (Patient p : ms.patients.values()) {
            this.addPatient(p);
        }
    }

    public void performAssessment(Patient patient, Nurse nurse, Scanner sc) {

        String patientID = patient.getPatientID();
        String nurseID = nurse.getNurseID();

        System.out.println(" Previous Records for " + patient.getName() + " ---");
        System.out.println("Pain Logs: " + painLogs.get(patientID).size());
        System.out.println("Wound Logs: " + woundLogs.get(patientID).size());

        System.out.println(" Pain Assessment");
        System.out.print("Enter pain level (0 - 10): ");
        int painLevel = Integer.parseInt(sc.nextLine());

        System.out.print("Enter pain location: ");
        String painLocation = sc.nextLine();

        System.out.print("Enter pain type (sharp/dull/irritating): ");
        String painType = sc.nextLine();

        System.out.print("Enter patient comments: ");
        String comments = sc.nextLine();

        PainAssessment pa = new PainAssessment(
                painLevel, painLocation, painType, comments, nurseID
        );

        painLogs.get(patientID).add(pa);

        System.out.println("Pain Assessment Recorded Successfully.\n");

        System.out.println("Wound Assessment");
        System.out.print("Enter wound appearance (normal/red/swollen): ");
        String appearance = sc.nextLine();

        System.out.print("Enter wound size (small/medium/large): ");
        String size = sc.nextLine();

        System.out.print("Enter drainage type (none/serous/porous): ");
        String drainage = sc.nextLine();

        System.out.print("Was dressing changed? (yes/no): ");
        boolean changed = sc.nextLine().equalsIgnoreCase("yes");

        WoundStatus status = WoundStatus.NORMAL;

        if (appearance.equalsIgnoreCase("red") ||
            drainage.equalsIgnoreCase("bleeding")) {
            status = WoundStatus.INFECTED;
        }

        WoundAssessment wa = new WoundAssessment(
                appearance, size, drainage, changed, status, nurseID
        );

        woundLogs.get(patientID).add(wa);

        if (status == WoundStatus.INFECTED) {
            System.out.println("ALERT: Infection Indicators Found. Doctor Has Been Notified.");
        }

        System.out.println("Assessment Completed.\n");
    }
}

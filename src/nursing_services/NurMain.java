package nursing_services;

import java.util.List;
import java.util.Scanner;

import nursing_services.schedule.MedicalSystem;
import nursing_services.schedule.Medication;
import nursing_services.schedule.MedicationSchedule;
import nursing_services.schedule.Nurse;
import nursing_services.schedule.Patient;
import nursing_services.painwound.PainWoundSystem;
import nursing_services.discharge.DischargeSystem;
import nursing_services.discharge.DischargeSummary;

public class NurMain {

    public static void main(String[] args) {

        System.out.println("Nursing Services Loaded");

        Scanner sc = new Scanner(System.in);

        // Use case 1 base system (medication scheduling)
        MedicalSystem system = new MedicalSystem();

        system.addPatient(new Patient("P001", "Joe Tyler", "Room 001"));
        system.addPatient(new Patient("P002", "Sebastian Smith", "Room 002"));
        system.addPatient(new Patient("P003", "Brad Powell", "Room 003"));

        system.addMedication(new Medication("100", "Paracetamol", "500mg", "Fever"), 2);
        system.addMedication(new Medication("200", "Ibuprofen", "200mg", "Stomach upset"), 0);
        system.addMedication(new Medication("300", "Amoxicillin", "250mg", "Rash"), 3);

        system.addSchedule(new MedicationSchedule("S01",
                system.patients.get("P001"),
                system.medications.get("100"),
                "9:00 AM"));

        system.addSchedule(new MedicationSchedule("S02",
                system.patients.get("P001"),
                system.medications.get("200"),
                "2:00 PM"));

        system.addSchedule(new MedicationSchedule("S03",
                system.patients.get("P002"),
                system.medications.get("300"),
                "11:00 AM"));

        system.addSchedule(new MedicationSchedule("S04",
                system.patients.get("P003"),
                system.medications.get("100"),
                "10:00 PM"));

        system.addSchedule(new MedicationSchedule("S05",
                system.patients.get("P003"),
                system.medications.get("300"),
                "4:00 PM"));

        // Shared Pain and Wound System for use cases 2 and 3
        PainWoundSystem painWoundSystem = new PainWoundSystem();
        painWoundSystem.loadExistingPatients(system);

        DischargeSystem dischargeSystem = new DischargeSystem();

        System.out.println("Nurse Login");
        System.out.print("Enter Nurse ID: ");
        String nurseID = sc.nextLine();
        System.out.print("Enter Nurse Name: ");
        String nurseName = sc.nextLine();
        Nurse nurse = new Nurse(nurseID, nurseName);

        while (true) {

            System.out.println();
            System.out.println("MAIN MENU");
            System.out.println("1) Medication Scheduling (Use Case 1)");
            System.out.println("2) Pain and Wound Assessment (Use Case 2)");
            System.out.println("3) Patient Discharge (Use Case 3)");
            System.out.println("4) Exit Nursing Services");
            System.out.print("Choose an option: ");

            String menuChoice = sc.nextLine();

            if (menuChoice.equals("4")) {
                break;
            }

            system.displayPatients();
            System.out.print("Select patient by number: ");
            int pIndex = Integer.parseInt(sc.nextLine());
            Patient selectedPatient = system.selectPatient(pIndex);

            // USE CASE 1: Medication Scheduling
            if (menuChoice.equals("1")) {

                List<MedicationSchedule> patientSchedules = system.getSchedules(selectedPatient);

                if (patientSchedules.isEmpty()) {
                    System.out.println("No medication schedules for this patient.");
                    continue;
                }

                System.out.println("Medication Schedules for " + selectedPatient.getName() + ":");
                for (int i = 0; i < patientSchedules.size(); i++) {
                    System.out.println((i + 1) + ") " + patientSchedules.get(i));
                }

                System.out.print("Select schedule: ");
                int sIndex = Integer.parseInt(sc.nextLine());
                MedicationSchedule schedule = patientSchedules.get(sIndex - 1);

                System.out.println("Options");
                System.out.println("1) Administer Medication");
                System.out.println("2) Record Allergy Reaction");
                System.out.println("3) Request Pharmacy Restock");
                System.out.println("4) Back to Main Menu");
                System.out.print("Choose: ");

                String action = sc.nextLine();

                if (action.equals("1")) {
                    system.administerMedication(schedule, nurse);
                } else if (action.equals("2")) {
                    System.out.print("Enter allergy notes: ");
                    String notes = sc.nextLine();
                    system.holdDueToAllergy(schedule, nurse, notes);
                } else if (action.equals("3")) {
                    System.out.print("Enter medication ID: ");
                    String medID = sc.nextLine();
                    System.out.print("Enter restock amount: ");
                    int qty = Integer.parseInt(sc.nextLine());
                    system.requestRestock(medID, qty);
                }
            }

            // USE CASE 2: Pain and Wound Assessment
            if (menuChoice.equals("2")) {
                painWoundSystem.performAssessment(selectedPatient, nurse, sc);
            }

            // USE CASE 3: Discharge
            if (menuChoice.equals("3")) {
                System.out.println("Patient Discharge Processing");
                DischargeSummary summary =
                        dischargeSystem.processDischarge(selectedPatient, nurse, system, painWoundSystem, sc);

                if (summary == null) {
                    System.out.println("Discharge not completed.");
                } else {
                    System.out.println("Final Discharge Summary Generated:");
                    System.out.println(summary);
                }
            }
        }

        System.out.println("Nursing Services Closed");
        sc.close();
    }
}

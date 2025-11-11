package nursing_services;

import java.util.List;
import java.util.Scanner;
import nursing_services.schedule.*;

public class NurMain {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MedicalSystem system = new MedicalSystem();
        system.addPatient(new Patient("P001", "Joe Tyler", "Room 001"));
        system.addPatient(new Patient("P002", "Sebastian Smith", "Room 002"));
        system.addPatient(new Patient("P003", "Brad Powell", "Room 003"));
        system.addMedication(new Medication("100", "Paracetamol", "500mg", "Fever"), 2);
        system.addMedication(new Medication("200", "Ibuprofen", "200mg", "Stomach upset"), 0);
        system.addMedication(new Medication("300", "Amoxicillin", "250mg", "Rash"), 3);
        system.addSchedule(new MedicationSchedule("S01", system.patients.get("P001"), system.medications.get("100"), "9:00 AM"));
        system.addSchedule(new MedicationSchedule("S02", system.patients.get("P001"), system.medications.get("200"), "2:00 PM"));
        system.addSchedule(new MedicationSchedule("S03", system.patients.get("P002"), system.medications.get("300"), "11:00 AM"));
        system.addSchedule(new MedicationSchedule("S04", system.patients.get("P003"), system.medications.get("100"), "10:00 PM"));
        system.addSchedule(new MedicationSchedule("S05", system.patients.get("P003"), system.medications.get("300"), "4:00 PM"));
        System.out.println("Nurse Login..... ");
        System.out.print("Enter Nurse ID: ");
        String nurseID = sc.nextLine();
        System.out.print("Enter Nurse Name: ");
        String nurseName = sc.nextLine();
        Nurse nurse = new Nurse(nurseID, nurseName);

        while (true) {
            System.out.println("MAIN MENU...");
            System.out.println("1) Select Patient");
            System.out.println("2) Exit System");
            System.out.print("Choose from the above options: ");

            String choice = sc.nextLine();

            if (choice.equals("2")) {
            	break;
            }

            system.displayPatients();
            System.out.print("Select patient by the number displayed: ");
            int pIndex = Integer.parseInt(sc.nextLine());
            Patient selectedPatient = system.selectPatient(pIndex);

            List<MedicationSchedule> patientSchedules = system.getSchedules(selectedPatient);

            if (patientSchedules.isEmpty()) {
                System.out.println("No medication schedules for this patient as of now.");
                continue;
            }

            System.out.println("Medication Schedules for" + selectedPatient.getName() + ":");
            for (int i = 0; i < patientSchedules.size(); i++) {
                System.out.println((i+1) + ") " + patientSchedules.get(i));
            }

            System.out.print("Select schedule by the number displayed: ");
            int sIndex = Integer.parseInt(sc.nextLine());
            MedicationSchedule schedule = patientSchedules.get(sIndex - 1);
            System.out.println("Different options: ");
            System.out.println("1) Administer Medication");
            System.out.println("2) Record Allergy Reaction and stop Medication for the Moment");
            System.out.println("3) Request Pharmacy Restock");
            System.out.println("4) Back to the Menu");
            System.out.print("Choose: ");

            String action = sc.nextLine();

            switch (action) {
                case "1":
                    system.administerMedication(schedule, nurse);
                    break;

                case "2":
                    System.out.print("Enter allergy notes: ");
                    String notes = sc.nextLine();
                    system.holdDueToAllergy(schedule, nurse, notes);
                    break;

                case "3":
                    System.out.print("Enter medication ID to restock: ");
                    String medID = sc.nextLine();
                    System.out.print("Enter restock quantity: ");
                    int qty = Integer.parseInt(sc.nextLine());
                    system.requestRestock(medID, qty);
                    break;

                case "4":
                    continue;
            }
        }

        System.out.println("System Closed...");
        sc.close();
    }
}

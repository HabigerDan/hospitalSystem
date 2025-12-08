package medical_services;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

import medical_services.request_test_and_update_patient_record.Doctor;
import medical_services.request_test_and_update_patient_record.MedicalSystem;
import medical_services.request_test_and_update_patient_record.Patient;
import medical_services.schedule.AppointmentController;
import medical_services.schedule.AppointmentUI;
import medical_services.refill.RequestPrescriptionRefillUI;   // <-- NEW IMPORT

public class MedicalUseCaseSelector {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        MedicalSystem system = new MedicalSystem();

        Patient p1 = new Patient(UUID.randomUUID(), "Ayan Asim", LocalDate.of(2002, 5, 12));
        Patient p2 = new Patient(UUID.randomUUID(), "Asim Qureshi", LocalDate.of(1980, 6, 6));
        Patient p3 = new Patient(UUID.randomUUID(), "Hira Asim", LocalDate.of(1980, 1, 12));

        system.patients.put(p1.getPatientID(), p1);
        system.patients.put(p2.getPatientID(), p2);
        system.patients.put(p3.getPatientID(), p3);

        boolean running = true;
        while (running) {
            System.out.println("\n=== Medical Services Use Cases ===");
            System.out.println("1) Request Diagnostic Test");
            System.out.println("2) Schedule Follow-Up Appointment");
            System.out.println("3) Update Patient Medical Record");
            System.out.println("4) Request Prescription Refill");   // <-- NEW
            System.out.println("Q) Quit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    MedMain.main(new String[] {});
                }
                case "2" -> {
                    runScheduleAppointment(sc);
                }
                case "3" -> {
                    runUpdateMedicalRecord(sc, system);
                }
                case "4" -> {   
                    runRequestPrescriptionRefill(sc);
                }
                case "q", "Q" -> {
                    running = false;
                    System.out.println("Exiting Medical Services.");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }

    private static void runScheduleAppointment(Scanner sc) {

        var patients = new java.util.ArrayList<Patient>();
        patients.add(new Patient(UUID.randomUUID(), "Ayan Asim", LocalDate.of(2002, 5, 12)));
        patients.add(new Patient(UUID.randomUUID(), "Asim Qureshi", LocalDate.of(1980, 6, 6)));
        patients.add(new Patient(UUID.randomUUID(), "Hira Asim", LocalDate.of(1980, 1, 12)));

        System.out.println("=== Doctor Sign-in ===");
        System.out.print("Enter your name: ");
        String docName = sc.nextLine().trim();
        System.out.print("Enter your specialty: ");
        String docSpec = sc.nextLine().trim();
        UUID doctorId = UUID.randomUUID();
        Doctor doc = new Doctor(doctorId, docName, docSpec);
        System.out.println("Welcome, Dr. " + docName + " (" + docSpec + ").");

        AppointmentController controller = new AppointmentController(patients);
        AppointmentUI ui = new AppointmentUI(controller, sc);
        ui.startScheduleAppointment(doc);
    }

    private static void runUpdateMedicalRecord(Scanner sc, MedicalSystem system) {
        system.updatePatientMedicalRecord(sc);
    }


    private static void runRequestPrescriptionRefill(Scanner sc) {
        RequestPrescriptionRefillUI refillUI = new RequestPrescriptionRefillUI(sc);
        refillUI.run();
    }
}

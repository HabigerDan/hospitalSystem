package medical_services.schedule;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Scanner;

import medical_services.request_test_and_update_patient_record.Doctor;
import medical_services.request_test_and_update_patient_record.Patient;

public class AppointmentUI {

    private final AppointmentController controller;
    private final Scanner scanner;

    public AppointmentUI(AppointmentController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    //startScheduleAppointment()
    public void startScheduleAppointment(Doctor doctor) {

        //get and show patients
        List<Patient> patients = controller.getPatientsForDoctor(doctor);
        System.out.println("\n=== Schedule Follow-Up Appointment ===");
        System.out.println("Select a patient:");
        for (int i = 0; i < patients.size(); i++) {
            System.out.println((i + 1) + ") " + patients.get(i));
        }
        int patientChoice = readChoice("Enter number: ", 1, patients.size());
        Patient selectedPatient = patients.get(patientChoice - 1);

        //get and show available slots
        List<LocalDateTime> slots = controller.findAvailableSlots(doctor, selectedPatient);
        System.out.println("\nSelect available slot:");
        for (int i = 0; i < slots.size(); i++) {
            System.out.println((i + 1) + ") " + slots.get(i));
        }
        int slotChoice = readChoice("Enter number: ", 1, slots.size());
        LocalDateTime chosenSlot = slots.get(slotChoice - 1);

        // create appointment + confirmation
        Appointment appointment = controller.createAppointment(doctor, selectedPatient, chosenSlot, "Follow-up");
        System.out.println("\n=== Appointment Scheduled ===");
        System.out.println(appointment);
    }

    private int readChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }
}

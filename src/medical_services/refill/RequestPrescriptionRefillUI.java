package medical_services.refill;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import medical_services.request_test_and_update_patient_record.Doctor;
import medical_services.request_test_and_update_patient_record.Patient;

public class RequestPrescriptionRefillUI {

    private final Scanner scanner;

    private final List<Patient> patients = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();
    private PharmacyDepartment pharmacyDepartment;

    public RequestPrescriptionRefillUI(Scanner scanner) {
        this.scanner = scanner;
        this.pharmacyDepartment = new PharmacyDepartment("Main Hospital Pharmacy");
    }

    
    public void run() {
        System.out.println("\n=== Use Case: Request Prescription Refill ===");

        
        Doctor doctor = readDoctorInfo();

        
        setupData(doctor);

        
        Patient selectedPatient = selectPatient();
        if (selectedPatient == null) {
            System.out.println("No patient selected. Returning to main menu.");
            return;
        }

        
        handlePrescriptionSelectionAndRefill(doctor, selectedPatient);
    }

    

    private Doctor readDoctorInfo() {
        System.out.println("=== Doctor Sign-in ===");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter your specialty: ");
        String specialty = scanner.nextLine().trim();

        UUID doctorId = UUID.randomUUID();
        Doctor doctor = new Doctor(doctorId, name, specialty);

        System.out.println("\nWelcome, Dr. " + name + " (" + specialty + ").");
        return doctor;
    }

   

    private void setupData(Doctor doctor) {
        
        Patient p1 = new Patient(UUID.randomUUID(), "Ayan Asim", LocalDate.of(2002, 5, 12));
        Patient p2 = new Patient(UUID.randomUUID(), "Asim Qureshi", LocalDate.of(1980, 6, 6));
        Patient p3 = new Patient(UUID.randomUUID(), "Hira Asim", LocalDate.of(1980, 1, 12));

        patients.add(p1);
        patients.add(p2);
        patients.add(p3);

        
        prescriptions.add(new Prescription(
                p1, doctor, "Lisinopril", "10mg",
                true,  
                true   // eligible
        ));
        prescriptions.add(new Prescription(
                p1, doctor, "Ibuprofen", "200mg",
                true,
                false  // not eligible
        ));
        prescriptions.add(new Prescription(
                p2, doctor, "Metformin", "500mg",
                true,
                true
        ));
        prescriptions.add(new Prescription(
                p3, doctor, "Atorvastatin", "20mg",
                false, // inactive
                true
        ));
    }

    

    private Patient selectPatient() {
        if (patients.isEmpty()) {
            System.out.println("There are no patients in the system.");
            return null;
        }

        System.out.println("patient list:");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            System.out.println((i + 1) + ") " + p.getFullName());
        }

        while (true) {
            System.out.print("Select a patient by number (or 0 to cancel): ");
            String input = scanner.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 0) {
                return null;
            }

            if (choice >= 1 && choice <= patients.size()) {
                Patient selected = patients.get(choice - 1);
                System.out.println("You selected: " + selected.getFullName());
                return selected;
            }

            System.out.println("Invalid choice. Try again.");
        }
    }

   

    private void handlePrescriptionSelectionAndRefill(Doctor doctor, Patient patient) {
        boolean keepSelecting = true;

        while (keepSelecting) {
            List<Prescription> activePrescriptions = getActivePrescriptionsForPatient(patient);

            if (activePrescriptions.isEmpty()) {
                System.out.println("No active prescriptions found for this patient.");
                return;
            }

            System.out.println("Active prescriptions for " + patient.getFullName() + ":");
            for (int i = 0; i < activePrescriptions.size(); i++) {
                Prescription p = activePrescriptions.get(i);
                System.out.printf("%d) %s %s (eligibleForRefill=%s)%n",
                        (i + 1),
                        p.getMedicationName(),
                        p.getDosage(),
                        p.isEligibleForRefill());
            }

            Prescription selectedPrescription = selectPrescription(activePrescriptions);
            if (selectedPrescription == null) {
                System.out.println("No prescription selected.");
                return;
            }

            // Alt 1: not eligible
            if (!selectedPrescription.isEligibleForRefill()) {
                System.out.println("No prescriptions can be refilled for this specific medication.");
                System.out.println("Medication: " + selectedPrescription.getMedicationName());

                System.out.print("Select '1' to go back and choose another prescription, or '0' to cancel: ");
                String alt = scanner.nextLine().trim();
                if ("1".equals(alt)) {
                    System.out.println("Going back to prescription selection.");
                    continue; // back to loop
                } else {
                    System.out.println("Cancelling refill process.");
                    return;
                }
            }

            System.out.println("Would the patient like a refill for "
                    + selectedPrescription.getMedicationName() + "? (yes/no)");
            String wantRefill = scanner.nextLine().trim().toLowerCase();

            if (!wantRefill.startsWith("y")) {
                System.out.println("Refill not requested.");
                return;
            }

            // create RefillRequest with PENDING
            System.out.println("Creating RefillRequest...");
            RefillRequest request = new RefillRequest(selectedPrescription, doctor);
            System.out.println("RefillRequest ID: " + request.getRefillRequestID()
                    + " | status: " + request.getStatus());

            // doctor approves
            System.out.print("Approve this refill now? (yes/no): ");
            String approve = scanner.nextLine().trim().toLowerCase();

            if (!approve.startsWith("y")) {
                System.out.println("Refill request left in PENDING state. Ending use case.");
                return;
            }

            // update to APPROVED and notify pharmacy
            approveRefillRequest(request);
            pharmacyDepartment.notifyApprovedRefill(request);

            System.out.println("completed successfully.");
            keepSelecting = false;
        }
    }

    private List<Prescription> getActivePrescriptionsForPatient(Patient patient) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription p : prescriptions) {
            if (p.isActive()
                    && p.getPatient().getPatientID().equals(patient.getPatientID())) {
                result.add(p);
            }
        }
        return result;
    }

    private Prescription selectPrescription(List<Prescription> activePrescriptions) {
        while (true) {
            System.out.print("Select a prescription by number (or 0 to cancel): ");
            String input = scanner.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 0) {
                return null;
            }

            if (choice >= 1 && choice <= activePrescriptions.size()) {
                Prescription selected = activePrescriptions.get(choice - 1);
                System.out.println("You selected: " + selected.getMedicationName()
                        + " " + selected.getDosage());
                return selected;
            }

            System.out.println("Invalid choice. Try again.");
        }
    }

    private void approveRefillRequest(RefillRequest request) {
        request.setStatus(RefillStatus.APPROVED);
        request.setApprovalDate(java.time.LocalDateTime.now());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("Step 9: RefillRequest status updated to "
                + request.getStatus()
                + " at " + request.getApprovalDate().format(fmt));
    }
}

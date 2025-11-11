package nursing_services.schedule;
import java.util.*;
public class MedicalSystem {
	public final Map<String, Patient> patients = new LinkedHashMap<>();
    public final Map<String, Medication> medications = new LinkedHashMap<>();
    public final List<MedicationSchedule> schedules = new ArrayList<>();
    public final Pharmacy pharmacy = new Pharmacy(); 

    public void addPatient(Patient p) {
        patients.put(p.patientID, p);
    }

    public void addMedication(Medication m, int quantity) {
        medications.put(m.medicationID, m);
        pharmacy.stock.put(m.medicationID, quantity);
    }

    public void addSchedule(MedicationSchedule s) {
        schedules.add(s);
    }

    public void displayPatients() {
        System.out.println("Patient List: ");
        int i = 1;
        for (Patient p : patients.values()) {
            System.out.println(i++ + ") " + p);
        }
    }

    public Patient selectPatient(int index) {
        return patients.values().stream().toList().get(index - 1);
    }

    public List<MedicationSchedule> getSchedules(Patient p) {
        List<MedicationSchedule> result = new ArrayList<>();
        for (MedicationSchedule s : schedules) {
            if (s.patient == p)
                result.add(s);
        }
        return result;
    }

    public void administerMedication(MedicationSchedule schedule, Nurse nurse) {
        System.out.println("Nurse " + nurse.name + " administers medicine");

        if (!pharmacy.given(schedule.medication.medicationID)) {
            schedule.status = MedStatus.OUT_OF_STOCK;
            System.out.println(" Medication is OUT OF STOCK.");
            System.out.println("System has made sure to notify the Pharmacy for meds");
            return;
        }

        schedule.status = MedStatus.GIVEN;
        schedule.administeredBy = nurse.nurseID;

        System.out.println(" Medication has been successfully administered by the nurse.");
        System.out.println("Doctor has been notified about this as well.\n");
    }

    public void holdDueToAllergy(MedicationSchedule schedule, Nurse nurse, String notes) {
        schedule.status = MedStatus.HOLD_ALLERGY;
        schedule.administeredBy = nurse.nurseID;
        schedule.Notes = notes;

        System.out.println("Allergy reaction has been recorded and medication MUST be paused");
        System.out.println("Doctor has been given details of the allergy with notes provided by the nurse. \n");
    }

    public void requestRestock(String medicationID, int amount) {
        pharmacy.restock(medicationID, amount);
        System.out.println("Pharmacy has brought the stock of " + amount + " units for medication.");
    }
}

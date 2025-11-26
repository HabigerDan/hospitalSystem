package medical_services.schedule;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import medical_services.request_test_and_update_patient_record.Doctor;
import medical_services.request_test_and_update_patient_record.Patient;

public class AppointmentController {

    private final List<Patient> patients;
    private final List<Appointment> appointments;

    public AppointmentController(List<Patient> patients) {
        this.patients = patients;
        this.appointments = new ArrayList<>();
    }

   
    public List<Patient> getPatientsForDoctor(Doctor doc) {
        
        return patients;
    }

    
    public List<LocalDateTime> findAvailableSlots(Doctor doc, Patient pat) {
        List<LocalDateTime> slots = new ArrayList<>();
       
        slots.add(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0));
        slots.add(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0).withSecond(0));
        slots.add(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0).withSecond(0));
        return slots;
    }

    // createAppointment() + saveAppointment()
    public Appointment createAppointment(Doctor doc, Patient pat,
                                         LocalDateTime slot, String purpose) {
        Appointment a = new Appointment(doc.getDoctorID(), pat.getPatientID(), slot, purpose);
        appointments.add(a);
        return a;
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }
}

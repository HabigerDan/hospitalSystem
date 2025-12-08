package diagnostic_services.iteration_2;

import roles.EMT;
import roles.Patient;

public class PCR {
  int id;
  Ambulance ambulance;
  Patient patient;
  String notes;

  public PCR(EMT emt1, EMT emt2) {
    this.ambulance = new Ambulance(emt1, emt2);
  }

  public PCR(int id, Ambulance ambulance, Patient patient, String notes) {
    this.id = id;
    this.ambulance = ambulance;
    this.patient = patient;
    this.notes = notes;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public Ambulance getAmbulance() {
    return ambulance;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getNotes() {
    return notes;
  }

  @Override
  public String toString() {
    if (patient != null) {
      return  "PCR ID: " + id +
              "\nEMTs: " + ambulance.emt.get(0).getFirstName() + " " + ambulance.emt.get(0).getLastName() +
              ", " + ambulance.emt.get(1).getFirstName() + " " + ambulance.emt.get(1).getLastName() +
              "\nPatient: " + patient.getFirstName() + " " + patient.getLastName() +
              "\nNotes: " + notes;
    } else {
      return "PCR ID: " + id +
              "\nEMTs: " + ambulance.emt.get(0).getFirstName() + " " + ambulance.emt.get(0).getLastName() +
              ", " + ambulance.emt.get(1).getFirstName() + " " + ambulance.emt.get(1).getLastName() +
              "\nPatient: " + "Null" +
              "\nNotes: " + notes;
    }
  }
}

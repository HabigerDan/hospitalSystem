package diagnostic_services.iteration_2;

import java.util.LinkedHashMap;
import roles.Patient;

public class Sample {
  private int id = 0;
  private final String sampleType;
  private Status status;
  private final Patient patient;
  private final LinkedHashMap<String, String> results = new LinkedHashMap<>();

  public Sample(int id, String sampleType, Patient patient) {
    this.id = id;
    this.sampleType = sampleType;
    this.patient = patient;
    this.status = Status.PENDING;
  }

  public Sample(int id, String sampleType, Status status, Patient patient, LinkedHashMap<String, String> results) {
    this.id = id;
    this.sampleType = sampleType;
    this.status = status;
    this.patient = patient;
    this.results.putAll(results);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setResults(LinkedHashMap<String, String> results) {
    this.results.putAll(results);
  }

  public LinkedHashMap<String, String> getResults() {
    return results;
  }

  public String getSampleType() {
    return sampleType;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return  "Sample Type: " + sampleType + ", Status: " + status + ", Patient: " + patient +
            "\nResults: " + results +
            "\n------------------------";
  }
}

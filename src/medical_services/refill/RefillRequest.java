package medical_services.refill;

import java.time.LocalDateTime;
import java.util.UUID;
import medical_services.request_test_and_update_patient_record.Doctor;


public class RefillRequest {
	 private final UUID refillRequestID;
	    private final Prescription prescription;
	    private final Doctor doctor; 

	    private RefillStatus status;
	    private final LocalDateTime requestDate;
	    private LocalDateTime approvalDate; 

	    public RefillRequest(Prescription prescription, Doctor doctor) {
	        this.refillRequestID = UUID.randomUUID();
	        this.prescription = prescription;
	        this.doctor = doctor;
	        this.status = RefillStatus.PENDING;
	        this.requestDate = LocalDateTime.now();
	        this.approvalDate = null;
	    }

	    public UUID getRefillRequestID() {
	        return refillRequestID;
	    }

	    public Prescription getPrescription() {
	        return prescription;
	    }

	    public Doctor getDoctor() {
	        return doctor;
	    }

	    public RefillStatus getStatus() {
	        return status;
	    }

	    public void setStatus(RefillStatus status) {
	        this.status = status;
	    }

	    public LocalDateTime getRequestDate() {
	        return requestDate;
	    }

	    public LocalDateTime getApprovalDate() {
	        return approvalDate;
	    }

	    public void setApprovalDate(LocalDateTime approvalDate) {
	        this.approvalDate = approvalDate;
	    }

}

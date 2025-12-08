package medical_services.refill;

import java.util.UUID;

public class PharmacyDepartment {
	  private final UUID departmentID;
	    private String name;

	    public PharmacyDepartment(String name) {
	        this.departmentID = UUID.randomUUID();
	        this.name = name;
	    }

	    public UUID getDepartmentID() {
	        return departmentID;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	   //called when refill request is approved
	    public void notifyApprovedRefill(RefillRequest refillRequest) {
	       
	        System.out.println("Pharmacy '" + name + "' notified of approved refill: "
	                + refillRequest.getRefillRequestID()
	                + " for medication "
	                + refillRequest.getPrescription().getMedicationName());
	    }

}

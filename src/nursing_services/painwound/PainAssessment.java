package nursing_services.painwound;
import nursing_services.schedule.Patient;
import nursing_services.schedule.Nurse;
import nursing_services.schedule.MedicalSystem;
import java.util.*;

public class PainAssessment {
    final int painLevel;
    final String location;
    final String painType;
    final String comments;
    final String nurseID;

    public PainAssessment(int painLevel, String location, String painType,
                          String comments, String nurseID) {
        this.painLevel = painLevel;
        this.location = location;
        this.painType = painType;
        this.comments = comments;
        this.nurseID = nurseID;
    }

    @Override
    public String toString() {
        return "Pain Level: " + painLevel + " | Location: " + location + " | Type: " + painType;
    }
}

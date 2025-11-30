package nursing_services.painwound;
import java.util.*;
public class WoundAssessment {
    final String appearance;
    final String size;
    final String drainage;
    final boolean dressingChanged;
    final WoundStatus status;
    final String nurseID;

    public WoundAssessment(String appearance, String size, String drainage,
                           boolean dressingChanged, WoundStatus status, String nurseID) {
        this.appearance = appearance;
        this.size = size;
        this.drainage = drainage;
        this.dressingChanged = dressingChanged;
        this.status = status;
        this.nurseID = nurseID;
    }

    @Override
    public String toString() {
        return "Wound Appearance: " + appearance +
                " | Size: " + size +
                " | Drainage: " + drainage +
                " | Dressing Changed: " + dressingChanged +
                " | Status: " + status;
    }
}

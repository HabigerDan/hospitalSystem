package nursing_services.discharge;

import java.util.List;
import java.util.Scanner;
import nursing_services.schedule.MedicalSystem;
import nursing_services.schedule.Nurse;
import nursing_services.schedule.Patient;
import nursing_services.painwound.PainAssessment;
import nursing_services.painwound.PainWoundSystem;
import nursing_services.painwound.WoundAssessment;

public class DischargeSystem {

    public DischargeSummary processDischarge(
            Patient patient,
            Nurse nurse,
            MedicalSystem system,
            PainWoundSystem painWoundSystem,
            Scanner sc) {

        System.out.println("Starting discharge process for " + patient.getName());

        System.out.print("Are all medication doses cleared for this patient? (yes/no): ");
        boolean medsCleared = sc.nextLine().equalsIgnoreCase("yes");
        if (!medsCleared) return null;

        System.out.print("Enter patient temperature in Fahrenheit: ");
        double temp = Double.parseDouble(sc.nextLine());

        System.out.print("Enter patient blood pressure (systolic): ");
        int bp = Integer.parseInt(sc.nextLine());

        System.out.print("Enter heart rate: ");
        int hr = Integer.parseInt(sc.nextLine());

        System.out.print("Enter oxygen saturation: ");
        int o2 = Integer.parseInt(sc.nextLine());

        VitalSigns vitals = new VitalSigns(temp, bp, hr, o2);
        boolean vitalsSafe = vitals.isWithinSafeRange();
        if (!vitalsSafe) return null;

        System.out.print("Has the doctor approved discharge? (yes/no): ");
        boolean doctorApproved = sc.nextLine().equalsIgnoreCase("yes");
        if (!doctorApproved) return null;

        String pid = patient.getPatientID();

        String woundInfo = "No wound assessments found.";
        List<WoundAssessment> wounds = painWoundSystem.woundLogs.get(pid);
        if (wounds != null && !wounds.isEmpty()) {
            woundInfo = "Wound assessments recorded: " + wounds.size();
        }

        String painInfo = "No pain assessments found.";
        List<PainAssessment> pains = painWoundSystem.painLogs.get(pid);
        if (pains != null && !pains.isEmpty()) {
            painInfo = "Pain assessments recorded: " + pains.size();
        }

        System.out.print("Is a follow up appointment scheduled? (yes/no): ");
        String fu = sc.nextLine();
        FollowUpAppointment followUp = null;

        if (fu.equalsIgnoreCase("yes")) {
            System.out.print("Enter clinic/purpose: ");
            String clinic = sc.nextLine();
            System.out.print("Enter date: ");
            String date = sc.nextLine();
            System.out.print("Enter time: ");
            String time = sc.nextLine();
            System.out.print("Enter status: ");
            String status = sc.nextLine();
            followUp = new FollowUpAppointment(clinic, date, time, status);
        }

        System.out.print("Enter discharge notes: ");
        String notes = sc.nextLine();

        return new DischargeSummary(
                patient,
                nurse,
                medsCleared,
                vitalsSafe,
                doctorApproved,
                vitals,
                woundInfo,
                painInfo,
                notes,
                followUp
        );
    }
}

package nursing_services.discharge;

public class FollowUpAppointment {

    private final String clinicOrPurpose;
    private final String date;
    private final String time;
    private final String status;

    public FollowUpAppointment(String clinicOrPurpose,
                               String date,
                               String time,
                               String status) {
        this.clinicOrPurpose = clinicOrPurpose;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    @Override
    public String toString() {
        return clinicOrPurpose + " on " + date + " at " + time + " (" + status + ")";
    }
}

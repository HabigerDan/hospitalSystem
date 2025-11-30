package nursing_services.discharge;

public class VitalSigns {

    private final double temperature;
    private final int bloodPressure;
    private final int heartRate;
    private final int oxygenSaturation;

    public VitalSigns(double temperature,
                      int bloodPressure,
                      int heartRate,
                      int oxygenSaturation) {
        this.temperature = temperature;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.oxygenSaturation = oxygenSaturation;
    }

    public boolean isWithinSafeRange() {
        boolean tempOk = temperature >= 96.0 && temperature <= 100.4;
        boolean bpOk = bloodPressure >= 90 && bloodPressure <= 140;
        boolean hrOk = heartRate >= 50 && heartRate <= 110;
        boolean o2Ok = oxygenSaturation >= 94;
        return tempOk && bpOk && hrOk && o2Ok;
    }

    @Override
    public String toString() {
        return "Temperature: " + temperature
                + " F, Blood Pressure: " + bloodPressure
                + " mmHg, Heart Rate: " + heartRate
                + " bpm, Oxygen Saturation: " + oxygenSaturation + "%";
    }
}

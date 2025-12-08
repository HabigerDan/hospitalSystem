package food_nutrition_manager;

public class Patient {
    private int patientId;
    private String name;
    private String roomNumber;

    public Patient(int patientId, String name, String roomNumber) {
        this.patientId = patientId;
        this.name = name;
        this.roomNumber = roomNumber;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}
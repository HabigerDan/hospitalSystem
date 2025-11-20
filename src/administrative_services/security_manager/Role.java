package administrative_services.security_manager;

public enum Role {
    DOCTOR("Doctor"),
    NURSE("Nurse"),
    ADMIN("Administrator"),
    IT_SUPPORT("IT Support"),
    SECURITY("Security"),
    RECEPTIONIST("Receptionist"),
    MAINTENANCE("Maintenance"),
    CONTRACTOR("Contractor"),
    VISITOR("Visitor"),
    INTERN("Intern");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getDefaultAccess() {
        switch (this) {
            case DOCTOR:
                return new String[] { "Patient Records", "Lab Systems", "Prescription System", "Medical Floors" };
            case NURSE:
                return new String[] { "Patient Records", "Medication System", "Medical Floors" };
            case ADMIN:
                return new String[] { "HR Systems", "Payroll", "Office Areas" };
            case IT_SUPPORT:
                return new String[] { "Server Room", "Network Systems", "All Workstations" };
            case SECURITY:
                return new String[] { "All Areas", "Security Systems", "Camera Access" };
            case RECEPTIONIST:
                return new String[] { "Reception Systems", "Visitor Management", "Front Desk" };
            case MAINTENANCE:
                return new String[] { "Utility Areas", "Maintenance Systems", "All Floors" };
            case CONTRACTOR:
                return new String[] { "Designated Work Area" };
            case VISITOR:
                return new String[] { "Public Areas", "Meeting Rooms" };
            case INTERN:
                return new String[] { "Training Systems", "Limited Access" };
            default:
                return new String[] {};
        }
    }

    public static void printRoleMenu() {
        Role[] roles = Role.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i].getDisplayName());
        }
    }

    public static Role getByNumber(int number) {
        Role[] roles = Role.values();
        if (number >= 1 && number <= roles.length) {
            return roles[number - 1];
        }
        return null;
    }

    public static int count() {
        return Role.values().length;
    }
}
import administrative_services.onboarding_manager.ConsoleApp;
import administrative_services.scheduling_manager.StaffSchedulingSystem;
import diagnostic_services.DiagMain;
import java.util.Scanner;
import nursing_services.NurMain;
import support_services.equipment_inventory_manager.equipmentInventoryManager;
import support_services.cleaningStaff;

public class Main {
    private static Boolean running;

    public static void main(String[] args) {
        running = true;
        while (running) {
            menu();
        }
    }

    public static void menu() {
        System.out.println("---------------------------------Main Menu---------------------------------");
        System.out.println("1. Administrative_Services");
        System.out.println("2. Diagnostic_Services");
        System.out.println("3. Medical_Services");
        System.out.println("4. Nursigng_Services");
        System.out.println("5. Support_Services");
        System.out.println("---------------------------------------------------------------------------");
        menuOptions();
    }

    public static void menuOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the service you want to enter (1, 2, 3, 4, 5): ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> callAdmistrativeServices();
            case "2" -> callDiagnosticServices();
            case "3" -> callMedicalServices();
            case "4" -> callNursingServices();
            case "5" -> callSupportServices();
            default -> running = false;
        }
    }

    public static void callAdmistrativeServices() {
        Scanner scanner = new Scanner(System.in);
        boolean inAdminMenu = true;

        while (inAdminMenu) {
            System.out.println("----------------------Administrative Services Menu----------------------");
            System.out.println("1. Onboarding Manager");
            System.out.println("2. Security Manager");
            System.out.println("3. Staff Scheduling");
            System.out.println("0. Back to Main Menu");
            System.out.println("------------------------------------------------------------------------");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    administrative_services.onboarding_manager.ConsoleApp.main(new String[] {});
                }
                case "2" -> {
                    administrative_services.security_manager.ConsoleApp.main(new String[] {});
                }
                case "3" -> {
                    StaffSchedulingSystem.main(new String[] {});
                }
                case "0" -> inAdminMenu = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public static void callDiagnosticServices() {
        DiagMain.main(new String[] {});
    }

    public static void callMedicalServices() {
        medical_services.MedicalUseCaseSelector.main(new String[] {});
    }

    public static void callNursingServices() {
        NurMain.main(new String[] {});
    }

    public static void callSupportServices() {
        displaySupportServicesOptions();
    }

    public static void displaySupportServicesOptions() {
        System.out.println("---------------------------------Support Services Menu---------------------------------");
        System.out.println("1. Maintenance Staff");
        System.out.println("2. Cleaning Staff");
        System.out.println("---------------------------------------------------------------------------");
        supportServicesMenu();
    }

    public static void supportServicesMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the manager you want to enter (1, 2, 3): ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> equipmentInventoryManager.main(new String[] {});
            case "2" -> cleaningStaff.main(new String[] {});
            default -> running = false;
        }
    }
}
import java.util.Scanner;
import administrative_services.onboarding_manager.ConsoleApp;
import diagnostic_services.DiagMain;
import medical_services.MedMain;
import nursing_services.NurMain;
import support_services.equipment_inventory_manager.equipmentInventoryManager;

public class Main {
    private static Boolean running;

    public static void main(String [] args) {
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
    	ConsoleApp.main(new String[] {});
    }

    public static void callDiagnosticServices() {
    	DiagMain.main(new String[] {});
    }

    public static void callMedicalServices() {
    	MedMain.main(new String[]{});
    }

    public static void callNursingServices() {
    	NurMain.main(new String[]{});
    }

    public static void callSupportServices() {
        equipmentInventoryManager.main(new String[]{}); 
    }
}
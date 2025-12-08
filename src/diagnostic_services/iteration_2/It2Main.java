package diagnostic_services.iteration_2;

import diagnostic_services.file_system.FilerSystem;
import java.util.Scanner;

public class It2Main {

    public static void pcrReporter(int id, FilerSystem fs, Scanner sc) {
        String command;
        boolean running = true;

        if (fs.isDoctor(id) || fs.isNurse(id)) {
            while (running) {
                System.out.print(
                    """
                    PCR_System
                    1) View all PCRs
                    2) View PCR by ID
                    3) Exit PCR Reporter
                    >\s"""
                );

                command = sc.nextLine();
                System.out.println();

                switch (command) {
                    case "1" -> fs.displayPCR();
                    case "2" -> {
                        while (true) {
                            System.out.print("Enter PCR ID: ");

                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.displayPCR(_id);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "3" -> running = false;
                    default -> System.out.println("Invalid command, please try again.\n");
                }
            }
        } else if (fs.isEMT(id)) {
            while (running) {
                System.out.print(
                    """
                    PCR_System
                    What would you like to do?
                    1) View all PCRs
                    2) View PCR by ID
                    3) Create PCR
                    4) Edit PCR
                    5) Exit PCR Reporter
                    >\s""");

                command = sc.nextLine();
                System.out.println();

                switch (command) {
                    case "1" -> fs.displayPCR();
                    case "2" -> {
                        while (true) {
                            System.out.print("Enter PCR ID: ");
                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.displayPCR(_id);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "3" -> fs.createPCR(id, sc);
                    case "4" -> {
                        while (true) {
                            System.out.print("Enter PCR ID: ");
                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.editPCR(_id, sc);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "5" -> running = false;
                    default -> System.out.println("Invalid command, please try again.\n");
                }
            }
        } else {
            System.out.println("You do not have access to the Sample Manager.\n");
        }
    }

    public static void sampleManager(int id, FilerSystem fs, Scanner sc) {
        String command;
        boolean running = true;

        if (fs.isDoctor(id)) {
            while (running) {
                System.out.print(
                    """
                    Sample_Manager
                    What would you like to do?
                    1) Display Samples
                    2) Display Sample by ID
                    3) Submit Sample
                    4) Exit Sample Manager
                    >\s"""
                );

                command = sc.nextLine();
                System.out.println();

                switch (command) {
                    case "1" -> fs.displaySample();
                    case "2" -> {
                        while (true) {
                            System.out.print("Enter Sample ID: ");
                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.displaySample(_id);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "3" -> {
                        while (true) {
                            try {
                                fs.submitSample(sc);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "4" -> running = false;
                    default -> System.out.println("Invalid command, please try again.\n");
                }
            }
        } else if (fs.isNurse(id)) {
            while (running) {
                System.out.print(
                    """
                    Sample_Manager
                    What would you like to do?
                    1) Display Samples
                    2) Display Sample by ID
                    3) Exit Sample Manager
                    >\s"""
                );

                command = sc.nextLine();
                System.out.println();

                switch (command) {
                    case "1" -> fs.displaySample();
                    case "2" -> {
                        while (true) {
                            System.out.print("Enter Sample ID: ");
                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.displaySample(_id);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "3" -> running = false;
                    default -> System.out.println("Invalid command, please try again.\n");
                }
            }

        } else if (fs.isLabTech(id)) {
            while (running) {
                System.out.print(
                    """
                    Sample_Manager
                    What would you like to do?
                    1) Display Samples
                    2) View Sample by ID
                    3) Edit Sample
                    4) Complete Sample
                    5) Cancel Sample
                    6) Exit Sample Manager
                    >\s"""
                );

                command = sc.nextLine();
                System.out.println();

                switch (command) {
                    case "1" -> fs.displaySample();
                    case "2" -> {
                        while (true) {
                            System.out.print("Enter Sample ID: ");
                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.displaySample(_id);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "3" -> {
                        while (true) {
                            System.out.print("Enter Sample ID: ");
                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.editSample(_id);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "4" -> {
                        while (true) {
                            System.out.print("Enter Sample ID: ");
                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.completeSample(_id);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "5" -> {
                        while (true) {
                            System.out.print("Enter Sample ID: ");
                            String _in = sc.nextLine();
                            System.out.println();

                            try {
                                int _id = Integer.parseInt(_in);
                                fs.cancelSample(_id);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid ID, please try again.\n");
                            }
                        }
                    }
                    case "6" -> running = false;
                    default -> System.out.println("Invalid command, please try again.\n");
                }
            }
        } else {
            System.out.println("You do not have access to the Pre-Hopsital Care Reporting.\n");
        }
    }
}
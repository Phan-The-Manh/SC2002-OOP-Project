package controller;
import java.util.Scanner;
public class OfficerAction {
    public void officerAction(){
        HDBOfficerController officerController = new HDBOfficerController();
        boolean continueActions = true;
        Scanner scanner = new Scanner(System.in);
        while (continueActions) {
            System.out.println("\n===== List of Actions =====");
            System.out.println("1. Applicant Actions");
            System.out.println("2. Officer Actions");
            System.out.println("3. Logout");
            System.out.print("Choose an action: ");

            // Check if the input is an integer
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice){
                    case 1: 
                        boolean continueApplicantActions = true;
                        while (continueApplicantActions){
                            System.out.println("\n===== Applicant Actions =====");
                            System.out.println("1. View Available Projects");
                            System.out.println("2. Apply for a Project");
                            System.out.println("3. View My Application Status");
                            System.out.println("4. Request Withdrawal of Application");
                            System.out.println("5. Submit an Enquiry");
                            System.out.println("6. View/Edit/Delete My Enquiries");
                            System.out.println("7. Back");
            
                            System.out.print("Choose an action: ");
            
                            // Check if the input is an integer
                            if (scanner.hasNextInt()) {
                                int applicantChoice = scanner.nextInt();
                                scanner.nextLine(); // Consume newline
                                switch (applicantChoice) {
                                    case 1:
                                        officerController.viewAvailableProjects();
                                        break;
                                    case 2:
                                        officerController.applyForProject();
                                        break;
                                    case 3:
                                        officerController.viewApplicationStatus();
                                        break;
                                    case 4:
                                        officerController.requestWithdrawal();
                                        break;
                                    case 5:
                                        officerController.submitEnquiry();
                                        break;
                                    case 6:
                                        System.out.println("\n===== Enquiry Actions =====");
                                        System.out.println("1. View Enquiries");
                                        System.out.println("2. Edit Enquiry");
                                        System.out.println("3. Delete Enquiry");
                                        int enquiryChoice;
                                        while (true) {
                                            System.out.print("Enter your enquiry choice: ");
                                            if (scanner.hasNextInt()) {
                                                enquiryChoice = scanner.nextInt();
                                                break; // valid input, exit loop
                                            } else {
                                                System.out.println("Invalid input. Please enter a valid choice.");
                                                scanner.next(); // consume the invalid input
                                            }
                                        }
                                        switch(enquiryChoice){
                                            case 1:
                                                officerController.viewEnquiry();
                                                break;
                                            case 2:
                                                officerController.editEnquiry();
                                                break;
                                            case 3:
                                                officerController.deleteEnquiry();
                                                break;
                                            default:
                                                System.out.println("Invalid choice. Please try again.");
                                        }
                                        break;
            
                                    case 7:
                                        System.out.println("Back");
                                        continueApplicantActions = false;
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                }
                            } else {
                                System.out.println("❌ Invalid input. Please enter a number (1-7).");
                                scanner.nextLine(); // Clear invalid input
                            }                 
                        }
                        break;
                    case 2: 
                        boolean continueOfficerActions = true;
                        while (continueOfficerActions){
                            System.out.println("\n===== Officer Actions =====");
                            System.out.println("1. Register for a Project");
                            System.out.println("2. View Registration Status");
                            System.out.println("3. View Application by Status");
                            System.out.println("4. Approve Flat Booking");
                            System.out.println("5. Generate Receipt");
                            System.out.println("6. View Project Enquiry");
                            System.out.println("7. Reply to Enquiry");
                            System.out.println("8. Back");
            
                            System.out.print("Choose an action: ");
            
                            // Check if the input is an integer
                            if (scanner.hasNextInt()) {
                                int applicantChoice = scanner.nextInt();
                                scanner.nextLine(); // Consume newline
                                switch (applicantChoice) {
                                    case 1:
                                        officerController.registerForProject();
                                        break;
                                    case 2:
                                        officerController.viewRegistrationStatus();
                                        break;
                                    case 3:
                                        officerController.viewApplicationByStatus();
                                        break;
                                    case 4:
                                        //officerController.approveFlatBooking();
                                        break;
                                    case 5:
                                        officerController.generateReceipt();
                                        break;
                                    case 6:
                                        officerController.viewProjectEnquiry();
                                        break;
                                    case 7:
                                        officerController.replyToEnquiry();
                                        break;
                                    case 8:
                                        System.out.println("Back");
                                        continueOfficerActions = false;
                                        break;
                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                }
                            } else {
                                System.out.println("❌ Invalid input. Please enter a number (1-7).");
                                scanner.nextLine(); // Clear invalid input
                            }                 
                        }
                        break;
                    case 3:
                        System.out.println("Logging out");
                        continueActions = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            
            } else {
                System.out.println("❌ Invalid input. Please enter a number (1-3).");
                scanner.nextLine(); // Clear invalid input
            }     
        }
    }
}

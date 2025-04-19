package controller;

import java.util.Scanner;

public class ApplicantAction {
    public void applicantAction() {
        ApplicantController applicantController = new ApplicantController();
        Scanner scanner = new Scanner(System.in);
        boolean continueActions = true;

        while (continueActions) {
            System.out.println("\n===== Applicant Actions =====");
            System.out.println("1. View Available Projects");
            System.out.println("2. Apply for a Project");
            System.out.println("3. View My Application Status");
            System.out.println("4. Request Withdrawal of Application");
            System.out.println("5. Submit an Enquiry");
            System.out.println("6. View/Edit/Delete My Enquiries");
            System.out.println("7. Logout");

            System.out.print("Choose an action: ");

            // Check if the input is an integer
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        applicantController.viewAvailableProjects();
                        break;
                    case 2:
                        applicantController.applyForProject();
                        break;
                    case 3:
                        applicantController.viewApplicationStatus();
                        break;
                    case 4:
                        applicantController.requestWithdrawal();
                        break;
                    case 5:
                        applicantController.submitEnquiry();
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
                                applicantController.viewEnquiry();
                                break;
                            case 2:
                                applicantController.editEnquiry();
                                break;
                            case 3:
                                applicantController.deleteEnquiry();
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        break;

                    case 7:
                        System.out.println("Logging out...");
                        continueActions = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("❌ Invalid input. Please enter a number (1-7).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}

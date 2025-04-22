package controller;

import java.util.Scanner;

public class ManagerAction {
    public void managerAction() {
        HDBManagerController managerController = new HDBManagerController();
        Scanner scanner = new Scanner(System.in);
        boolean continueActions = true;

        while (continueActions) {
            System.out.println("\n===== HDB Manager Actions =====");
            System.out.println("1. View All Projects");
            System.out.println("2. Manage Project (Create, Edit, Delete)");
            System.out.println("3. Toggle Project Visibility");
            System.out.println("4. Filter Created Projects by Application Period");
            System.out.println("5. View HDB Officer Registration");
            System.out.println("6. Approve/Reject HDB Officer Registration");
            System.out.println("7. Approve/Reject Applicant BTO Application");
            System.out.println("8. Approve/Reject Applicant Withdrawal Request");
            System.out.println("9. Generate Report");
            System.out.println("10. View Enquiries for All Projects");
            System.out.println("11. Reply to Enquiries");
            System.out.println("12. Logout");

            System.out.print("Choose an action: ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        managerController.viewAvailableProjects();
                        break;
                    case 2:
                        boolean managingProjects = true;
                        while (managingProjects) {
                            System.out.println("\n--- Manage Project ---");
                            System.out.println("1. Create Project");
                            System.out.println("2. Edit Project");
                            System.out.println("3. Delete Project");
                            System.out.println("4. Back to Main Menu");
                            System.out.print("Choose an option: ");
                            if (scanner.hasNextInt()) {
                                int subChoice = scanner.nextInt();
                                scanner.nextLine(); // consume newline
                                switch (subChoice) {
                                    case 1:
                                        managerController.createProject();
                                        break;
                                    case 2:
                                        managerController.editProject();
                                        break;
                                    case 3:
                                        managerController.deleteProject();
                                        break;
                                    case 4:
                                        managingProjects = false;
                                        break;
                                    default:
                                        System.out.println("Invalid option. Try again.");
                                }
                            } else {
                                System.out.println("Invalid input. Please enter a number (1-4).");
                                scanner.nextLine(); // clear buffer
                            }
                        }
                        break;
                    case 3:
                        managerController.toggleProjectVisibility();
                        break;
                    case 4:
                        managerController.filterProject();
                        break;
                    case 5:
                        managerController.viewOfficerRegistrations();
                        break;
                    case 6:
                        managerController.manageHDBOfficerRegistration();
                        break;
                    case 7:
                        managerController.manageApplicantApplication();
                        break;
                    case 8:
                        managerController.manageWithdrawalRequest();
                        break;
                    case 9:
                        managerController.generateReport();
                        break;
                    case 10:
                        managerController.viewEnquiries();
                        break;
                    case 11:
                        managerController.replyToEnquiry();
                        break;
                    case 12:
                        System.out.println("Logging out...");
                        continueActions = false;
                        break;                    
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1-12).");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}

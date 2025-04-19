package controller;

import java.util.Scanner;

public class ChooseRole {

    public void chooseRoleAndLogin() {
        boolean loginSuccessful = false;
        Scanner scanner = new Scanner(System.in);

        while (!loginSuccessful) {
            int choice = -1;

            // Keep prompting until a valid choice is made
            while (choice < 1 || choice > 3) {
                System.out.println("Please choose your role:");
                System.out.println("1. Applicant");
                System.out.println("2. Officer");
                System.out.println("3. Manager");

                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                } else {
                    scanner.nextLine(); // clear invalid input
                    System.out.println("❌ Invalid input. Please enter a number from 1 to 3.");
                }
            }

            // Call the corresponding login method based on user choice
            switch (choice) {
                case 1:
                    System.out.println("You selected: Applicant");
                    ApplicantController applicantController = new ApplicantController();
                    loginSuccessful = applicantController.login();
                    if (loginSuccessful) {
                        ApplicantAction applicantAction = new ApplicantAction();
                        applicantAction.applicantAction();
                    }
                    break;

                case 2:
                    // System.out.println("You selected: Officer");
                    // HDBOfficerController officerController = new HDBOfficerController();
                    // loginSuccessful = officerController.login();
                    break;

                case 3:
                    System.out.println("You selected: Manager");
                    HDBManagerController managerController = new HDBManagerController();
                    loginSuccessful = managerController.login();
                    if (loginSuccessful) {
                        ManagerAction managerAction = new ManagerAction();
                        managerAction.managerAction();
                    }
                    break;

                default:
                    System.out.println("❌ Invalid choice. Please select a valid role.");
            }
        }

        scanner.close();
    }
}

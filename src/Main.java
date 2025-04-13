import controller.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for role selection
        System.out.println("Please choose your role:");
        System.out.println("1. Applicant");
        System.out.println("2. Officer");
        System.out.println("3. Manager");

        int choice = scanner.nextInt();  // Get user input
        scanner.nextLine();  // Consume the leftover newline

        switch (choice) {
            case 1:
                System.out.println("You selected: Applicant");
                // Call ApplicantController login method
                ApplicantController applicantController = new ApplicantController();
                applicantController.login();
                break;

            case 2:
                System.out.println("You selected: Officer");
                // Call OfficerController login method (similar implementation)
                HDBOfficerController officerController = new HDBOfficerController();
                officerController.login();
                break;

            case 3:
                System.out.println("You selected: Manager");
                // Call ManagerController login method (similar implementation)
                HDBManagerController managerController = new HDBManagerController();
                managerController.login();
                break;

            default:
                System.out.println("❌ Invalid choice. Please select a valid role.");
        }

        scanner.close();
    }
}


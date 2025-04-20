package controller;

import java.util.Scanner;
import model.Applicant;
import model.CurrentUser;
import service.*;

public class ApplicantController {

    private final ApplicantService applicantService;

    public ApplicantController() {
        this.applicantService = new ApplicantServiceImpl();  // Instantiate the service
    }

    Scanner scanner = new Scanner(System.in);

    public boolean login() {
        boolean loginSuccessful = false;
    
        while (!loginSuccessful) {
            // Prompt for login details
            System.out.print("Enter NRIC: ");
            String nric = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
    
            // Attempt login
            Applicant loggedInApplicant = applicantService.checkLogin(nric, password);
    
            if (loggedInApplicant != null) {
                // Store current user on success
                CurrentUser.<Applicant>getInstance().setUser(loggedInApplicant);
                loginSuccessful = true;  // Break the loop naturally
                return true;
            } else {
                // Prompt to retry only if login failed
                System.out.print("Do you want to try again? (y/n): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("n")) {
                    return false;
                }
            }
        }
        return false;
    }
    

    public void viewAvailableProjects() {
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
        applicantService.viewAvailableProjects(applicant); 
    }

    public void applyForProject() {
    
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("⚠️ No applicant is currently logged in.");
            return;
        }
    
        if (applicant.getApplications() != null){
            System.out.println("The applicant has already applied for a project.");
            return;
        }
        
        // Prompt for project name (ensure it's not blank)
        String projectName;
        do {
            System.out.print("Enter the project name you want to apply for: ");
            projectName = scanner.nextLine().trim();
            if (projectName.isEmpty()) {
                System.out.println("Project name cannot be empty. Please try again.");
            }
        } while (projectName.isEmpty());
        
        // Prompt for roomType
        int roomType;
        while (true) {
            System.out.print("Enter your room type(1. 2-room | 2. 3-room): ");
            if (scanner.hasNextInt()) {
                roomType = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character after reading int
                break; // valid input, exit loop
            } else {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.next(); // consume the invalid input
            }
        }
    
        // Delegate to service
        applicantService.applyForProject(applicant, projectName, roomType);
    }
    

    void viewApplicationStatus() {
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("⚠️ No applicant is currently logged in.");
            return;
        }
    
        // Delegate to service
        applicantService.viewApplicationStatus(applicant);
    }

    void requestWithdrawal() {
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("No applicant is currently logged in.");
            return;
        }
        if (applicant.getApplications() == null){
            System.out.println("The applicant has not applied for any project.");
            return;
        }
        // Delegate to service
        applicantService.requestWithdrawal(applicant);
    }

    void submitEnquiry() {
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("No applicant is currently logged in.");
            return;
        }
    
        String projectName;
        do {
            System.out.print("Enter your project name: ");
            projectName = scanner.nextLine().trim();
            if (projectName.isEmpty()) {
                System.out.println("Project name cannot be empty. Please try again.");
            }
        } while (projectName.isEmpty());

                // Prompt for enquiry text (ensure it's not blank)
                String enquiryText;
                do {
                    System.out.print("Enter your enquiry: ");
                    enquiryText = scanner.nextLine().trim();
                    if (enquiryText.isEmpty()) {
                        System.out.println("Enquiry cannot be empty. Please try again.");
                    }
                } while (enquiryText.isEmpty());
    
        // Delegate to service
        applicantService.submitEnquiry(applicant, enquiryText, projectName);
    }

    void editEnquiry(){
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("No applicant is currently logged in.");
            return;
        } 
       // Prompt for enquiry index and text
        String newText;
        int index;
        while (true) {
            System.out.print("Enter your enquiry index: ");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt();
                scanner.nextLine();
                break; // valid input, exit loop
            } else {
                System.out.println("Invalid input. Please enter a valid index.");
                scanner.nextLine(); // consume the invalid input
            }
        }
        do {
           System.out.print("Enter your edited enquiry: ");
           newText = scanner.nextLine().trim();
           if (newText.isEmpty()) {
               System.out.println("Enquiry cannot be empty. Please try again.");
           }
        } while (newText.isEmpty());
   
       // Delegate to service
       applicantService.editEnquiry(applicant, index, newText);       
    }

    void viewEnquiry(){
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("No applicant is currently logged in.");
            return;
        } 

        applicantService.viewEnquiry(applicant);
    }

    void deleteEnquiry(){
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("No applicant is currently logged in.");
            return;
        } 
       // Prompt for enquiry index
        int index;
        while (true) {
            System.out.print("Enter your enquiry index: ");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt();
                break; // valid input, exit loop
            } else {
                System.out.println("Invalid input. Please enter a valid index.");
                scanner.next(); // consume the invalid input
            }
        }
       // Delegate to service
       applicantService.deleteEnquiry(applicant, index);       
    }     
    
}

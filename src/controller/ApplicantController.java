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

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
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
        applicantService.viewAvailableProjects();  // Call the method to view available projects
    }

    public void applyForProject() {
        Scanner scanner = new Scanner(System.in);
    
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("⚠️ No applicant is currently logged in.");
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
    
        // Delegate to service
        applicantService.applyForProject(applicant, projectName);
    }
    

}

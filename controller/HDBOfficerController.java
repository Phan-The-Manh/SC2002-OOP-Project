package controller;

import data.*;
import java.util.Scanner;
import model.*;
import service.*;

public class HDBOfficerController {

    private final HDBOfficerService hdbOfficerService;
    
    public HDBOfficerController() {
        this.hdbOfficerService = new HDBOfficerServiceImpl(); 
    }

    Scanner scanner = new Scanner(System.in);

    public boolean login() {
        boolean loginSuccessful = false;
        while (!loginSuccessful) {
            System.out.print("Enter Officer ID: ");
            String nric = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();        
            // Attempt login
            Applicant loggedInOfficer = hdbOfficerService.checkLogin(nric, password);
            if (loggedInOfficer != null) {
                CurrentUser.<Applicant>getInstance().setUser(loggedInOfficer);
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
        hdbOfficerService.viewAvailableProjects(applicant); 
    }

    public void applyForProject() {
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("⚠️ No applicant is currently logged in.");
            return;
        }
    
        if (applicant.getApplications() != null) {
            System.out.println("⚠️ You have already applied for a project. You can only apply once.");
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
            System.out.print("Enter your room type (1. 2-room | 2. 3-room): ");
            if (scanner.hasNextInt()) {
                roomType = scanner.nextInt();
                scanner.nextLine(); // 🔑 consume the leftover newline
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid number (1 or 2).");
                scanner.next(); // consume invalid input
            }
        }
    
        // Delegate to service
        hdbOfficerService.applyForProject(applicant, projectName, roomType);
    }
    

    void viewApplicationStatus() {
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("⚠️ No applicant is currently logged in.");
            return;
        }
    
        // Delegate to service
        hdbOfficerService.viewApplicationStatus(applicant);
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
        hdbOfficerService.requestWithdrawal(applicant);
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
        hdbOfficerService.submitEnquiry(applicant, enquiryText, projectName);
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
       hdbOfficerService.editEnquiry(applicant, index, newText);       
    }

    void viewEnquiry(){
        // Get current user
        Applicant applicant = CurrentUser.<Applicant>getInstance().getUser();
    
        if (applicant == null) {
            System.out.println("No applicant is currently logged in.");
            return;
        } 

        hdbOfficerService.viewEnquiry(applicant);
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
       hdbOfficerService.deleteEnquiry(applicant, index);       
    }   

    void viewApplicationByStatus(){
        HDBOfficer officer = (HDBOfficer)(CurrentUser.<Applicant>getInstance().getUser());
        String status;
        do {
            System.out.print("Enter application status to view: ");
            status = scanner.nextLine().trim();
            if (status.isEmpty()) {
                System.out.println("Status cannot be empty. Please try again.");
            }
        } while (status.isEmpty());
        hdbOfficerService.viewApplicationByStatus(officer, status);
    }

    void registerForProject(){
        // Add code
    }
    
    void viewRegistrationStatus(){
        // Add code
    }

    void approveFlatBooking(){
        // Add code
    }

    void viewProjectEnquiry(){
        HDBOfficer officer = (HDBOfficer)(CurrentUser.<Applicant>getInstance().getUser());
        hdbOfficerService.viewProjectEnquiry(officer);
    }

    void replyToEnquiry(){
        HDBOfficer officer = (HDBOfficer)(CurrentUser.<Applicant>getInstance().getUser());
        
        // Prompt for enquiry index
        int index;
        String replyText;
        while (true) {
            System.out.print("Enter enquiry index to reply: ");
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
           System.out.print("Enter your reply: ");
           replyText = scanner.nextLine().trim();
           if (replyText.isEmpty()) {
               System.out.println("Reply text cannot be empty. Please try again.");
           }
        } while (replyText.isEmpty());
   
       // Delegate to service
       hdbOfficerService.replyToEnquiry(officer, index, replyText);  
    }

    void generateReceipt(){
        Database db = new Database();
        String NRIC;
        do {
            System.out.print("Enter the NRIC of the applicant you want to generate receipt: ");
            NRIC = scanner.nextLine().trim();
            if (NRIC.isEmpty()) {
                System.out.println("Enquiry cannot be empty. Please try again.");
            }
        } while (NRIC.isEmpty());

        Applicant applicant = null;
        for (Applicant existingApplicant : db.applicantList){
            if(existingApplicant.getNRIC().equalsIgnoreCase(NRIC)){
                applicant = existingApplicant;
            }
        }

        for (Applicant existingApplicant : db.hdbOfficerList){
            if(existingApplicant.getNRIC().equalsIgnoreCase(NRIC)){
                applicant = existingApplicant;
            }
        }

        if (applicant != null){
            hdbOfficerService.generateReceipt(applicant, applicant.getApplications());
        }
        else{
            System.out.println("Applicant not found");
        }
    }






}
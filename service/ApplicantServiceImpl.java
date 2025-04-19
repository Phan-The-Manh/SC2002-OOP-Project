package service;

import data.Database;
import java.util.List;
import model.*;

public class ApplicantServiceImpl implements ApplicantService {
    private DataHandler dataHandler;

    public ApplicantServiceImpl() {
        this.dataHandler = new DataHandlerImpl(new Database());
    }

    @Override
    public Applicant checkLogin(String userId, String password) {
        Database db = new Database();
        boolean userFound = false;  // Flag to check if the user exists
        boolean passwordCorrect = false;  // Flag to check if the password is correct
    
        // Ensure userId and password are trimmed to avoid issues with spaces
        if (userId != null) {
            userId = userId.trim();
        }
        if (password != null) {
            password = password.trim();
        }
    
        // Check in Applicants list from the Database
        for (Applicant applicant : db.applicantList) {
            // Add debug log to see what values are being compared
            System.out.println("Comparing with Applicant: " + applicant.getUserId() + " - " + applicant.getPassword());
            
            if (applicant.getUserId().equals(userId)) {
                userFound = true;  // User exists in the Applicants list
                if (applicant.getPassword().equals(password)) {
                    passwordCorrect = true;  // Correct password
                    System.out.println("Login successful for Applicant: " + applicant.getName());
                    return applicant;
                }
                break;  // Stop after finding the user
            }
        }
    
        // Handle cases where user or password is incorrect
        if (!userFound) {
            System.out.println("No applicant found with the given NRIC.");
        } else if (!passwordCorrect) {
            System.out.println("Incorrect password. Please try again.");
        }
        return null;
    }
    

    @Override
    public void viewAvailableProjects() {
        Database db = new Database();
        db.getBTOProjectList();  // Fetch available projects from the database
    }

    @Override
    public void applyForProject(Applicant applicant, String projectName) {
        Database db = new Database();
        List<BTOProject> projectList = db.btoProjectList;
    
        // Find project by name
        BTOProject project = null;
        for (BTOProject p : projectList) {
            if (p.getProjectName().equalsIgnoreCase(projectName.trim())) {
                project = p;
                break;
            }
        }
    
        if (project != null) {
            // Check if the applicant has already applied for this project
            for (BTOApplication app : applicant.getApplications()) {
                if (app.getProjectName().equalsIgnoreCase(projectName)) {
                    System.out.println("You have already applied for this project.");
                    return;
                }
            }
            BTOApplication application = new BTOApplication(applicant, project.getProjectName());
            applicant.getApplications().add(application);
            System.out.println("Application submitted for project: " + projectName);
            dataHandler.saveBTOApplication(application);

            // BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
            // btoApplicationService.saveBTOApplication(application);
            System.out.println(application);
    
        } else {
            System.out.println("Project not found: " + projectName);
        }
    }
    


    @Override
    public void viewApplicationStatus(Applicant applicant) {
        // Check if the applicant has any applications
        if (applicant.getApplications().isEmpty()) {
            System.out.println("No applications found for this applicant.");
        } else {
            // Print the header of the table
            System.out.println(String.format("%-20s %-20s", "Project Name", "Application Status"));
            System.out.println("-----------------------------------------------");
        
            // Loop through all applications and print the details in a table-like format
            for (BTOApplication application : applicant.getApplications()) {
                System.out.println(String.format("%-20s %-20s", application.getProjectName(), application.getStatus()));
            }
        }
    }
    
    


    @Override
    public void requestWithdrawal(Applicant applicant, String projectName) {
        // Find the application by project name
        BTOApplication application = applicant.getApplications().stream()
            .filter(app -> app.getProjectName().equalsIgnoreCase(projectName))
            .findFirst()
            .orElse(null);
    
        if (application != null) {
            application.setStatus("Pending Withdrawal");  // Update status to pending withdrawal
            application.setRequestedWithdrawal(true);
            dataHandler.saveBTOApplication(application); 
            System.out.println("\nApplication withdrawn successfully!");
            System.out.println("---------------------------------------------");
            System.out.printf("%-20s %-20s\n", "Project Name", "Status");
            System.out.printf("%-20s %-20s\n", application.getProjectName(), application.getStatus());
        } else {
            System.out.println("No application found for project: " + projectName);
        }
    }
    
    
    @Override
    public void submitEnquiry(Applicant applicant, String enquiryText) {
        Enquiry enquiry = new Enquiry(applicant.getUserId(), enquiryText);
        enquiry.setEnquiryDate(new java.sql.Date(System.currentTimeMillis()));
        enquiry.setReply(""); // No reply yet
        enquiry.setReplied(false);
        applicant.addEnquiry(enquiry);
        Database db = new Database();
        EnquiryServiceImpl enquiryService = new EnquiryServiceImpl(db); 
        enquiryService.saveEnquiry(enquiry); // Save the enquiry to the database
        System.out.println("\nEnquiry submitted successfully!");
        System.out.println("---------------------------------------------");
        System.out.println(enquiry.toString());
    }

    @Override
    public void viewEnquiry(Applicant applicant){
        System.out.println("\nList of Enquiries");
        System.out.println("---------------------------------------------");
        for (int i = 0; i < applicant.getEnquiries().size(); i++){
                System.out.println("Index: " + i +" | " + applicant.getEnquiries().get(i).toString());
        }
    }
    
    @Override
    public void editEnquiry(Applicant applicant, int index, String newText) {
        Database db = new Database();
        if (index >= 0 && index < applicant.getEnquiries().size()) {
            Enquiry enquiry = applicant.getEnquiries().get(index);
            EnquiryServiceImpl enquiryService = new EnquiryServiceImpl(db);
            enquiryService.editEnquiry(enquiry,newText);
            enquiry.setEnquiryText(newText);
            applicant.getEnquiries().set(index, enquiry);
            System.out.println("Enquiry updated: " + newText);
        } else {
            System.out.println("Invalid enquiry index.");
        }
    }

    @Override
    public void deleteEnquiry(Applicant applicant, int index) {
        Database db = new Database();
        if (index >= 0 && index < applicant.getEnquiries().size()) {
            Enquiry enquiryToDelete = applicant.getEnquiries().get(index);
            applicant.getEnquiries().remove(index);
            EnquiryServiceImpl enquiryService = new EnquiryServiceImpl(db);
            enquiryService.deleteEnquiry(enquiryToDelete);
            System.out.println("Enquiry deleted at index: " + index);
        } else {
            System.out.println("Invalid enquiry index.");
        }
    }
}
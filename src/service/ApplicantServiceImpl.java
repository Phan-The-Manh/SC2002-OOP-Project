package service;

import data.Database;
import java.util.List;
import model.*;

public class ApplicantServiceImpl implements ApplicantService {

    @Override
    public Applicant checkLogin(String userId, String password) {
        Database db = new Database();
        boolean userFound = false;  // Flag to check if the user exists
        boolean passwordCorrect = false;  // Flag to check if the password is correct

        // Check in Applicants list from the Database
        for (Applicant applicant : db.applicantList) {
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
            BTOApplication application = new BTOApplication(applicant, project.getProjectName());  // Create a new application
            applicant.getApplications().add(application);  // Add application to the applicant's list
            System.out.println("Application submitted for project: " + projectName);
            BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
            btoApplicationService.saveBTOApplication(application);
            System.out.println(application);  // Print application details
        } else {
            System.out.println("Project not found: " + projectName);
        }        
    }
    

    @Override
    public void requestWithdrawal(Applicant applicant, BTOApplication application) {
        application.requestWithdrawal();
        System.out.println("Application withdrawn: " + application.toString());
    }

    @Override
    public void submitEnquiry(Applicant applicant, String enquiryText) {
        Enquiry enquiry = new Enquiry(enquiryText);
        applicant.addEnquiry(enquiry);
        System.out.println("Enquiry submitted: " + enquiryText);
    }

    @Override
    public void editEnquiry(Applicant applicant, int index, String newText) {
        if (index >= 0 && index < applicant.getEnquiries().size()) {
            Enquiry enquiry = applicant.getEnquiries().get(index);
            enquiry.setEnquiryText(newText);
            System.out.println("Enquiry updated: " + newText);
        } else {
            System.out.println("Invalid enquiry index.");
        }
    }

    @Override
    public void deleteEnquiry(Applicant applicant, int index) {
        if (index >= 0 && index < applicant.getEnquiries().size()) {
            applicant.getEnquiries().remove(index);
            System.out.println("Enquiry deleted at index: " + index);
        } else {
            System.out.println("Invalid enquiry index.");
        }
    }

    @Override
    public void viewApplicationStatus(Applicant applicant) {
        for (BTOApplication application : applicant.getApplications()) {
            System.out.println(application.toString());
        }
    }
}
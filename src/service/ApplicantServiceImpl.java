package service;

import data.Database;
import java.util.List;
import java.util.Scanner;
import model.*;

public class ApplicantServiceImpl implements ApplicantService {
    private Database db;

    public ApplicantServiceImpl() {
        this.db = new Database();
    }

    @Override
    public Applicant checkLogin(String userId, String password) {
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
    public void viewAvailableProjects(Applicant applicant) {
        if (applicant.getMaritalStatus().equals("Single") && applicant.getAge() >= 35){
            db.getBTOProjectListByRoomType(1);  // Fetch available projects for singles aged 35 and above
        }
        else 
            db.getBTOProjectList();  // Fetch available projects from the database*/
    }

    @Override
    public void viewAvailableProjectsByRoomType(Applicant applicant, int roomType){
        switch(roomType){
            case 1: 
                db.getBTOProjectListByRoomType(1);
                break;
            case 2:
                if (applicant.getMaritalStatus().equals("Single") && applicant.getAge() >= 35){
                    System.out.println("You cannot view this room type");
                }
                else{
                    db.getBTOProjectListByRoomType(2);
                }
                break;
            default:
                System.out.println("Invalid Room Type");
                break;
        }
    }

    @Override
    public void applyForProject(Applicant applicant, String projectName, int roomType) {
        List<BTOProject> projectList = db.btoProjectList;

        // Kiểm tra điều kiện không hợp lệ với người độc thân
        if (applicant.getMaritalStatus().equalsIgnoreCase("Single") && applicant.getAge() >= 35 && roomType == 2) {
            System.out.println("Room type is not applicable for single applicants aged 35 and above.");
            return;
        }

        // Tìm project theo tên
        BTOProject project = null;
        for (BTOProject p : projectList) {
            if (p.getProjectName().equalsIgnoreCase(projectName.trim())) {
                project = p;
                break;
            }
        }

        if (project == null) {
            System.out.println("Project not found: " + projectName);
            return;
        }

        // Kiểm tra project có visible không
        if (!project.isVisible()) {
            System.out.println("The selected project is not currently available for application.");
            return;
        }

        // Tiến hành tạo application
        BTOApplication application = new BTOApplication(applicant, project.getProjectName());
        application.setStatus("Pending");
        application.setRequestedWithdrawal(false);
        application.setRoomType(roomType);

        applicant.setApplications(application);
        BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
        btoApplicationService.saveBTOApplication(application);

        // Hiển thị thông tin xác nhận
        System.out.println("\nApplication applied successfully!");
        System.out.println("---------------------------------------------");
        System.out.printf("%-20s %-20s %-20s\n", "Project Name", "Room Type", "Status");
        System.out.printf("%-20s %-20d %-20s\n", application.getProjectName(), application.getRoomType(), application.getStatus());
    }

    


    @Override
    public void viewApplicationStatus(Applicant applicant) {
        // Check if the applicant has any applications
        if (applicant.getApplications() == null) {
            System.out.println("No applications found for you.");
        } else {
            // Print the header of the table
            System.out.println(String.format("%-20s %-20s", "Project Name", "Application Status"));
            System.out.println("-----------------------------------------------");
            BTOApplication application = applicant.getApplications();
            System.out.println(String.format("%-20s %-20s", application.getProjectName(), application.getStatus()));
            if(application.getStatus().equals("Successful")){
                System.out.print("Do you want to request booking for this project? (y/n): ");
                Scanner sc = new Scanner(System.in);
                String response = sc.next();
                if (response.equalsIgnoreCase("y")) {
                    application.setStatus("Pending Booking");
                    BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
                    btoApplicationService.saveBTOApplication(application);
                    System.out.println("Request booking for project: " + application.getProjectName());
                }             
            }
        }
        
    }


    @Override
    public void requestWithdrawal(Applicant applicant) {
        // Get the application associated with the applicant
        BTOApplication application = applicant.getApplications();
        // Update the application status and set withdrawal request flag
        application.setStatus("Pending Withdrawal");
        application.setRequestedWithdrawal(true);

        // Save the updated application
        BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
        btoApplicationService.saveBTOApplication(application);

        // Provide feedback to the user
        System.out.println("\nApplication withdrawn successfully!");
        System.out.println("---------------------------------------------");
        System.out.printf("%-20s %-20s\n", "Project Name", "Status");
        System.out.printf("%-20s %-20s\n", application.getProjectName(), application.getStatus());
    }
    

    
    
    @Override
    public void submitEnquiry(Applicant applicant, String enquiryText, String projectName) {
        BTOProject project = db.btoProjectList.stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(projectName.trim()))
                .findFirst()
                .orElse(null);
    
        if (project == null) {
            System.out.println("Project not found: " + projectName);
            return;
        }
    
        if (!project.isVisible()) {
            System.out.println("Project '" + projectName + "' is currently not accepting enquiries.");
            return;
        }
    
        Enquiry enquiry = new Enquiry(applicant.getUserId(), enquiryText, projectName);
        enquiry.setEnquiryDate(new java.sql.Date(System.currentTimeMillis()));
        enquiry.setReply("");
        enquiry.setReplied(false);
    
        applicant.addEnquiry(enquiry);
        EnquiryServiceImpl enquiryService = new EnquiryServiceImpl(db);
        enquiryService.saveEnquiry(enquiry);
    
        System.out.println("\nEnquiry submitted successfully!");
        System.out.println("---------------------------------------------");
        System.out.println(enquiry);
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
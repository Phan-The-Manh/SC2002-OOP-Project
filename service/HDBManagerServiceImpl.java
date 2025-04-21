package service;

import data.Database;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import model.*;

public class HDBManagerServiceImpl implements HDBManagerService {
    private final Database db = new Database();
    private final EnquiryService enquiryService = new EnquiryServiceImpl(db);
    private final BTOProjectService btoProjectService = new BTOProjectServiceImpl(db);

    @Override
    public HDBManager checkLogin(String userId, String password) {
        boolean userFound = false;
        boolean passwordCorrect = false;

        if (userId != null) {
            userId = userId.trim();
        }
        if (password != null) {
            password = password.trim();
        }

        for (HDBManager manager : db.hdbManagerList) {
            if (manager.getUserId().equals(userId)) {
                userFound = true;
                if (manager.getPassword().equals(password)) {
                    passwordCorrect = true;
                    System.out.println("Login successful for Manager: " + manager.getName());
                    return manager;
                }
                break;
            }
        }

        if (!userFound) {
            System.out.println("No manager found with the given user ID.");
        } else if (!passwordCorrect) {
            System.out.println("Incorrect password. Please try again.");
        }
        return null;
    }

    @Override
    public void viewAvailableProjects() {
        Database db = new Database();
        List<BTOProject> projects = db.btoProjectList;
    
        if (projects.isEmpty()) {
            System.out.println("No available projects to display.");
            return;
        }
    
        // Print header with column names
        System.out.println("=== Available BTO Projects ===");
    
        // Loop through the list of projects and print details in a formatted way
        for (BTOProject project : projects) {
            System.out.println("Project Name: " + project.getProjectName());
            System.out.println("Neighborhood: " + project.getNeighborhood());
            System.out.println("2-Room Units: " + project.getFlatType1Units());
            System.out.println("3-Room Units: " + project.getFlatType2Units());
            System.out.println("Application Open Date: " + formatDate(project.getApplicationOpenDate()));
            System.out.println("Application Close Date: " + formatDate(project.getApplicationCloseDate()));
            System.out.println("Officer Slots: " + project.getAvailableSlots());
            System.out.println("Manager: " + project.getManager().getName());
            System.out.println("Officer List: " + project.getOfficerList().size());
            System.out.println("Visible: " + project.isVisible());
            System.out.println("-----------------------------------------------------");
        }
    }
    
    
    // Helper method to format the date in MM/dd/yyyy format
    private String formatDate(java.util.Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }
    


    @Override
    public void createProject(HDBManager manager, BTOProject project) {
        btoProjectService.saveProject(project);
        System.out.println("Project " + project.getProjectName() + " created successfully.");
        System.out.println("Project details: \n" + project.toString());
    }

    @Override
    public void editProject(String projectName, BTOProject updatedProject) {
        btoProjectService.editProject(projectName, updatedProject);
        System.out.println("Project " + projectName + " updated successfully.");
        System.out.println("Updated project details: \n" + updatedProject.toString());
    }

    @Override
    public void deleteProject(String projectName) {
        btoProjectService.deleteProject(projectName);
    }
    

    @Override
    public void toggleProjectVisibility(BTOProject project) {
        btoProjectService.toggleProjectVisibility(project);
        System.out.println("Project " + project.getProjectName() + " visibility set to " + (project.isVisible() ? "visible" : "hidden") + ".");
    }

    @Override
    public void manageHDBOfficerRegistration(ProjectRegistration projectRegistration, boolean approve) {
        BTOProject project = projectRegistration.getProject();
        HDBOfficer officer = projectRegistration.getOfficer();
    
        if (approve) {
            // Check if slots are available
            if (project.getAvailableSlots() <= 0) {
                System.out.println("No available slots for officers in project: " + project.getProjectName());
                return;
            }
    
            // Add officer to project
            officer.setProjectAssigned(project);
            officer.setIsRegisteredForProject(true);
            project.getOfficerList().add(officer);
            project.setAvailableSlots(project.getAvailableSlots() - 1);
            btoProjectService.saveProject(project);
    
            // Save the registration
            projectRegistration.setStatus("Approved");
            ProjectRegistrationServiceImpl projectRegistrationService = new ProjectRegistrationServiceImpl(db);
            projectRegistrationService.saveProjectRegistration(projectRegistration);
    
            System.out.println("Officer " + officer.getName() + " has been successfully registered for project: " + project.getProjectName());
        } else {
            System.out.println("Officer " + officer.getName() + " has been rejected from project: " + project.getProjectName());
        }
    }
    
    

    @Override
    public void manageApplicantApplication(BTOApplication application, boolean approve) {
        if (approve) {
            application.setStatus("Successful");
        } else {
            application.setStatus("Unsuccessful");
        }
        BTOApplicationService btoApplicationService = new BTOApplicationServiceImpl(db);
        btoApplicationService.saveBTOApplication(application);
        System.out.println("Application for " + application.getApplicant().getName() + " has been " + application.getStatus() + ".");
    }

    @Override
    public void manageWithdrawalRequest(BTOApplication application, boolean approve) {
        if (approve) {
            application.setStatus("Withdrawn");
    
            BTOApplicationService btoApplicationService = new BTOApplicationServiceImpl(db);
            btoApplicationService.saveBTOApplication(application);
    
            System.out.println("Withdrawal request for " + application.getApplicant().getName() + " has been approved.");
        } else {
            System.out.println("Withdrawal request for " + application.getApplicant().getName() + " has been rejected.");
        }
    }
    

    @Override
    public void generateReport(HDBManager manager, BTOProject project, String filterCriteria) {
        List<BTOApplication> filtered = db.btoApplicationList.stream()
                .filter(app -> app.getProjectName().equals(project.getProjectName()))
                .filter(app -> {
                    if (filterCriteria.equalsIgnoreCase("married")) {
                        return app.getApplicant().getMaritalStatus().equalsIgnoreCase("Married");
                    } else if (filterCriteria.equalsIgnoreCase("single")) {
                        return app.getApplicant().getMaritalStatus().equalsIgnoreCase("Single");
                    }
                    return true;
                })
                .collect(Collectors.toList());

        System.out.println("\n--- Report for Project: " + project.getProjectName() + " ---");
        for (BTOApplication app : filtered) {
            System.out.println(app.getApplicant().getName() + " | Status: " + app.getStatus());
        }
    }

    @Override
    public void viewEnquiries() {
        Database db = new Database();
        List<Enquiry> unrepliedEnquiries = db.enquiryList.stream()
            .filter(enquiry -> !enquiry.isReplied())
            .collect(Collectors.toList());
    
        if (unrepliedEnquiries.isEmpty()) {
            System.out.println("No unreplied enquiries at the moment.");
            return;
        }
    
        for (Enquiry enquiry : unrepliedEnquiries) {
            System.out.println("From: " + enquiry.getApplicantName());
            System.out.println("Text: " + enquiry.getEnquiryText());
            System.out.println("---");
        }
    }
    

    @Override
    public void replyToEnquiry(HDBManager manager, Enquiry enquiry, String replyText) {
        enquiry.setReply(replyText);
        enquiry.setReplied(true);
    }
}

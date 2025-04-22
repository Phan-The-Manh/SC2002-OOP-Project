package service;

import data.Database;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
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
        if (!project.isVisible()) {
            long visibleCount = db.btoProjectList.stream()
                    .filter(p -> p.getManager().getUserId().equals(project.getManager().getUserId()))
                    .filter(BTOProject::isVisible)
                    .count();
    
            if (visibleCount >= 1) {
                System.out.println("You already have a visible project. Please hide it before showing another.");
                return;
            }
        }
    
        btoProjectService.toggleProjectVisibility(project);
        System.out.println("Project " + project.getProjectName() + " visibility set to " +
                (project.isVisible() ? "visible" : "hidden") + ".");
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

            BTOProject project = null;
            for (BTOProject p: db.btoProjectList){
                if (p.getProjectName().trim().equalsIgnoreCase(application.getProjectName().trim())){
                    project = p;
                    break;
                }
            } 

            for (FlatBooking booking : db.flatBookingList){
                //Check if the application is booked
                if (booking.getApplicant().getUserId().trim().equalsIgnoreCase(application.getApplicant().getUserId().trim())){
                    
                    //Delete Flat Booking
                    FlatBookingServiceImpl flatBookingService = new FlatBookingServiceImpl(db);
                    flatBookingService.deleteFlatBooking(booking);

                    //Increase number of flat availability
                    BTOProjectServiceImpl btoProjectService = new BTOProjectServiceImpl(db);
                    switch(application.getRoomType()) {
                        case 1: 
                            int availableUnit1= project.getFlatType1Units() + 1;
                            project.setFlatType1Units(availableUnit1);
                            break;
                        case 2:
                            int availableUnit2= project.getFlatType2Units() + 1;
                            project.setFlatType2Units(availableUnit2);
                            break;
                    }
                    btoProjectService.saveProject(project);

                    break;
                }
            }
            
    
            System.out.println("Withdrawal request for " + application.getApplicant().getName() + " has been approved.");
        } else {
            System.out.println("Withdrawal request for " + application.getApplicant().getName() + " has been rejected.");
        }
    }
    
    @Override
    public void generateReport(HDBManager manager, String filterCriteria) {
        List<BTOProject> managedProjects = manager.getManagedProjects();

        if (managedProjects == null || managedProjects.isEmpty()) {
            System.out.println("No projects assigned to this manager.");
            return;
        }

        // Get all visible project names managed by the manager
        Set<String> managedProjectNames = managedProjects.stream()
                .filter(BTOProject::isVisible)
                .map(BTOProject::getProjectName)
                .collect(Collectors.toSet());

        if (managedProjectNames.isEmpty()) {
            System.out.println("No visible projects assigned to this manager.");
            return;
        }

        boolean hasMatch = false;

        System.out.println("\n--- Report: Flat Bookings in Managed Projects ---");

        for (FlatBooking booking : db.flatBookingList) {
            // Check if this booking is for a project the manager manages
            if (!managedProjectNames.contains(booking.getProject().getProjectName())) {
                continue;
            }

            // Try to find the applicant from the list
            Applicant matchedApplicant = null;
            for (Applicant applicant : db.applicantList) {
                if (applicant.getUserId().equalsIgnoreCase(booking.getApplicant().getUserId())) {
                    matchedApplicant = applicant;
                    break;
                }
            }

            if (matchedApplicant == null) continue;

            // Marital status filter
            String applicantStatus = matchedApplicant.getMaritalStatus().toLowerCase();
            String filter = filterCriteria.toLowerCase();

            if (!(filter.equals("all") || filter.isEmpty() || applicantStatus.equals(filter))) {
                continue;
            }

            // Passed all checks, show the report
            hasMatch = true;
            System.out.println("\nApplicant Name: " + matchedApplicant.getName());
            System.out.println("Age: " + matchedApplicant.getAge());
            System.out.println("Marital Status: " + matchedApplicant.getMaritalStatus());
            System.out.println("Flat Info:");
            System.out.println("  - Project Name: " + booking.getProject().getProjectName());
            System.out.println("  - Room Type: " + booking.getFlatType());
            System.out.println("---------------------------------------------");
        }

        if (!hasMatch) {
            System.out.println("No bookings matched the given filter criteria.");
        }
    }

    @Override
    public void viewEnquiries() {
        List<Enquiry> unrepliedEnquiries = db.enquiryList.stream()
            .collect(Collectors.toList());
    
        for (Enquiry enquiry : unrepliedEnquiries) {
            System.out.println("From: " + enquiry.getApplicantName());
            System.out.println("Text: " + enquiry.getEnquiryText());
            System.out.println("Reply: " + (enquiry.getReply() != null ? enquiry.getReply() : "N/A"));
            System.out.println("---");
        }
    }

    @Override
    public void replyToEnquiry(HDBManager manager, Enquiry enquiry, String replyText) {
        List<BTOProject> projects = db.btoProjectList;
        boolean hasPermission = false;

        for (BTOProject project : projects) {
            if (project.isVisible()
                    && project.getManager().getUserId().equals(manager.getUserId())
                    && enquiry.getProjectName().equalsIgnoreCase(project.getProjectName())) {
                hasPermission = true;
                break;
            }
        }

        if (!hasPermission) {
            System.out.println("You do not have permission to reply to this enquiry.");
            return;
        }

        enquiry.setReply(replyText);
        enquiry.setReplied(true);
        enquiryService.saveEnquiry(enquiry);
        System.out.println("Reply sent to " + enquiry.getApplicantName() + ": " + replyText);
    }

    @Override
    public void filterProject(HDBManager manager) {
        List<BTOProject> projects = db.btoProjectList;
        List<BTOProject> managedProjects = new ArrayList<>();

        for (BTOProject project : projects) {
            if (project.getManager().getUserId().equals(manager.getUserId())) {
                managedProjects.add(project);
            }
        }

        LocalDate today = LocalDate.now();
        List<BTOProject> overdueProjects = new ArrayList<>();
        List<BTOProject> inDueProjects = new ArrayList<>();
        for (BTOProject project : managedProjects) {
            try {
                // Convert java.sql.Date to LocalDate using Calendar
                java.sql.Date sqlCloseDate = (Date) project.getApplicationCloseDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sqlCloseDate);
                LocalDate closeDate = calendar.toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();

                if (closeDate.isBefore(today)) {
                    overdueProjects.add(project);
                } else if (closeDate.isAfter(today)) {
                    inDueProjects.add(project);
                } else {
                    // In case the close date is exactly today
                    System.out.println("Project " + project.getProjectName() + " closes today.");
                }
            } catch (Exception e) {
                System.out.println("Error processing date for project: " + project.getProjectName());
                e.printStackTrace();
            }
        }

        // Print In Due Projects
        System.out.println("\nProjects not over application period:");
        System.out.println("---------------------------------------------");
        for (BTOProject project : inDueProjects) {
            System.out.println(project);
        }

        // Print Overdue Projects
        System.out.println("\nProjects Overdue:");
        System.out.println("---------------------------------------------");
        for (BTOProject project : overdueProjects) {
            System.out.println(project);
        }
    }

}

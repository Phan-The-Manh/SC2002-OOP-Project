package service;
import data.Database;
import java.util.List;
import model.*;

public class HDBOfficerServiceImpl extends ApplicantServiceImpl implements HDBOfficerService {
    private final Database db;
    public HDBOfficerServiceImpl(Database db) {
        this.db = db;
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
        for (Applicant applicant : db.hdbOfficerList) {
            if (applicant.getUserId().equals(userId)) {
                userFound = true;  // User exists in the Applicants list
                if (applicant.getPassword().equals(password)) {
                    passwordCorrect = true;  // Correct password
                    System.out.println("Login successful for Officer: " + applicant.getName());
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
    public void applyForProject(Applicant officer, String projectName, int roomType) {
        if (!(officer instanceof HDBOfficer)) {
            System.out.println("Only HDB officers can apply using this method.");
            return;
        }

        HDBOfficer hdbOfficer = (HDBOfficer) officer;
        for (BTOProject project : db.btoProjectList) {
            List<HDBOfficer> officerList = project.getOfficerList();
            for (HDBOfficer o : officerList) {
                if (o.getName().equalsIgnoreCase(hdbOfficer.getName())) {
                    hdbOfficer.setProjectAssigned(project);
                    hdbOfficer.setRegisteredForProject(true);
                    break;
                }
            }
        }
        List<BTOProject> projectList = db.btoProjectList;

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
        if (!project.isVisible()) {
            System.out.println("The selected project is currently hidden and cannot be applied for.");
            return;
        }

        if (hdbOfficer.getProjectAssigned() != null &&
            hdbOfficer.getProjectAssigned().getProjectName().equalsIgnoreCase(projectName)) {
            System.out.println("HDB Officer has already been assigned to project: " + projectName);
            return;
        }

        if (officer.getMaritalStatus().equalsIgnoreCase("Single") &&
            officer.getAge() >= 35 &&
            roomType == 2) {
            System.out.println("Room type is not applicable for single applicants aged 35 and above.");
            return;
        }

        // Tạo application
        BTOApplication application = new BTOApplication(officer, project.getProjectName());
        application.setStatus("Pending");
        application.setRequestedWithdrawal(false);
        application.setRoomType(roomType);

        officer.setApplications(application);
        BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
        btoApplicationService.saveBTOApplication(application);

        System.out.println("\nApplication applied successfully!");
        System.out.println("---------------------------------------------");
        System.out.printf("%-20s %-20s %-20s\n", "Project Name", "Room Type", "Status");
        System.out.printf("%-20s %-20d %-20s\n", application.getProjectName(), application.getRoomType(), application.getStatus());
    }


    @Override
    public void registerForProject(HDBOfficer officer, BTOProject project) {
        // 1. Check if the officer is already registered for another project
        if (officer.isRegisteredForProject()) {
            System.out.println("Officer is already registered for another project.");
        }
        // 2. Check if the officer has already applied for this project
        else if (project.getApplications().stream().anyMatch(e -> e.getApplicant().getUserId().equals(officer.getUserId()))) {
            System.out.println("Officer has already applied for this project.");
        }
        else {
            ProjectRegistration registration = new ProjectRegistration(officer, project);
            db.projectRegistrationList.add(registration);
            officer.setRegisteredForProject(true);
            ProjectRegistrationServiceImpl registrationService = new ProjectRegistrationServiceImpl(db);
            registrationService.saveProjectRegistration(registration);
            System.out.println("Officer has been successfully registered for the project: " + project.getProjectName());
        }
    }


    @Override
    public void viewRegistrationStatus(HDBOfficer officer) {
        boolean isRegistered = false;

        // Loop through all registrations and check if the officer's NRIC matches
        for (ProjectRegistration registration : db.projectRegistrationList) {
            if (registration.getOfficer().getUserId().equals(officer.getUserId())) {
                System.out.println("Officer " + officer.getName() + " is registered for the project: " + registration.getProject().getProjectName());
                System.out.println("Registration Status: " + registration.getStatus());
                isRegistered = true; // Officer is registered for at least one project
            }
        }

        // If the officer is not registered for any project
        if (!isRegistered) {
            System.out.println("You is not registered for any project.");
        }
    }

    

    @Override 
    public void viewApplicationByStatus(HDBOfficer officer, String status){
        for (BTOProject project : db.btoProjectList) {
            List<HDBOfficer> officerList = project.getOfficerList();
            for (HDBOfficer o : officerList) {
                if (o.getName().equalsIgnoreCase(officer.getName())) {
                    officer.setProjectAssigned(project);
                    officer.setRegisteredForProject(true);
                    break;
                }
            }
        }

        if (officer.getProjectAssigned() == null) {
            System.out.println("Officer has not registered for any project.");
            return;
        }
    
        System.out.println("Project with status: " + status);
        for (BTOApplication application : db.btoApplicationList) {
            if(application.getStatus().equalsIgnoreCase(status) && application.getProjectName().equalsIgnoreCase(officer.getProjectAssigned().getProjectName())) {
                System.out.printf("%-20s %-20s %-20s %-20s\n", "Applicant ID", "Project Name", "Room Type", "Status");
                System.out.printf("%-20s %-20s %-20d %-20s\n",application.getApplicant().getUserId(), application.getProjectName(), application.getRoomType(), application.getStatus());
            }
        }
    }
    

    @Override
    public void approveFlatBooking(Applicant applicant, BTOApplication application) {
        if (application.getStatus().equals("Pending Booking")) {
            BTOProject project = null;
            for (BTOProject p : db.btoProjectList) {
                if (p.getProjectName().equalsIgnoreCase(application.getProjectName().trim())) {
                    project = p;
                    break;
                }
            }
            //Change application Status
            application.setStatus("Booked");
            BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
            btoApplicationService.saveBTOApplication(application);

            //Decrease number of flat availability
            BTOProjectServiceImpl btoProjectService = new BTOProjectServiceImpl(db);
            switch(application.getRoomType()) {
                case 1: 
                    int availableUnit1= project.getFlatType1Units() - 1;
                    project.setFlatType1Units(availableUnit1);
                    break;
                case 2:
                    int availableUnit2= project.getFlatType2Units() - 1;
                    project.setFlatType2Units(availableUnit2);
                    break;
            }
            btoProjectService.saveProject(project);

            //Saving new Flat Booking
            int flatType = application.getRoomType();
            FlatBooking booking = new FlatBooking(applicant, project, flatType);
            FlatBookingServiceImpl flatBookingService = new FlatBookingServiceImpl(db);
            flatBookingService.saveFlatBooking(booking);
            System.out.println("\nApprove Flat Booking Successfully!");
            System.out.println("---------------------------------------------");
            System.out.printf("%-20s %-20s %-20s\n", "Applicant Name", "Project Name", "Room Type");
            System.out.printf("%-20s %-20s %-20d\n", applicant.getName(), application.getProjectName(),application.getRoomType());
        } else {
             System.out.println("Cannot approve flat booking.");
        }
    }

    @Override
    public void viewProjectEnquiry(HDBOfficer officer) {
        // Assign project if not already set
        if (officer.getProjectAssigned() == null) {
            for (BTOProject project : db.btoProjectList) {
                for (HDBOfficer o : project.getOfficerList()) {
                    if (o.getName().equalsIgnoreCase(officer.getName())) {
                        officer.setProjectAssigned(project);
                        officer.setRegisteredForProject(true);
                        break;
                    }
                }
                if (officer.getProjectAssigned() != null) break;
            }
        }
    
        BTOProject project = officer.getProjectAssigned();
    
        if (project == null) {
            System.out.println("Officer has not registered for any project.");
            return;
        }
    
        List<Enquiry> enquiries = project.getEnquiries();
    
        if (enquiries == null || enquiries.isEmpty()) {
            System.out.println("\nNo enquiries found for project: " + project.getProjectName());
            return;
        }
    
        System.out.println("\nList of Enquiries for Project: " + project.getProjectName());
        System.out.println("------------------------------------------------------------");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println("Index: " + i + " | " + enquiries.get(i));
        }
    }
    


    @Override
    public void replyToEnquiry(HDBOfficer officer, int index, String replyText) {
        if (officer.getProjectAssigned() == null) {
            for (BTOProject project : db.btoProjectList) {
                for (HDBOfficer o : project.getOfficerList()) {
                    if (o.getName().equalsIgnoreCase(officer.getName())) {
                        officer.setProjectAssigned(project);
                        officer.setRegisteredForProject(true);
                        break;
                    }
                }
                if (officer.getProjectAssigned() != null) break;
            }
        }
        BTOProject project = officer.getProjectAssigned();
        if (project == null) {
            System.out.println("Officer has not registered for any project.");
            return;
        }
        if (index >= 0 && index < project.getEnquiries().size()) {
            Enquiry enquiry = project.getEnquiries().get(index);
            if (enquiry.isReplied()){
                System.out.println("This enquiry has been replied");
                return;
            }
            EnquiryServiceImpl enquiryService = new EnquiryServiceImpl(db);
            enquiry.setReply(replyText);
            enquiry.setReplied(true);
            enquiryService.saveEnquiry(enquiry);
            project.getEnquiries().set(index, enquiry);
            System.out.println("Enquiry replied: " + replyText);
        } else {
            System.out.println("Invalid enquiry index.");
        }
    }

    @Override
    public void generateReceipt(Applicant applicant, BTOApplication application) {
        if (application.getStatus().equals("Booked")) {
            BTOProject project = null;
            for (BTOProject p : db.btoProjectList) {
                if (p.getProjectName().equalsIgnoreCase(application.getProjectName().trim())) {
                    project = p;
                    break;
                }
            }
            int flatType = application.getRoomType();
            FlatBooking booking = new FlatBooking(applicant, project, flatType);
            FlatBookingServiceImpl flatBookingService = new FlatBookingServiceImpl(db);
            flatBookingService.generateReceipt(booking);
        } else {
            System.out.println("Application not booked. Cannot generate receipt.");
        }
    }
}

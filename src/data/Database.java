package data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import model.*;

public class Database {
    public List<Applicant> applicantList = new ArrayList<>();
    public List<HDBManager> hdbManagerList = new ArrayList<>();
    public List<HDBOfficer> hdbOfficerList = new ArrayList<>();
    public List<BTOProject> btoProjectList = new ArrayList<>();
    public List<BTOApplication> btoApplicationList = new ArrayList<>();
    public List<Enquiry> enquiryList = new ArrayList<>();
    public List<FlatBooking> flatBookingList = new ArrayList<>();
    public List<BTOProject> btoVisibleProjectList = new ArrayList<>();
    public List<ProjectRegistration> projectRegistrationList = new ArrayList<>();
    public Database() {
        loadApplicants("src/data/ApplicantList.csv");
        loadManagers("src/data/ManagerList.csv");
        loadOfficers("src/data/OfficerList.csv");
        loadProjects("src/data/ProjectList.csv");
        loadBTOApplicationList("src/data/BTOApplicationList.csv");
        loadEnquiries("src/data/EnquiryList.csv");
        loadFlatBookings("src/data/FlatBookingList.csv");
        loadProjectRegistrations("src/data/ProjectRegistrationList.csv");
    }

    private void loadApplicants(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Allow empty fields
                if (parts.length >= 5) {
                    String name = parts[0].trim();
                    String nric = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String maritalStatus = parts[3].trim();
                    String password = parts[4].trim();
    
                    // Create applicant and add to the list
                    Applicant applicant = new Applicant(name, nric, age, maritalStatus, password);
                    applicantList.add(applicant);
                } else {
                    System.err.println("Skipping line due to invalid data: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load applicants: " + e.getMessage());
        }
    }

    
    private void loadOfficers(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
    
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
    
                if (parts.length >= 5) {
                    String name = parts[0].trim();
                    String nric = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String maritalStatus = parts[3].trim();
                    String password = parts[4].trim();
    
                    HDBOfficer officer = new HDBOfficer(name, nric, age, maritalStatus, password);
                    hdbOfficerList.add(officer);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load officers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private void loadManagers(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    hdbManagerList.add(new HDBManager(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim()), parts[3].trim(), parts[4].trim()));
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load managers: " + e.getMessage());
        }
    }

    private void loadProjects(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
            while ((line = reader.readLine()) != null) {
                // Split the line based on comma, ensuring we account for quotes in values (like officer names with commas).
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    
                // Make sure there are enough fields to process
                if (parts.length >= 14) {
                    try {
                        // Extract the basic information from the CSV columns
                        String projectName = parts[0].trim();
                        String neighborhood = parts[1].trim();
                        int units1 = Integer.parseInt(parts[3].trim());
                        double price1 = Double.parseDouble(parts[4].trim());
                        int units2 = Integer.parseInt(parts[6].trim());
                        double price2 = Double.parseDouble(parts[7].trim());
    
                        // Parse application dates
                        LocalDate openDate = LocalDate.parse(parts[8].trim(), formatter);
                        LocalDate closeDate = LocalDate.parse(parts[9].trim(), formatter);
    
                        // Manager and officer slot
                        String managerName = parts[10].trim();
                        int officerSlot = Integer.parseInt(parts[11].trim());
    
                        // Parse the officers, which are now separated by a semicolon
                        List<HDBOfficer> assignedOfficers = new ArrayList<>();
                        String officerField = parts[12].trim().replace("\"", ""); // Remove extra quotes if they exist
                        if (!officerField.isEmpty()) {
                            // Split by semicolon for multiple officers
                            String[] officerNames = officerField.split(";");
                            for (String officerName : officerNames) {
                                String trimmedName = officerName.trim();
                                HDBOfficer officer = hdbOfficerList.stream()
                                        .filter(o -> o.getName().trim().equalsIgnoreCase(trimmedName))
                                        .findFirst()
                                        .orElse(null);
    
                                if (officer != null) {
                                    assignedOfficers.add(officer);
                                }
                            }
                            
                        }
    
                        // Parse visibility (this field is now after the officer list)
                        boolean visible = Boolean.parseBoolean(parts[13].trim());
    
                        // Find the manager from the list
                        HDBManager manager = hdbManagerList.stream()
                                .filter(m -> m.getName().equalsIgnoreCase(managerName))
                                .findFirst()
                                .orElse(null);
    
                        // If manager exists, create and add the project
                        if (manager != null) {
                            BTOProject project = new BTOProject(
                                    projectName,
                                    neighborhood,
                                    units1,
                                    price1,
                                    units2,
                                    price2,
                                    java.sql.Date.valueOf(openDate),
                                    java.sql.Date.valueOf(closeDate),
                                    manager,
                                    officerSlot,
                                    assignedOfficers
                            );
                            project.setVisibility(visible); // Set the visibility after the officer list
                            project.setManager(manager);
                            manager.addProject(project); // Add project to manager's list
                            project.setOfficerList(assignedOfficers);
                            btoProjectList.add(project);
                            if (project.isVisible() == true){
                                btoVisibleProjectList.add(project);
                            }
                        } else {
                            System.err.println("Manager not found for project: " + projectName);
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load projects: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void loadBTOApplicationList(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // handle empty fields
                if (parts.length >= 6) {
                    String applicantId = parts[0].trim();
                    String projectName = parts[1].trim();
                    String status = parts[2].trim();
                    String roomType = parts[3].trim();
                    String applicationDate = parts[4].trim();
                    String withdrawalRequested = parts[5].trim();
    
                    // Find the applicant
                    Applicant applicant = applicantList.stream()
                            .filter(a -> a.getUserId().equals(applicantId))
                            .findFirst()
                            .orElse(null);
    
                    if (applicant != null) {
                        BTOApplication app = new BTOApplication(applicant, projectName);
                        app.setStatus(status);
                        java.sql.Date date = java.sql.Date.valueOf(applicationDate);
                        app.setApplicationDate(date);
                        app.setRequestedWithdrawal(Boolean.parseBoolean(withdrawalRequested)); // <- NEW
                        app.setRoomType(Integer.parseInt(roomType));
                        btoApplicationList.add(app);
                        applicant.setApplications(app);
                    }

                    HDBOfficer officer = hdbOfficerList.stream()
                            .filter(a -> a.getUserId().equals(applicantId))
                            .findFirst()
                            .orElse(null);
                    if (officer != null) {
                        BTOApplication app = new BTOApplication(officer, projectName);
                        app.setStatus(status);
                        java.sql.Date date = java.sql.Date.valueOf(applicationDate);
                        app.setApplicationDate(date);
                        app.setRequestedWithdrawal(Boolean.parseBoolean(withdrawalRequested)); // <- NEW
                        app.setRoomType(Integer.parseInt(roomType));
                        btoApplicationList.add(app);
                        officer.setApplications(app);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load BTO applications: " + e.getMessage());
        }
    }

    

    public void getBTOProjectList() {
        System.out.println("\n===== BTO Projects =====");
        if (btoProjectList.isEmpty()) {
            System.out.println("No available projects.");
        } else {
            for (BTOProject project : btoVisibleProjectList) {
                System.out.println("-----------------------------------");
                System.out.printf("Project: %-30s | Neighborhood: %-20s\n", project.getProjectName(), project.getNeighborhood());
                System.out.printf("2-Room: %-3d units at $%-8.2f | 3-Room: %-3d units at $%-8.2f\n",
                        project.getFlatType1Units(), project.getFlatType1Price(), project.getFlatType2Units(), project.getFlatType2Price());
                System.out.printf("Application Period: %-10s to %-10s\n", project.getApplicationOpenDate(), project.getApplicationCloseDate());

                System.out.print("Manager: ");
                System.out.println(project.getManager().getName());

                System.out.print("Officers: ");
                for (HDBOfficer officer : project.getOfficerList()) {
                    System.out.print(officer.getName() + " ");
                }

                System.out.print("Visibility: ");
                System.out.println(project.isVisible());

                System.out.println("\n-----------------------------------");
            }
        }
    }

    public void getBTOProjectListByRoomType(int roomType) {
        boolean flag = false;
        switch (roomType){
        case 1:
            System.out.println("\n===== 2-Room BTO Projects =====");
            for (BTOProject project : btoVisibleProjectList) {
                if (project.getFlatType1Units() > 0){
                    flag = true;
                    System.out.println("-----------------------------------");
                    System.out.printf("Project: %-30s | Neighborhood: %-20s\n", project.getProjectName(), project.getNeighborhood());
                    System.out.printf("2-Room: %-3d units at $%-8.2f\n",project.getFlatType1Units(), project.getFlatType1Price());
                    System.out.printf("Application Period: %-10s to %-10s\n", project.getApplicationOpenDate(), project.getApplicationCloseDate());
                    System.out.print("Manager: ");
                    System.out.println(project.getManager().getName());
                    System.out.print("Officers: ");
                    for (HDBOfficer officer : project.getOfficerList()) {
                        System.out.print(officer.getName() + " ");
                    }
                System.out.println("\n-----------------------------------");
                }
            }
            if (!flag) 
                System.out.println("No available projects.");
            break;
        case 2:
            System.out.println("\n===== 3-Room BTO Projects =====");
            for (BTOProject project : btoVisibleProjectList) {
                if (project.getFlatType2Units() > 0){
                    flag = true;
                    System.out.println("-----------------------------------");
                    System.out.printf("Project: %-30s | Neighborhood: %-20s\n", project.getProjectName(), project.getNeighborhood());
                    System.out.printf("3-Room: %-3d units at $%-8.2f\n",project.getFlatType2Units(), project.getFlatType2Price());
                    System.out.printf("Application Period: %-10s to %-10s\n", project.getApplicationOpenDate(), project.getApplicationCloseDate());
                    System.out.print("Manager: ");
                    System.out.println(project.getManager().getName());
                    System.out.print("Officers: ");
                    for (HDBOfficer officer : project.getOfficerList()) {
                        System.out.print(officer.getName() + " ");
                    }
                System.out.println("\n-----------------------------------");
                }
            }
            if (!flag) 
                System.out.println("No available projects.");
            break;
        default:
            System.out.println("Invalid Room Type");
            break;
        }
    }

    public void get2RoomBTOProjectList() {
        boolean flag = false;
        System.out.println("\n===== 2-Room BTO Projects =====");
        for (BTOProject project : btoProjectList) {
            if (project.getFlatType1Units() > 0){
                flag = true;
                System.out.println("-----------------------------------");
                System.out.printf("Project: %-30s | Neighborhood: %-20s\n", project.getProjectName(), project.getNeighborhood());
                System.out.printf("2-Room: %-3d units at $%-8.2f\n",project.getFlatType1Units(), project.getFlatType1Price());
                System.out.printf("Application Period: %-10s to %-10s\n", project.getApplicationOpenDate(), project.getApplicationCloseDate());
                System.out.print("Manager: ");
                System.out.println(project.getManager().getName());
                System.out.print("Officers: ");
                for (HDBOfficer officer : project.getOfficerList()) {
                    System.out.print(officer.getName() + " ");
                }
            System.out.println("\n-----------------------------------");
            }
        }
        if (!flag) 
            System.out.println("No available projects.");
    }

    public void getVisibleBTOProjectList(){
        System.out.println("\n===== BTO Projects =====");
        if (btoVisibleProjectList.isEmpty()) {
            System.out.println("No available projects.");
        } else {
            for (BTOProject project : btoVisibleProjectList) {
                System.out.println("-----------------------------------");
                System.out.printf("Project: %-30s | Neighborhood: %-20s\n", project.getProjectName(), project.getNeighborhood());
                System.out.printf("2-Room: %-3d units at $%-8.2f | 3-Room: %-3d units at $%-8.2f\n",
                        project.getFlatType1Units(), project.getFlatType1Price(), project.getFlatType2Units(), project.getFlatType2Price());
                System.out.printf("Application Period: %-10s to %-10s\n", project.getApplicationOpenDate(), project.getApplicationCloseDate());

                System.out.print("Manager: ");
                System.out.println(project.getManager().getName());

                System.out.print("Officers: ");
                for (HDBOfficer officer : project.getOfficerList()) {
                    System.out.print(officer.getName() + " ");
                }
                System.out.println("\n-----------------------------------");
            }
        }
    }


    private void loadEnquiries(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // allow empty fields
                if (parts.length >= 6) {
                    String applicantId = parts[0].trim();
                    String enquiryText = parts[1].trim();
                    String reply = parts[2].trim();
                    String dateString = parts[3].trim();
                    String isRepliedStr = parts[4].trim();
                    String projectName = parts[5].trim();
                    
                    Enquiry enquiry = new Enquiry(applicantId, enquiryText, projectName);
                    enquiry.setApplicantName(applicantId);
                    enquiry.setEnquiryText(enquiryText);
                    enquiry.setReply(reply);
                    enquiry.setEnquiryDate(java.sql.Date.valueOf(dateString));
                    enquiry.setReplied(Boolean.parseBoolean(isRepliedStr));
                    enquiryList.add(enquiry);

                    Applicant applicant = applicantList.stream()
                            .filter(a -> a.getUserId().equals(applicantId))
                            .findFirst()
                            .orElse(null);
    
                    if (applicant != null) {
                        applicant.getEnquiries().add(enquiry); // if you track per applicant
                    }

                    Applicant officer = hdbOfficerList.stream()
                            .filter(a -> a.getUserId().equals(applicantId))
                            .findFirst()
                            .orElse(null);

                    if (officer != null) {
                        officer.getEnquiries().add(enquiry); // if you track per applicant
                    }

                    BTOProject project = btoProjectList.stream()
                            .filter(a -> a.getProjectName().trim().equalsIgnoreCase(projectName))
                            .findAny()
                            .orElse(null);
    
                    if (project != null) {
                        project.getEnquiries().add(enquiry); // if you track per applicant
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load enquiries: " + e.getMessage());
        }
    }
    
    private void loadFlatBookings(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String applicantId = parts[0].trim();
                    String projectName = parts[1].trim();
                    int flatType = Integer.parseInt(parts[2].trim());
               
                    Applicant applicant = applicantList.stream()
                    .filter(a -> a.getUserId().equals(applicantId))
                    .findFirst()
                    .orElse(null);

                    BTOProject project = btoProjectList.stream()
                            .filter(a -> a.getProjectName().equals(projectName))
                            .findFirst()
                            .orElse(null);
                    
                    FlatBooking booking = new FlatBooking(applicant, project, flatType);
                    flatBookingList.add(booking);
                }    
            }
        } catch (Exception e) {
            System.err.println("Failed to load managers: " + e.getMessage());
        }
    }

    private void loadProjectRegistrations(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // handle empty fields
                if (parts.length >= 3) {
                    String officerId = parts[0].trim();
                    String projectName = parts[1].trim();
                    String status = parts[2].trim();
    
                    // Find the corresponding HDBOfficer and BTOProject
                    HDBOfficer officer = hdbOfficerList.stream()
                            .filter(o -> o.getUserId().equals(officerId))
                            .findFirst()
                            .orElse(null);
    
                    BTOProject project = btoProjectList.stream()
                            .filter(p -> p.getProjectName().equals(projectName))
                            .findFirst()
                            .orElse(null);
    
                    // If both officer and project are found, create the ProjectRegistration
                    if (officer != null && project != null) {
                        ProjectRegistration pr = new ProjectRegistration(officer, project);
                        pr.setStatus(status);
                        projectRegistrationList.add(pr);

                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load project registrations: " + e.getMessage());
        }
    }
    
    

    
}

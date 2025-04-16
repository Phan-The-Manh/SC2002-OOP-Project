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

    public Database() {
        loadApplicants("src/data/ApplicantList.csv");
        loadManagers("src/data/ManagerList.csv");
        loadOfficers("src/data/OfficerList.csv");
        loadProjects("src/data/ProjectList.csv");
        loadBTOApplicationList("src/data/BTOApplicationList.csv");
        loadEnquiries("src/data/EnquiryList.csv");
    }

    private void loadApplicants(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Split by comma, allow empty fields
                if (parts.length >= 5) {
                    String name = parts[0].trim();
                    String nric = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String maritalStatus = parts[3].trim();
                    String password = parts[4].trim();
                    String btoApplication = parts.length > 5 ? parts[5].trim() : "";
                    String enquiry = parts.length > 6 ? parts[6].trim() : "";
    
                    // Create the applicant and add to the list
                    Applicant applicant = new Applicant(name, nric, age, maritalStatus, password);
                    applicantList.add(applicant);
    
                    // Link BTO Applications from btoApplicationList
                    if (btoApplication != null && !btoApplication.isEmpty()) {
                        String[] projectNames = btoApplication.split(";");
                        for (String projectName : projectNames) {
                            projectName = projectName.trim();
                            for (BTOApplication app : btoApplicationList) {
                                if (app.getApplicant().getUserId().equals(applicant.getUserId())
                                        && app.getProjectName().equalsIgnoreCase(projectName)) {
                                    applicant.getApplications().add(app);
                                }
                            }
                        }
                    }
    
                    // Link Enquiries from enquiryList
                    if (enquiry != null && !enquiry.isEmpty()) {
                        String[] enquiryTexts = enquiry.split(";");
                        for (String enquiryText : enquiryTexts) {
                            enquiryText = enquiryText.trim();
                            for (Enquiry e : enquiryList) {
                                if (e.getApplicantName().equals(applicant.getUserId())
                                        && e.getEnquiryText().equalsIgnoreCase(enquiryText)) {
                                    applicant.getEnquiries().add(e);
                                }
                            }
                        }
                    }
                } else {
                    System.err.println("Skipping line due to invalid data: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load applicants: " + e.getMessage());
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

    private void loadOfficers(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    hdbOfficerList.add(new HDBOfficer(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim()), parts[3].trim(), parts[4].trim()));
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load officers: " + e.getMessage());
        }
    }

    private void loadProjects(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 13) {
                    String projectName = parts[0].trim();
                    String neighborhood = parts[1].trim();
                    int units1 = Integer.parseInt(parts[3].trim());
                    double price1 = Double.parseDouble(parts[4].trim());
                    int units2 = Integer.parseInt(parts[6].trim());
                    double price2 = Double.parseDouble(parts[7].trim());
                    LocalDate openDate = LocalDate.parse(parts[8].trim(), formatter);
                    LocalDate closeDate = LocalDate.parse(parts[9].trim(), formatter);
                    String managerName = parts[10].trim();
                    int officerSlot = Integer.parseInt(parts[11].trim());

                    String[] officerNames = parts[12].replace("\"", "").split(",");
                    List<HDBOfficer> assignedOfficers = new ArrayList<>();
                    for (String name : officerNames) {
                        hdbOfficerList.stream()
                                .filter(o -> o.getName().equalsIgnoreCase(name.trim()))
                                .findFirst()
                                .ifPresent(assignedOfficers::add);
                    }

                    HDBManager manager = hdbManagerList.stream()
                            .filter(m -> m.getName().equalsIgnoreCase(managerName))
                            .findFirst()
                            .orElse(null);

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
                        btoProjectList.add(project);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load projects: " + e.getMessage());
        }
    }

    public void getBTOProjectList() {
        System.out.println("\n===== BTO Projects =====");
        if (btoProjectList.isEmpty()) {
            System.out.println("No available projects.");
        } else {
            for (BTOProject project : btoProjectList) {
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


    private void loadBTOApplicationList(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // handle empty fields
                if (parts.length >= 5) {
                    String applicantId = parts[0].trim();
                    String projectName = parts[1].trim();
                    String status = parts[2].trim();
                    String applicationDate = parts[3].trim();
                    String withdrawalRequested = parts[4].trim();
    
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
    
                        btoApplicationList.add(app);
                        applicant.getApplications().add(app);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load BTO applications: " + e.getMessage());
        }
    }

    private void loadEnquiries(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // allow empty fields
                if (parts.length >= 5) {
                    String applicantId = parts[0].trim();
                    String enquiryText = parts[1].trim();
                    String reply = parts[2].trim();
                    String dateString = parts[3].trim();
                    String isRepliedStr = parts[4].trim();
    
                    Applicant applicant = applicantList.stream()
                            .filter(a -> a.getUserId().equals(applicantId))
                            .findFirst()
                            .orElse(null);
    
                    if (applicant != null) {
                        Enquiry enquiry = new Enquiry(applicantId, enquiryText);
                        enquiry.setApplicantName(applicantId);
                        enquiry.setEnquiryText(enquiryText);
                        enquiry.setReply(reply);
                        enquiry.setEnquiryDate(java.sql.Date.valueOf(dateString));
                        enquiry.setReplied(Boolean.parseBoolean(isRepliedStr));
    
                        enquiryList.add(enquiry);
                        applicant.getEnquiries().add(enquiry); // if you track per applicant
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load enquiries: " + e.getMessage());
        }
    }
    
    
    
    
}

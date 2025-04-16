package service;

import data.Database;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class ApplicantServiceImpl implements ApplicantService {

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
            BTOApplication application = new BTOApplication(applicant, project.getProjectName());
            applicant.getApplications().add(application);
            System.out.println("Application submitted for project: " + projectName);
    
            BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
            btoApplicationService.saveBTOApplication(application);
            System.out.println(application);
    
            // ✅ Update the applicant CSV safely
            try {
                File file = new File("src/data/ApplicantList.csv");
                List<String> updatedLines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String header = reader.readLine(); // Read header
                updatedLines.add(header); // Keep header
    
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
    
                    if (parts[1].trim().equals(applicant.getUserId())) { // Match by NRIC
    
                        // Ensure the array has at least 7 columns (up to "Enquiry")
                        if (parts.length < 7) {
                            String[] newParts = new String[7];
                            System.arraycopy(parts, 0, newParts, 0, parts.length);
                            for (int i = parts.length; i < 7; i++) {
                                newParts[i] = "";
                            }
                            parts = newParts;
                        }
    
                        // Update BTO Application column (index 5)
                        String existing = parts[5].trim();
                        if (!existing.contains(project.getProjectName())) {
                            if (!existing.isEmpty()) {
                                existing += ";" + project.getProjectName();
                            } else {
                                existing = project.getProjectName();
                            }
                        }
                        parts[5] = existing;
                        updatedLines.add(String.join(",", parts));
                    } else {
                        updatedLines.add(line);
                    }
                }
                reader.close();
    
                // Write updated content back to file
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine + "\n");
                }
                writer.close();
    
                System.out.println("CSV updated with new BTO Application.");
            } catch (IOException e) {
                System.err.println("Failed to update CSV: " + e.getMessage());
            }
    
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
        Database db = new Database();
        // Find the application by project name
        BTOApplication application = applicant.getApplications().stream()
            .filter(app -> app.getProjectName().equalsIgnoreCase(projectName))
            .findFirst()
            .orElse(null);
    
        if (application != null) {
            application.setStatus("Pending Withdrawal");  // Update status to pending withdrawal
            application.setRequestedWithdrawal(true);
            BTOApplicationServiceImpl btoApplicationService = new BTOApplicationServiceImpl(db);
            btoApplicationService.saveBTOApplication(application);
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
        Database db = new Database();
        Enquiry enquiry = new Enquiry(applicant.getUserId(), enquiryText);
        enquiry.setEnquiryDate(new java.sql.Date(System.currentTimeMillis()));
        enquiry.setReply(""); // No reply yet
        enquiry.setReplied(false);
    
        applicant.addEnquiry(enquiry);
        db.enquiryList.add(enquiry);  // Add to global list if applicable
    
        // Save to EnquiryList.csv
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/EnquiryList.csv", true))) {
            String dateString = enquiry.getEnquiryDate().toString(); // yyyy-MM-dd
            writer.write(enquiry.getApplicantName() + "," +
                         enquiry.getEnquiryText() + "," +
                         enquiry.getReply() + "," +
                         dateString + "," +
                         enquiry.isReplied() + "\n");
        } catch (IOException e) {
            System.err.println("Failed to save enquiry: " + e.getMessage());
        }
    
        // ✅ Update the applicant's enquiry field in ApplicantList.csv
        try {
            File file = new File("src/data/ApplicantList.csv");
            List<String> updatedLines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String header = reader.readLine(); // Read header
            updatedLines.add(header); // Keep header
    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts[1].trim().equals(applicant.getUserId())) { // Match NRIC
    
                    // Ensure parts has at least 7 fields
                    if (parts.length < 7) {
                        String[] newParts = new String[7];
                        System.arraycopy(parts, 0, newParts, 0, parts.length);
                        for (int i = parts.length; i < 7; i++) {
                            newParts[i] = "";
                        }
                        parts = newParts;
                    }
    
                    // Update Enquiry field (index 6)
                    String existing = parts[6].trim();
                    if (!existing.contains(enquiryText)) {
                        if (!existing.isEmpty()) {
                            existing += ";" + enquiryText;
                        } else {
                            existing = enquiryText;
                        }
                    }
                    parts[6] = existing;
                    updatedLines.add(String.join(",", parts));
                } else {
                    updatedLines.add(line);
                }
            }
            reader.close();
    
            // Write updated content back to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
            writer.close();
    
            System.out.println("\nEnquiry saved and ApplicantList.csv updated!");
        } catch (IOException e) {
            System.err.println("Failed to update ApplicantList.csv: " + e.getMessage());
        }
    
        System.out.println("\nEnquiry submitted successfully!");
        System.out.println("---------------------------------------------");
        System.out.println(enquiry.toString());
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


}
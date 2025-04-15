package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BTOApplication {
    private Date applicationDate = new Date();
    private String status = "Pending";
    private Applicant applicant;
    private String projectName;

    // Constructor
    public BTOApplication(Applicant applicant, String projectName) {
        this.applicant = applicant;
        this.projectName = projectName;
        this.applicationDate = new Date(System.currentTimeMillis()); 
    }

    // Getter for applicationDate
    public Date getApplicationDate() {
        return applicationDate;
    }

    // Setter for applicationDate
    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter for applicant
    public Applicant getApplicant() {
        return applicant;
    }

    // Setter for applicant
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    // Getter for project
    public String getProjectName() {
        return projectName;
    }

    // Setter for project
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    // Method to request withdrawal (status change)
    public void requestWithdrawal() {
        status = "Withdrawn";
    }

    // Overridden toString method
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(applicationDate);
        return "===== BTO Application =====\n" +
               "Applicant Name     : " + applicant.getName() + "\n" +
               "Project Name       : " + projectName + "\n" +
               "Application Date   : " + formattedDate + "\n" +
               "Application Status : " + status + "\n";
    }
    
}

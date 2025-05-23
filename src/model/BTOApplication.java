package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BTOApplication {
    private Date applicationDate = new Date();
    private String status = "Pending";
    private int roomType;
    private Applicant applicant;
    private String projectName;
    private boolean isRequestedWithdrawal = false;

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

    public int getRoomType() {
        return roomType;
    }

    // Setter for applicationDate
    public void setRoomType(int roomType) {
        this.roomType = roomType;
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

    // Getter for isRequestedWithdrawal
    public boolean isRequestedWithdrawal() {
        return isRequestedWithdrawal;
    }

    // Setter for isRequestedWithdrawal
    public void setRequestedWithdrawal(boolean isRequestedWithdrawal) {
        this.isRequestedWithdrawal = isRequestedWithdrawal;
    }

    // Overridden toString method
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(applicationDate);
        return "===== BTO Application =====\n" +
               "Applicant Name     : " + applicant.getName() + "\n" +
               "NRIC               : " + applicant.getNRIC() + "\n" +
               "Project Name       : " + projectName + "\n" +
               "Room Type          : " + roomType + "\n" +
               "(1. 2-room | 2. 3-room)" + "\n" +
               "Application Date   : " + formattedDate + "\n" +
               "Application Status : " + status + "\n";
    }
    
}

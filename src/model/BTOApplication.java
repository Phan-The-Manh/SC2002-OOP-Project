package model;

import java.util.Date;

public class BTOApplication {
    private Date applicationDate = new Date();
    private String status = "Pending";
    private Applicant applicant;
    private BTOProject project;

    public BTOApplication(Applicant applicant, BTOProject project) {
        this.applicant = applicant;
        this.project = project;
    }
    
    public String getStatus() {
        return status;
    }

    public void requestWithdrawal() {
        status = "Withdrawn";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Application by: " + applicant.getName() + " | Status: " + status;
    }
}
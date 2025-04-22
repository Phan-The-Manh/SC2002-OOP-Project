package model;

import java.util.Date;

public class Enquiry {
    private String applicantName;
    private String enquiryText;
    private String reply = null;
    private Date enquiryDate;
    private String projectName;
    private boolean isReplied = false;

    // Constructor
    public Enquiry(String applicantName, String enquiryText, String projectName) {
        this.applicantName = applicantName;
        this.enquiryText = enquiryText;
        this.projectName = projectName;
        this.enquiryDate = new Date();
    }

   // Getter for applicantName
    public String getApplicantName() {
        return applicantName;
    }

    // Setter for applicantName
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    // Getter for enquiryText
    public String getEnquiryText() {
        return enquiryText;
    }

    // Setter for enquiryText
    public void setEnquiryText(String enquiryText) {
        this.enquiryText = enquiryText;
    }

    // Getter for reply
    public String getReply() {
        return reply;
    }

    // Setter for reply
    public void setReply(String reply) {
        this.reply = reply;
    }

    // Getter for enquiryDate
    public Date getEnquiryDate() {
        return enquiryDate;
    }

    // Setter for enquiryDate
    public void setEnquiryDate(Date enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    // Getter for isReplied 
    public boolean isReplied() {
          return isReplied;
     }
    
    // Setter for isReplied
    public void setReplied(boolean isReplied) {
        this.isReplied = isReplied;
    }

    public String getProjectName(){
        return projectName;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    // Overridden toString method
    @Override
    public String toString() {
        return "Enquiry: " + enquiryText + " | Project Name: " + projectName + " | Reply: " + (reply != null ? reply : "Pending");
    }
}

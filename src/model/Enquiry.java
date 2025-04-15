package model;

import java.util.Date;

public class Enquiry {
    private String enquiryText;
    private String reply;
    private Date enquiryDate;

    // Constructor
    public Enquiry(String enquiryText) {
        this.enquiryText = enquiryText;
        this.enquiryDate = new Date();
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

    // Overridden toString method
    @Override
    public String toString() {
        return "Enquiry: " + enquiryText + " | Reply: " + (reply != null ? reply : "Pending");
    }
}

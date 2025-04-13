package model;

import java.util.Date;
public class Enquiry {
    private String enquiryText;
    private String reply;
    private Date enquiryDate;

    public Enquiry(String enquiryText) {
        this.enquiryText = enquiryText;
        this.enquiryDate = new Date();
    }

    public void setEnquiryText(String enquiryText) {
        this.enquiryText = enquiryText;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String toString() {
        return "Enquiry: " + enquiryText + " | Reply: " + (reply != null ? reply : "Pending");
    }
}
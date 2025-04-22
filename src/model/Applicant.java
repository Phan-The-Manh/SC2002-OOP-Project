package model;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
    private List<Enquiry> enquiries = new ArrayList<>(); 
    private BTOApplication application = null;

    public Applicant(String name, String userId, int age, String maritalStatus, String password) {
        super(name, userId, age, maritalStatus, password);
    }

    // === Inherited Fields (from User class) ===
    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getNRIC() {
        return super.getUserId();
    }

    public void setNRIC(String nric) {
        super.setUserId(nric);
    }

    public int getAge() {
        return super.getAge();
    }

    public void setAge(int age) {
        super.setAge(age);
    }

    public String getMaritalStatus() {
        return super.getMaritalStatus();
    }

    public void setMaritalStatus(String maritalStatus) {
        super.setMaritalStatus(maritalStatus);
    }

    public String getPassword() {
        return super.getPassword();
    }

    public void setPassword(String password) {
        super.setPassword(password);
    }

    // === Applications ===
    public BTOApplication getApplications() {
        return application;
    }

    public void setApplications(BTOApplication application) {
        this.application = application;
    }

    // === Enquiries ===
    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void setEnquiries(List<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }

    public void addEnquiry(Enquiry enquiry) {
        this.enquiries.add(enquiry);
    }
}

package model;
import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
    private List<Enquiry> enquiries = new ArrayList<>();
    private List<BTOApplication> applications = new ArrayList<>();

    public Applicant(String name, String userId, String password, int age, String maritalStatus) {
        super(name, userId, password, age, maritalStatus);
    }

    public List<BTOApplication> getApplications() {
        return applications;
    }

    public void addApplication(BTOApplication application) {
        applications.add(application);
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

}
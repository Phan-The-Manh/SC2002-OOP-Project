package service;

import model.*;

public class HDBOfficerServiceImpl implements HDBOfficerService {
    
    @Override
    public Applicant checkLogin(String userId, String password) {
        return null; // Placeholder for actual login check logic
    }

    @Override
    public void applyForProject(Applicant applicant, String projectName) {

    }

    @Override
    public void requestWithdrawal(Applicant applicant, BTOApplication application) {
        application.requestWithdrawal();
        System.out.println("Application withdrawn: " + application.toString());
    }

    @Override
    public void submitEnquiry(Applicant applicant, String enquiryText) {
        Enquiry enquiry = new Enquiry(enquiryText);
        applicant.addEnquiry(enquiry);
        System.out.println("Enquiry submitted: " + enquiryText);
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

    @Override
    public void viewApplicationStatus(Applicant applicant) {
        for (BTOApplication application : applicant.getApplications()) {
            System.out.println(application.toString());
        }
    }

    @Override
    public void registerForProject(HDBOfficer officer, BTOProject project) {
        if (officer.isRegisteredForProject()) {
            System.out.println("Officer is already registered for another project.");
        } else {
            officer.setProjectAssigned(project);
            System.out.println("Officer " + officer.getName() + " successfully registered for project " + project.getProjectName());
        }
    }

    // @Override
    // public void approveFlatBooking(Applicant applicant, BTOApplication application, String flatType) {
    //     if (application.getStatus().equals("Successful")) {
    //         ((HDBOfficer) applicant).approveFlatBooking(applicant, application, flatType);
    //     } else {
    //         System.out.println("Application not successful. Cannot approve flat booking.");
    //     }
    // }

    @Override
    public void replyToEnquiry(Enquiry enquiry, String replyText) {
        enquiry.setReply(replyText);
        System.out.println("Reply sent to enquiry: " + replyText);
    }

    // @Override
    // public void generateReceipt(Applicant applicant, BTOApplication application, String flatType) {
    //     if (application.getStatus().equals("Booked")) {
    //         ((HDBOfficer) applicant).generateReceipt(applicant, application, flatType);
    //     } else {
    //         System.out.println("Application not booked. Cannot generate receipt.");
    //     }
    // }
}

package service;

import model.*;

public class ApplicantServiceImpl implements ApplicantService {

    @Override
    public void checkLogin(String userId, String password) {
    }

    @Override
    public void applyForProject(Applicant applicant, BTOProject project) {
        BTOApplication application = new BTOApplication(applicant, project);
        applicant.addApplication(application);
        System.out.println("Application submitted for project: " + project.getProjectName());
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
}
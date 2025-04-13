package service;

import model.*;

public interface ApplicantService {
    void checkLogin(String userId, String password);
    void applyForProject(Applicant applicant, BTOProject project);
    void requestWithdrawal(Applicant applicant, BTOApplication application);
    void submitEnquiry(Applicant applicant, String enquiryText);
    void editEnquiry(Applicant applicant, int index, String newText);
    void deleteEnquiry(Applicant applicant, int index);
    void viewApplicationStatus(Applicant applicant);
}

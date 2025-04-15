package service;

import model.*;

public interface ApplicantService {
    Applicant checkLogin(String userId, String password);
    void viewAvailableProjects(); 
    void applyForProject(Applicant applicant, String projectName);
    void requestWithdrawal(Applicant applicant, BTOApplication application);
    void submitEnquiry(Applicant applicant, String enquiryText);
    void editEnquiry(Applicant applicant, int index, String newText);
    void deleteEnquiry(Applicant applicant, int index);
    void viewApplicationStatus(Applicant applicant);
}

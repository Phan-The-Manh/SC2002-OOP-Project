package service;

import model.*;

public interface ApplicantService {
    Applicant checkLogin(String userId, String password);
    void viewAvailableProjects(); 
    void applyForProject(Applicant applicant, String projectName);
    void viewApplicationStatus(Applicant applicant);
    void requestWithdrawal(Applicant applicant, String projectName);
    void submitEnquiry(Applicant applicant, String enquiryText);
    void editEnquiry(Applicant applicant, int index, String newText);
    void deleteEnquiry(Applicant applicant, int index);

}

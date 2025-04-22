package service;

import model.*;

public interface ApplicantService {
    Applicant checkLogin(String userId, String password);
    void viewAvailableProjects(Applicant applicant); 
    void viewAvailableProjectsByRoomType(Applicant applicant, int roomType);
    void applyForProject(Applicant applicant, String projectName, int roomType);
    void viewApplicationStatus(Applicant applicant);
    void requestWithdrawal(Applicant applicant);
    void submitEnquiry(Applicant applicant, String enquiryText, String projectName);
    void viewEnquiry(Applicant applicant);
    void editEnquiry(Applicant applicant, int index, String newText);
    void deleteEnquiry(Applicant applicant, int index);

}

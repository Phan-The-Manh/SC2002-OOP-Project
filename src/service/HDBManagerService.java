package service;

import model.*;

public interface HDBManagerService {
    HDBManager checkLogin(String userId, String password);
    void viewAvailableProjects();
    void createProject(HDBManager manager, BTOProject project);
    void editProject(String projectName, BTOProject project);
    void deleteProject(String projectName);
    void toggleProjectVisibility(BTOProject project);
    void manageHDBOfficerRegistration(ProjectRegistration projectRegistration, boolean approve);
    void manageApplicantApplication(BTOApplication application, boolean approve);
    void manageWithdrawalRequest(BTOApplication application, boolean approve);
    void generateReport(HDBManager manager, String filterCriteria);
    void viewEnquiries();
    void replyToEnquiry(HDBManager manager, Enquiry enquiry, String replyText);
    void filterProject(HDBManager manager);
}
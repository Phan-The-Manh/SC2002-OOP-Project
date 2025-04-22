package service;

import model.*;

public interface HDBOfficerService extends ApplicantService {
    void registerForProject(HDBOfficer officer, BTOProject project);
    void viewRegistrationStatus(HDBOfficer officer);
    void viewApplicationByStatus(HDBOfficer officer, String status);
    void approveFlatBooking(Applicant applicant, BTOApplication application);
    void viewProjectEnquiry(HDBOfficer officer);
    void replyToEnquiry(HDBOfficer officer, int index, String replyText);
    void generateReceipt(Applicant applicant, BTOApplication application);

}
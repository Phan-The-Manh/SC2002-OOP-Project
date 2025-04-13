package service;

import model.*;

public interface HDBOfficerService extends ApplicantService {
    void registerForProject(HDBOfficer officer, BTOProject project);
    // void approveFlatBooking(Applicant applicant, BTOApplication application, String flatType);
    void replyToEnquiry(Enquiry enquiry, String replyText);
    // void generateReceipt(Applicant applicant, BTOApplication application, String flatType);
}
package service;

import java.util.List;

import data.Database;
import model.*;

public class HDBOfficerServiceImpl implements HDBOfficerService {

    @Override
    public void registerForProject(HDBOfficer officer, BTOProject project) {
        if (officer.isRegisteredForProject()) {
            System.out.println("Officer is already registered for another project.");
        } else {
            officer.setProjectAssigned(project);
            System.out.println("Officer " + officer.getName() + " successfully registered for project " + project.getProjectName());
        }
    }

     @Override
    public void approveFlatBooking(Applicant applicant, BTOApplication application, String flatType) {
        if (application.getStatus().equals("Successful")) {
            BTOProject project = null; 
            Database db = new Database();
            List<BTOProject> projectList = db.btoProjectList;
        
            // Find project by name
            for (BTOProject p : projectList) {
                if (p.getProjectName().equalsIgnoreCase(application.getProjectName().trim())) {
                    project = p;
                    break;
                }
            }

        } else {
             System.out.println("Application not successful. Cannot approve flat booking.");
        }
    }

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

package service;

import model.BTOApplication;
import data.Database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BTOApplicationServiceImpl implements BTOApplicationService {

    private Database db;

    public BTOApplicationServiceImpl(Database db) {
        this.db = db;
    }

    @Override
    public List<BTOApplication> getAllBTOApplications() {
        // Return all BTO applications from the database (in-memory list)
        return db.btoApplicationList;
    }

    @Override
    public void saveBTOApplication(BTOApplication updatedApplication) {
        // Update the existing application in the list
        boolean updated = false;
        for (int i = 0; i < db.btoApplicationList.size(); i++) {
            BTOApplication existingApp = db.btoApplicationList.get(i);
            if (existingApp.getApplicant().getUserId().equals(updatedApplication.getApplicant().getUserId()) &&
                existingApp.getProjectName().equals(updatedApplication.getProjectName())) {
                db.btoApplicationList.set(i, updatedApplication); // Replace with updated
                updated = true;
                break;
            }
        }
    
        // If not found (new), add to list
        if (!updated) {
            db.btoApplicationList.add(updatedApplication);
        }
    
        // Rewrite entire CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/BTOApplicationList.csv"))) {
            writer.write("Applicant ID,Project Name,Status,Application Date,isRequestedWithdrawal\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
            for (BTOApplication app : db.btoApplicationList) {
                String formattedDate = dateFormat.format(app.getApplicationDate());
                writer.write(
                    app.getApplicant().getUserId() + "," +
                    app.getProjectName() + "," +
                    app.getStatus() + "," +
                    formattedDate + "," +
                    app.isRequestedWithdrawal() + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


    // @Override
    // public void deleteBTOApplication(String applicationId) {
    //     // Find and remove the application from the in-memory list
    //     BTOApplication applicationToDelete = null;
    //     for (BTOApplication application : db.btoApplicationList) {
    //         if (application.getApplicantId().equals(applicationId)) {
    //             applicationToDelete = application;
    //             break;
    //         }
    //     }

    //     if (applicationToDelete != null) {
    //         db.btoApplicationList.remove(applicationToDelete);
    //         // Call method to save the updated list back to the CSV file
    //         saveToCSV();
    //         System.out.println("BTO Application deleted: " + applicationToDelete);
    //     } else {
    //         System.out.println("Application with ID " + applicationId + " not found.");
    //     }
    // }

}

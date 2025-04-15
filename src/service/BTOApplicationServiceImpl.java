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
    public void saveBTOApplication(BTOApplication application) {
        // Add the new application to the in-memory list
        db.btoApplicationList.add(application);
    
        // Save the updated list to the CSV file directly in this method
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/BTOApplicationList.csv", true))) {
            // Write the CSV header only if the file is empty (i.e., first write)
            File file = new File("src/data/BTOApplications.csv");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(application.getApplicationDate());
            // Write the new BTO application as a new line in the CSV file
            writer.write(
                application.getApplicant().getUserId() + "," +
                application.getProjectName() + "," +
                application.getStatus() + "," +
                formattedDate + "\n"
            );
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

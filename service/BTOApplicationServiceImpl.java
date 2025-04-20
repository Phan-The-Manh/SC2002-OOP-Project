package service;

import data.Database;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import model.BTOApplication;

public class BTOApplicationServiceImpl implements BTOApplicationService {

    private Database db;

    public BTOApplicationServiceImpl(Database db) {
        this.db = db;
    }

    //Method to save BTOApplication List to CSV file
    private void saveToCsv(Database db){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/BTOApplicationList.csv"))) {
            writer.write("Applicant ID,Project Name,Status,Room Type,Application Date,isRequestedWithdrawal\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
            for (BTOApplication app : db.btoApplicationList) {
                String formattedDate = dateFormat.format(app.getApplicationDate());
                writer.write(
                    app.getApplicant().getUserId() + "," +
                    app.getProjectName() + "," +
                    app.getStatus() + "," +
                    app.getRoomType() + "," +
                    formattedDate + "," +
                    app.isRequestedWithdrawal() + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        saveToCsv(db);
    }
    


    public void deleteBTOApplication(BTOApplication deletedApplication) {
    // Find and remove the application from the in-memory list
        boolean isExist = false;
        for (BTOApplication application : db.btoApplicationList) {
            if (application.getApplicant().getUserId().equals(deletedApplication.getApplicant().getUserId())) {
                isExist = true;
                db.btoApplicationList.remove(application);
                break;
            }
        }

        if (isExist) {
            // Call method to save the updated list back to the CSV file
            saveToCsv(db);
            System.out.println("BTO Application deleted: " + deletedApplication.getApplicant().getUserId());
        } else {
            System.out.println("Application with ID " + deletedApplication.getApplicant().getUserId() + " not found.");
        }
    }

    

}

package service;
import data.Database;
import model.BTOApplication;
import model.Enquiry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
public class EnquiryServiceImpl {
    private Database db;
    public EnquiryServiceImpl(Database db) {
        this.db = db;
    }


    // Method to retrieve all enquiries
    List<Enquiry> getAllEnquiries(){
        return db.enquiryList;
    }

    // Method to save a new enquiry
    void saveEnquiry(Enquiry updatedEnquiry){
        boolean updated = false;
        for (int i = 0; i < db.enquiryList.size(); i++) {
            Enquiry existingEnquiry = db.enquiryList.get(i);
            if (existingEnquiry.getApplicantName().equals(updatedEnquiry.getApplicantName()) &&
                existingEnquiry.getEnquiryText().equals(updatedEnquiry.getEnquiryText())) {
                db.enquiryList.set(i, updatedEnquiry); // Replace with updated
                updated = true;
                break;
            }
        }
    
        // If not found (new), add to list
        if (!updated) {
            db.enquiryList.add(updatedEnquiry);
        }
    
        // Rewrite entire CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/EnquiryList.csv"))) {
            writer.write("Applicant Name, Enquiry Text,Reply Content,Enquiry Date,Is Replied\n");
    
            for (Enquiry enquiry : db.enquiryList) {
                String dateString = enquiry.getEnquiryDate().toString(); // yyyy-MM-dd
                writer.write(
                    enquiry.getApplicantName() + "," +
                    enquiry.getEnquiryText() + "," +
                    enquiry.getReply() + "," +
                    dateString + "," + 
                    enquiry.isReplied() + "\n"
                );
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void editEnquiry(Enquiry updatedEnquiry, String newText){
        boolean updated = false;
        for (int i = 0; i < db.enquiryList.size(); i++) {
            Enquiry existingEnquiry = db.enquiryList.get(i);
            if (existingEnquiry.getApplicantName().equals(updatedEnquiry.getApplicantName()) &&
                existingEnquiry.getEnquiryText().equals(updatedEnquiry.getEnquiryText())) {
                updatedEnquiry.setEnquiryText(newText);
                db.enquiryList.set(i, updatedEnquiry); // Replace with updated
                updated = true;
                break;
            }
        }
        // If not found (new), add to list
        if (!updated) {
            db.enquiryList.add(updatedEnquiry);
        }
    
        // Rewrite entire CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/EnquiryList.csv"))) {
            writer.write("Applicant Name, Enquiry Text,Reply Content,Enquiry Date,Is Replied\n");
    
            for (Enquiry enquiry : db.enquiryList) {
                String dateString = enquiry.getEnquiryDate().toString(); // yyyy-MM-dd
                writer.write(
                    enquiry.getApplicantName() + "," +
                    enquiry.getEnquiryText() + "," +
                    enquiry.getReply() + "," +
                    dateString + "," + 
                    enquiry.isReplied() + "\n"
                );
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to delete enquiry
    void deleteEnquiry(Enquiry enquiryToDelete){
        for (int i = 0; i < db.enquiryList.size(); i++) {
            Enquiry existingEnquiry = db.enquiryList.get(i);
            if (existingEnquiry.getApplicantName().equals(enquiryToDelete.getApplicantName()) &&
                existingEnquiry.getEnquiryText().equals(enquiryToDelete.getEnquiryText())) {
                db.enquiryList.remove(i); 
                break;
            }
        }
        // Call method to save the updated list back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/EnquiryList.csv"))) {
            writer.write("Applicant Name, Enquiry Text,Reply Content,Enquiry Date,Is Replied\n");
    
            for (Enquiry enquiry : db.enquiryList) {
                String dateString = enquiry.getEnquiryDate().toString(); // yyyy-MM-dd
                writer.write(
                    enquiry.getApplicantName() + "," +
                    enquiry.getEnquiryText() + "," +
                    enquiry.getReply() + "," +
                    dateString + "," + 
                    enquiry.isReplied() + "\n"
                );
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

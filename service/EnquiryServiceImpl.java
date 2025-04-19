package service;

import data.Database;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import model.Enquiry;

public class EnquiryServiceImpl implements EnquiryService {
    private Database db;

    public EnquiryServiceImpl(Database db) {
        this.db = db;
    }

    // Method to retrieve all enquiries
    @Override
    public List<Enquiry> getAllEnquiries() {
        return db.enquiryList;
    }

    // Method to save a new enquiry
    @Override
    public void saveEnquiry(Enquiry updatedEnquiry) {
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
            writer.write("Applicant Name, Enquiry Text, Reply Content, Enquiry Date, Is Replied\n");

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

    // Method to edit an enquiry
    @Override
    public void editEnquiry(Enquiry updatedEnquiry, String newText) {
        boolean updated = false;
        for (int i = 0; i < db.enquiryList.size(); i++) {
            Enquiry existingEnquiry = db.enquiryList.get(i);
            if (existingEnquiry.getApplicantName().equals(updatedEnquiry.getApplicantName()) &&
                existingEnquiry.getEnquiryText().equals(updatedEnquiry.getEnquiryText())) {
                updatedEnquiry.setEnquiryText(newText); // Update the text
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
            writer.write("Applicant Name, Enquiry Text, Reply Content, Enquiry Date, Is Replied\n");

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

    // Method to delete an enquiry
    @Override
    public void deleteEnquiry(Enquiry enquiryToDelete) {
        for (int i = 0; i < db.enquiryList.size(); i++) {
            Enquiry existingEnquiry = db.enquiryList.get(i);
            if (existingEnquiry.getApplicantName().equals(enquiryToDelete.getApplicantName()) &&
                existingEnquiry.getEnquiryText().equals(enquiryToDelete.getEnquiryText())) {
                db.enquiryList.remove(i); // Remove the enquiry
                break;
            }
        }
        // Call method to save the updated list back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/EnquiryList.csv"))) {
            writer.write("Applicant Name, Enquiry Text, Reply Content, Enquiry Date, Is Replied\n");

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

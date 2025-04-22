package service;

import data.Database;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import model.Enquiry;

public class EnquiryServiceImpl implements EnquiryService {
    private final Database db;
    private static final String CSV_FILE_PATH = "src/data/EnquiryList.csv";

    public EnquiryServiceImpl(Database db) {
        this.db = db;
    }

    @Override
    public List<Enquiry> getAllEnquiries() {
        return db.enquiryList;
    }

    @Override
    public void saveEnquiry(Enquiry updatedEnquiry) {
        boolean updated = false;

        for (int i = 0; i < db.enquiryList.size(); i++) {
            Enquiry existing = db.enquiryList.get(i);
            if (existing.getApplicantName().equals(updatedEnquiry.getApplicantName()) &&
                existing.getEnquiryText().equals(updatedEnquiry.getEnquiryText())) {
                db.enquiryList.set(i, updatedEnquiry);
                updated = true;
                break;
            }
        }

        if (!updated) {
            db.enquiryList.add(updatedEnquiry);
        }

        saveToCsv();
    }

    @Override
    public void editEnquiry(Enquiry enquiryToEdit, String newText) {
        for (Enquiry enquiry : db.enquiryList) {
            if (enquiry.getApplicantName().equals(enquiryToEdit.getApplicantName()) &&
                enquiry.getEnquiryText().equals(enquiryToEdit.getEnquiryText())) {
                enquiry.setEnquiryText(newText);
                break;
            }
        }

        saveToCsv();
    }

    @Override
    public void deleteEnquiry(Enquiry enquiryToDelete) {
        db.enquiryList.removeIf(enquiry ->
            enquiry.getApplicantName().equals(enquiryToDelete.getApplicantName()) &&
            enquiry.getEnquiryText().equals(enquiryToDelete.getEnquiryText())
        );

        saveToCsv();
    }

    // Private helper to write CSV with escaped values
    private void saveToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write("Applicant Name,Enquiry Text,Reply Content,Enquiry Date,Is Replied,Project Name\n");

            for (Enquiry enquiry : db.enquiryList) {
                String line = String.join(",",
                    escapeCsv(enquiry.getApplicantName()),
                    escapeCsv(enquiry.getEnquiryText()),
                    escapeCsv(enquiry.getReply()),
                    enquiry.getEnquiryDate().toString(),
                    Boolean.toString(enquiry.isReplied()),
                    escapeCsv(enquiry.getProjectName())
                );
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing EnquiryList.csv: " + e.getMessage());
        }
    }

    // Escape commas, quotes, and newlines in CSV
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}

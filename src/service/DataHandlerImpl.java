package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import data.Database;
import model.BTOApplication;
import model.Enquiry;

public class DataHandlerImpl implements DataHandler {

    private Database db;

    public DataHandlerImpl(Database db) {
        this.db = db;
    }

    @Override
    public void saveBTOApplication(BTOApplication updatedApplication) {
        boolean updated = false;
        for (int i = 0; i < db.btoApplicationList.size(); i++) {
            BTOApplication existingApp = db.btoApplicationList.get(i);
            if (existingApp.getApplicant().getUserId().equals(updatedApplication.getApplicant().getUserId()) &&
                existingApp.getProjectName().equals(updatedApplication.getProjectName())) {
                db.btoApplicationList.set(i, updatedApplication);
                updated = true;
                break;
            }
        }
        if (!updated) {
            db.btoApplicationList.add(updatedApplication);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/BTOApplicationList.csv"))) {
            writer.write("Applicant ID,Project Name,Status,Application Date\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (BTOApplication app : db.btoApplicationList) {
                String formattedDate = dateFormat.format(app.getApplicationDate());
                writer.write(
                    app.getApplicant().getUserId() + "," +
                    app.getProjectName() + "," +
                    app.getStatus() + "," +
                    formattedDate + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBTOApplication(BTOApplication application) {
        db.btoApplicationList.removeIf(app ->
            app.getApplicant().getUserId().equals(application.getApplicant().getUserId()) &&
            app.getProjectName().equals(application.getProjectName())
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/BTOApplicationList.csv"))) {
            writer.write("Applicant ID,Project Name,Status,Application Date\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (BTOApplication app : db.btoApplicationList) {
                String formattedDate = dateFormat.format(app.getApplicationDate());
                writer.write(
                    app.getApplicant().getUserId() + "," +
                    app.getProjectName() + "," +
                    app.getStatus() + "," +
                    formattedDate + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveEnquiry(Enquiry updatedEnquiry) {
        boolean updated = false;
        for (int i = 0; i < db.enquiryList.size(); i++) {
            Enquiry existingEnquiry = db.enquiryList.get(i);
            if (existingEnquiry.getApplicantName().equals(updatedEnquiry.getApplicantName()) &&
                existingEnquiry.getEnquiryText().equals(updatedEnquiry.getEnquiryText())) {
                db.enquiryList.set(i, updatedEnquiry);
                updated = true;
                break;
            }
        }

        if (!updated) {
            db.enquiryList.add(updatedEnquiry);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/EnquiryList.csv"))) {
            writer.write("ApplicantName,EnquiryText,ReplyContent,EnquiryDate,IsReplied\n");

            for (Enquiry enquiry : db.enquiryList) {
                String dateString = enquiry.getEnquiryDate().toString();
                String reply = enquiry.getReply() != null ? enquiry.getReply() : "";
                writer.write(
                    enquiry.getApplicantName() + "," +
                    enquiry.getEnquiryText() + "," +
                    reply + "," +
                    dateString + "," +
                    enquiry.isReplied() + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEnquiry(Enquiry enquiry) {
        db.enquiryList.removeIf(e ->
            e.getApplicantName().equals(enquiry.getApplicantName()) &&
            e.getEnquiryText().equals(enquiry.getEnquiryText())
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/EnquiryList.csv"))) {
            writer.write("ApplicantName,EnquiryText,ReplyContent,EnquiryDate,IsReplied\n");

            for (Enquiry e : db.enquiryList) {
                String dateString = e.getEnquiryDate().toString();
                String reply = e.getReply() != null ? e.getReply() : "";
                writer.write(
                    e.getApplicantName() + "," +
                    e.getEnquiryText() + "," +
                    reply + "," +
                    dateString + "," +
                    e.isReplied() + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

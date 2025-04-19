package service;

import java.util.List;

import model.Enquiry;

public interface EnquiryService {

    // Method to retrieve all enquiries
    List<Enquiry> getAllEnquiries();

    // Method to save a new enquiry
    void saveEnquiry(Enquiry enquiry);

    // Method to delete a BTO application by ID or some identifier
    // void deleteBTOApplication(BTOApplication application);
}


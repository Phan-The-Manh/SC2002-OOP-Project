package service;

import java.util.List;
import model.Enquiry;

public interface EnquiryService {

    // Method to retrieve all enquiries
    List<Enquiry> getAllEnquiries();

    // Method to save a new enquiry
    void saveEnquiry(Enquiry enquiry);

    // Method to edit an existing enquiry
    void editEnquiry(Enquiry updatedEnquiry, String newText);

    // Method to delete an enquiry
    void deleteEnquiry(Enquiry enquiry);
}

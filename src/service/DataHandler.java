package service;

import model.BTOApplication;

public interface DataHandler {
    void saveBTOApplication(BTOApplication application);
    void deleteBTOApplication(BTOApplication application);

    void saveEnquiry(Enquiry enquiry);
    void deleteEnquiry(Enquiry enquiry);
}

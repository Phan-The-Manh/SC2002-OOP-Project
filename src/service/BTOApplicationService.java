package service;

import model.BTOApplication;
import java.util.List;

public interface BTOApplicationService {

    // Method to retrieve all BTO applications
    List<BTOApplication> getAllBTOApplications();

    // Method to save a new BTO application
    void saveBTOApplication(BTOApplication application);

    // Method to delete a BTO application by ID or some identifier
    // void deleteBTOApplication(BTOApplication application);
}

package service;

import java.util.List;
import model.BTOApplication;

public interface BTOApplicationService {

    // Method to retrieve all BTO applications
    List<BTOApplication> getAllBTOApplications();

    // Method to save a new BTO application
    public void saveBTOApplication(BTOApplication application);

    // Method to delete a BTO application by ID or some identifier
    public void deleteBTOApplication(BTOApplication application);
}

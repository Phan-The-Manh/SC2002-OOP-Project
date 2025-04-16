package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BTOProject {
    private String projectName;
    private String neighborhood;

    private int flatType1Units;
    private double flatType1Price;

    private int flatType2Units;
    private double flatType2Price;

    private Date applicationOpenDate;
    private Date applicationCloseDate;

    private HDBManager manager;
    private int availableSlots;
    private List<HDBOfficer> officerList;

    private boolean visibility = true;
    private List<BTOApplication> applications = new ArrayList<>();

    // Constructor
    public BTOProject(String projectName, String neighborhood,
                      int flatType1Units, double flatType1Price,
                      int flatType2Units, double flatType2Price,
                      Date applicationOpenDate, Date applicationCloseDate,
                      HDBManager manager, int availableSlots, List<HDBOfficer> officerList) {

        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.flatType1Units = flatType1Units;
        this.flatType1Price = flatType1Price;
        this.flatType2Units = flatType2Units;
        this.flatType2Price = flatType2Price;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.manager = manager;
        this.availableSlots = availableSlots;
        this.officerList = officerList;
    }

    // Visibility toggle
    public void toggleVisibility() {
        visibility = !visibility;
    }

    // Application
    public void addApplication(BTOApplication application) {
        applications.add(application);
    }

    // Getters (add more if needed)
    public String getProjectName() {
        return projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public int getFlatType1Units() {
        return flatType1Units;
    }
    
    public double getFlatType1Price() {
        return flatType1Price;
    }
    
    public int getFlatType2Units() {
        return flatType2Units;
    }
    
    public double getFlatType2Price() {
        return flatType2Price;
    }
    

    public Date getApplicationOpenDate() {
        return applicationOpenDate;
    }

    public Date getApplicationCloseDate() {
        return applicationCloseDate;
    }

    public List<HDBOfficer> getOfficerList() {
        return officerList;
    }

    public HDBManager getManager() {
        return manager;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public boolean isVisible() {
        return visibility;
    }

    public List<BTOApplication> getApplications() {
        return applications;
    }
}

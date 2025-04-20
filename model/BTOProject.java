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

    private List<Enquiry> enquiries = new ArrayList<>();

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

    @Override
    public String toString() {
        StringBuilder officerNames = new StringBuilder();
        if (officerList != null && !officerList.isEmpty()) {
            for (int i = 0; i < officerList.size(); i++) {
                officerNames.append(officerList.get(i).getName());
                if (i < officerList.size() - 1) {
                    officerNames.append(", ");
                }
            }
        } else {
            officerNames.append("None");
        }

        return "=== BTO Project Information ===\n" +
            "Project Name: " + projectName + "\n" +
            "Neighborhood: " + neighborhood + "\n" +
            "2-Room Units: " + flatType1Units + " | Price: $" + flatType1Price + "\n" +
            "3-Room Units: " + flatType2Units + " | Price: $" + flatType2Price + "\n" +
            "Application Open: " + applicationOpenDate + "\n" +
            "Application Close: " + applicationCloseDate + "\n" +
            "Manager: " + (manager != null ? manager.getName() : "N/A") + "\n" +
            "Available Officer Slots: " + availableSlots + "\n" +
            "Officers Assigned: " + officerNames + "\n" +
            "Visible to Public: " + (visibility ? "Yes" : "No") + "\n" +
            "Applications Received: " + (applications != null ? applications.size() : 0) + "\n";
    }

    // Visibility toggle
    public void toggleVisibility() {
        visibility = !visibility;
    }

    // Application
    public void addApplication(BTOApplication application) {
        applications.add(application);
    }

    // Getters and Setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public int getFlatType1Units() {
        return flatType1Units;
    }

    public void setFlatType1Units(int flatType1Units) {
        this.flatType1Units = flatType1Units;
    }

    public double getFlatType1Price() {
        return flatType1Price;
    }

    public void setFlatType1Price(double flatType1Price) {
        this.flatType1Price = flatType1Price;
    }

    public int getFlatType2Units() {
        return flatType2Units;
    }

    public void setFlatType2Units(int flatType2Units) {
        this.flatType2Units = flatType2Units;
    }

    public double getFlatType2Price() {
        return flatType2Price;
    }

    public void setFlatType2Price(double flatType2Price) {
        this.flatType2Price = flatType2Price;
    }

    public Date getApplicationOpenDate() {
        return applicationOpenDate;
    }

    public void setApplicationOpenDate(Date applicationOpenDate) {
        this.applicationOpenDate = applicationOpenDate;
    }

    public Date getApplicationCloseDate() {
        return applicationCloseDate;
    }

    public void setApplicationCloseDate(Date applicationCloseDate) {
        this.applicationCloseDate = applicationCloseDate;
    }

    public HDBManager getManager() {
        return manager;
    }

    public void setManager(HDBManager manager) {
        this.manager = manager;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public List<HDBOfficer> getOfficerList() {
        return officerList;
    }

    public void setOfficerList(List<HDBOfficer> officerList) {
        this.officerList = officerList;
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public List<BTOApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<BTOApplication> applications) {
        this.applications = applications;
    }

    public List<Enquiry> getEnquiries(){
        return enquiries;
    }
}

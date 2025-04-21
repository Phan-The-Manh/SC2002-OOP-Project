package model;


public class ProjectRegistration {
    private HDBOfficer officer;
    private BTOProject project;
    private String status;

    public ProjectRegistration(HDBOfficer officer, BTOProject project) {
        this.officer = officer;
        this.project = project;
        this.status = "Pending"; // Use double quotes for strings
    }

    // Getters
    public HDBOfficer getOfficer() {
        return officer;
    }

    public BTOProject getProject() {
        return project;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    // Utility
    @Override
    public String toString() {
        return "Officer: " + officer.getName() + 
               " | Project: " + project.getProjectName() +
               " | Status: " + status;
    }
}

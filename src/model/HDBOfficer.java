package model;

public class HDBOfficer extends Applicant {
    private BTOProject projectAssigned;
    private boolean isRegisteredForProject;

    public HDBOfficer(String name, String userId, String password, int age, String maritalStatus, boolean isRegisteredForProject) {
        super(name, userId, password, age, maritalStatus);
        this.isRegisteredForProject = isRegisteredForProject;
    }

    public BTOProject getProjectAssigned() {
        return projectAssigned;
    }

    public void setProjectAssigned(BTOProject projectAssigned) {
        this.projectAssigned = projectAssigned;
        this.isRegisteredForProject = true;
    }

    public boolean isRegisteredForProject() {
        return isRegisteredForProject;
    }
}
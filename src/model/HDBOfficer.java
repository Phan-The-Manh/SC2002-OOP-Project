package model;

public class HDBOfficer extends Applicant {
    private BTOProject projectAssigned;
    private boolean isRegisteredForProject;

    // Constructor
    public HDBOfficer(String name, String userId, int age, String maritalStatus, String password) {
        super(name, userId, age, maritalStatus, password);
        this.isRegisteredForProject = false;  // Default to false until assigned
    }

    // === Inherited from Applicant (and User) ===

    // Getter and Setter for name
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    // Getter and Setter for userId (NRIC)
    @Override
    public String getUserId() {
        return super.getUserId();
    }

    @Override
    public void setUserId(String userId) {
        super.setUserId(userId);
    }

    // Getter and Setter for age
    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
    }

    // Getter and Setter for maritalStatus
    @Override
    public String getMaritalStatus() {
        return super.getMaritalStatus();
    }

    @Override
    public void setMaritalStatus(String maritalStatus) {
        super.setMaritalStatus(maritalStatus);
    }

    // Getter and Setter for password
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    // === HDBOfficer Specific ===

    // Getter for projectAssigned
    public BTOProject getProjectAssigned() {
        return projectAssigned;
    }

    // Setter for projectAssigned
    public void setProjectAssigned(BTOProject projectAssigned) {
        this.projectAssigned = projectAssigned;
        this.isRegisteredForProject = true;  // Mark as registered for project
    }

    // Getter for isRegisteredForProject
    public boolean isRegisteredForProject() {
        return isRegisteredForProject;
    }

    // Setter for isRegisteredForProject
    public void setRegisteredForProject(boolean isRegisteredForProject) {
        this.isRegisteredForProject = isRegisteredForProject;
    }

    // Alternative getter for isRegisteredForProject (in case needed)
    public boolean getIsRegisteredForProject() {
        return isRegisteredForProject;
    }

    // Alternative setter for isRegisteredForProject (in case needed)
    public void setIsRegisteredForProject(boolean isRegisteredForProject) {
        this.isRegisteredForProject = isRegisteredForProject;
    }
}

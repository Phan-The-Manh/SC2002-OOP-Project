package model;

import java.util.List;

public class HDBManager extends User {

    private List<BTOProject> projects;

    // Constructor
    public HDBManager(String name, String userId, int age, String maritalStatus, String password) {
        super(name, userId, age, maritalStatus, password);
    }

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

    // === HDBManager Specific ===

    // Getter for managed projects
    public List<BTOProject> getManagedProjects() {
        return projects;
    }

    // Setter for managed projects (useful if you want to modify the list)
    public void setManagedProjects(List<BTOProject> projects) {
        this.projects = projects;
    }

    // Add a project to the managed list
    public void addProject(BTOProject project) {
        this.projects.add(project);
    }
}

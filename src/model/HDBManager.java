package model;

import java.util.List;

public class HDBManager extends User {

    private List<BTOProject> projects;

    public HDBManager(String name, String userId, String password, int age, String maritalStatus) {
        super(name, userId, password, age, maritalStatus);
    }

    public List<BTOProject> getManagedProjects() {
        return projects;
    }

    public void addProject(BTOProject project) {
        this.projects.add(project);
    }


}
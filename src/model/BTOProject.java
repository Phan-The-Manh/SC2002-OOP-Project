package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BTOProject {
    private String projectName;
    private String neighborhood;
    private int flatType2Room, flatType3Room, flatType4Room;
    private Date applicationOpenDate, applicationCloseDate;
    private int availableSlots;
    private boolean visibility = true;
    private List<BTOApplication> applications = new ArrayList<>();

    public void toggleVisibility() {
        visibility = !visibility;
    }

    public String getProjectName() {
        return projectName;
    }

    public void addApplication(BTOApplication application) {
        applications.add(application);
    }

    public void setFlatType2Room(int n) { flatType2Room = n; }
    public void setFlatType3Room(int n) { flatType3Room = n; }
    public void setFlatType4Room(int n) { flatType4Room = n; }
}
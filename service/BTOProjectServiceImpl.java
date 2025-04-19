package service;

import data.Database;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import model.BTOProject;
import model.HDBOfficer;

public class BTOProjectServiceImpl implements BTOProjectService {
    private Database db;

    public BTOProjectServiceImpl(Database db) {
        this.db = db;
    }

    @Override
    public void writeAllProjectsToCSV() {
        File file = new File("src/data/ProjectList.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer,Visible\n");

            for (BTOProject project : db.btoProjectList) {
                String formattedOpenDate = new SimpleDateFormat("MM/dd/yyyy").format(project.getApplicationOpenDate());
                String formattedCloseDate = new SimpleDateFormat("MM/dd/yyyy").format(project.getApplicationCloseDate());
                long flatType1PriceLong = (long) project.getFlatType1Price();
                long flatType2PriceLong = (long) project.getFlatType2Price();

                writer.write(
                    project.getProjectName() + "," +
                    project.getNeighborhood() + "," +
                    "2-Room," + project.getFlatType1Units() + "," + flatType1PriceLong + "," +
                    "3-Room," + project.getFlatType2Units() + "," + flatType2PriceLong + "," +
                    formattedOpenDate + "," +
                    formattedCloseDate + "," +
                    project.getManager().getName() + "," +
                    project.getAvailableSlots() + "," +
                    String.join(";", project.getOfficerList().stream().map(HDBOfficer::getName).toArray(String[]::new)) + "," +
                    project.isVisible() + "\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save a new BTO project
    @Override
    public void saveProject(BTOProject updatedProject) {
        boolean updated = false;
    
        // Check if the project already exists in memory (by project name)
        for (int i = 0; i < db.btoProjectList.size(); i++) {
            BTOProject existingProject = db.btoProjectList.get(i);
            if (existingProject.getProjectName().equals(updatedProject.getProjectName())) {
                db.btoProjectList.set(i, updatedProject); // Replace existing with updated
                updated = true;
                break;
            }
        }
    
        // If not found, add as new
        if (!updated) {
            db.btoProjectList.add(updatedProject);
        }
    
        writeAllProjectsToCSV();
    }
    
    // Method to edit an existing BTO project
    @Override
    public void editProject(String projectName, BTOProject updatedProject) {
        // 1. Update in-memory list
        for (int i = 0; i < db.btoProjectList.size(); i++) {
            BTOProject existingProject = db.btoProjectList.get(i);
            if (existingProject.getProjectName().equals(projectName)) {
                db.btoProjectList.set(i, updatedProject);
                break;
            }
        }

        writeAllProjectsToCSV();
    }


    // Method to delete a BTO project
    @Override
    public void deleteProject(String projectName) {
        BTOProject toDelete = null;
    
        // Find the project in memory
        for (BTOProject project : db.btoProjectList) {
            if (project.getProjectName().equals(projectName)) {
                toDelete = project;
                break;
            }
        }
    
        if (toDelete != null) {
            db.btoProjectList.remove(toDelete);
        } else {
            System.out.println("Project not found: " + projectName);
            return;
        }
    
        writeAllProjectsToCSV();
    }

    @Override
    public void toggleProjectVisibility(BTOProject project) {
        project.setVisibility(!project.isVisible());
        for (int i = 0; i < db.btoProjectList.size(); i++) {
            BTOProject existingProject = db.btoProjectList.get(i);
            if (existingProject.getProjectName().equals(project.getProjectName())) {
                db.btoProjectList.set(i, project);
                break;
            }
        }
        writeAllProjectsToCSV();
    }

}

package service;

import model.BTOProject;

public interface BTOProjectService {
    void writeAllProjectsToCSV();
    void saveProject(BTOProject project);
    void editProject(String projectName, BTOProject updatedProject);
    void deleteProject(String projectName);
    void toggleProjectVisibility(BTOProject project);
}

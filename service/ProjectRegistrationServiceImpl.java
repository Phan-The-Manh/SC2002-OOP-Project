package service;

import data.Database;
import model.ProjectRegistration;
import java.io.*;
import java.util.*;

public class ProjectRegistrationServiceImpl implements ProjectRegistrationService {
    private final Database db;

    public ProjectRegistrationServiceImpl(Database db) {
        this.db = db;
    }

    @Override
    public void saveProjectRegistration(ProjectRegistration newRegistration) {
        File file = new File("src/data/ProjectRegistrationList.csv");
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        // Load existing registrations
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String header = reader.readLine();
                if (header != null) {
                    lines.add(header);
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String officerId = parts[0];
                        String projectName = parts[1];

                        // If same officer and project, replace the line with new one
                        if (officerId.equals(newRegistration.getOfficer().getNRIC()) &&
                            projectName.equals(newRegistration.getProject().getProjectName())) {
                            lines.add(newRegistration.getOfficer().getNRIC() + "," +
                                      newRegistration.getProject().getProjectName() + "," +
                                      newRegistration.getStatus());
                            updated = true;
                        } else {
                            lines.add(line); // Keep the original line
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // If no update occurred, append new line
        if (!updated) {
            if (lines.isEmpty()) {
                lines.add("Officer ID,Project Name,Status"); // Add header if file was empty
            }
            lines.add(newRegistration.getOfficer().getNRIC() + "," +
                      newRegistration.getProject().getProjectName() + "," +
                      newRegistration.getStatus());
        }

        // Write all lines back
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Project Registration saved/updated: " + newRegistration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

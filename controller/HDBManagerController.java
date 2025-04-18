package controller;

import data.Database;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import model.*;
import service.*;

public class HDBManagerController {

    private final HDBManagerService hdbManagerService;
    private final Scanner scanner;
    private final Database database;

    public HDBManagerController() {
        this.hdbManagerService = new HDBManagerServiceImpl();
        this.scanner = new Scanner(System.in);
        this.database = new Database(); // Assuming the Database class holds the list of projects
    }

    public boolean login() {
        boolean loginSuccessful = false;

        while (!loginSuccessful) {
            System.out.print("Enter Manager ID: ");
            String nric = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            HDBManager loggedInManager = hdbManagerService.checkLogin(nric, password);

            if (loggedInManager != null) {
                CurrentUser.<HDBManager>getInstance().setUser(loggedInManager);
                loginSuccessful = true;
                return true;
            } else {
                System.out.print("Do you want to try again? (y/n): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("n")) {
                    return false;
                }
            }
        }

        return false;
    }

    public void createProject() {
        HDBManager manager = CurrentUser.<HDBManager>getInstance().getUser();

        if (manager == null) {
            System.out.println("No manager is currently logged in.");
            return;
        }

        System.out.println("=== Create New BTO Project ===");

        String projectName;
        while (true) {
            System.out.print("Enter Project Name: ");
            projectName = scanner.nextLine();
            if (!projectName.isBlank()) {
                // Check if the project name already exists in the mock database
                boolean projectExists = checkProjectNameExists(projectName);
                if (projectExists) {
                    System.out.println("A project with this name already exists. Please choose a different name.");
                } else {
                    break; // Break if the name is unique
                }
            } else {
                System.out.println("Project name cannot be empty.");
            }
        }

        String neighborhood;
        while (true) {
            System.out.print("Enter Neighborhood: ");
            neighborhood = scanner.nextLine();
            if (!neighborhood.isBlank()) break;
            System.out.println("Neighborhood cannot be empty.");
        }

        int flatType1Units = 0;
        while (true) {
            System.out.print("Enter number of 2-Room Units: ");
            try {
                flatType1Units = Integer.parseInt(scanner.nextLine());
                if (flatType1Units >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        double flatType1Price = 0;
        while (true) {
            System.out.print("Enter price for 2-Room Unit: ");
            try {
                flatType1Price = Double.parseDouble(scanner.nextLine());
                if (flatType1Price >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        int flatType2Units = 0;
        while (true) {
            System.out.print("Enter number of 3-Room Units: ");
            try {
                flatType2Units = Integer.parseInt(scanner.nextLine());
                if (flatType2Units >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        double flatType2Price = 0;
        while (true) {
            System.out.print("Enter price for 3-Room Unit: ");
            try {
                flatType2Price = Double.parseDouble(scanner.nextLine());
                if (flatType2Price >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // strict parsing

        java.util.Date openDate = null;
        while (true) {
            System.out.print("Enter application open date (yyyy-MM-dd): ");
            try {
                openDate = sdf.parse(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        java.util.Date closeDate = null;
        while (true) {
            System.out.print("Enter application close date (yyyy-MM-dd): ");
            try {
                closeDate = sdf.parse(scanner.nextLine());
                if (!closeDate.before(openDate)) break;
                System.out.println("Close date must be after open date.");
            } catch (Exception e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        int officerSlots = 0;
        while (true) {
            System.out.print("Enter number of officer slots: ");
            try {
                officerSlots = Integer.parseInt(scanner.nextLine());
                if (officerSlots >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        // Create project
        BTOProject project = new BTOProject(
                projectName,
                neighborhood,
                flatType1Units,
                flatType1Price,
                flatType2Units,
                flatType2Price,
                openDate,
                closeDate,
                manager,
                officerSlots,
                new java.util.ArrayList<>() // empty officer list
        );
        hdbManagerService.createProject(manager, project); 
    }

    // Check if the project name already exists in the mock database
    private boolean checkProjectNameExists(String projectName) {
        Database database = new Database(); // Assuming this is how you access the database
        List<BTOProject> projects = database.btoProjectList;
        for (BTOProject project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return true;
            }
        }
        return false;
    }

    void viewAvailableProjects() {
        hdbManagerService.viewAvailableProjects();
    }

    public void editProject() {
        // Get the manager's current user (assuming they're logged in)
        HDBManager manager = CurrentUser.<HDBManager>getInstance().getUser();
        if (manager == null) {
            System.out.println("No manager is currently logged in.");
            return;
        }
    
        // Prompt for the project name to edit
        String projectName = "";
        while (projectName.isEmpty()) {
            System.out.print("Enter the project name to edit: ");
            projectName = scanner.nextLine().trim();
    
            if (projectName.isEmpty()) {
                System.out.println("Project name cannot be empty. Please enter a valid project name.");
            }
        }
    
        // Check if the project exists and if the manager has permission to edit it
        if (!checkManagerHasProjectName(projectName)) {
            System.out.println("You are not authorized to edit this project, or the project does not exist.");
            return;
        }
    
        System.out.println("=== Edit Project Details ===");
    
        // Prompt for new project details (similar to createProject method)
        String neighborhood;
        while (true) {
            System.out.print("Enter Neighborhood: ");
            neighborhood = scanner.nextLine();
            if (!neighborhood.isBlank()) break;
            System.out.println("Neighborhood cannot be empty.");
        }
    
        int flatType1Units = 0;
        while (true) {
            System.out.print("Enter number of 2-Room Units: ");
            try {
                flatType1Units = Integer.parseInt(scanner.nextLine());
                if (flatType1Units >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    
        double flatType1Price = 0;
        while (true) {
            System.out.print("Enter price for 2-Room Unit: ");
            try {
                flatType1Price = Double.parseDouble(scanner.nextLine());
                if (flatType1Price >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    
        int flatType2Units = 0;
        while (true) {
            System.out.print("Enter number of 3-Room Units: ");
            try {
                flatType2Units = Integer.parseInt(scanner.nextLine());
                if (flatType2Units >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    
        double flatType2Price = 0;
        while (true) {
            System.out.print("Enter price for 3-Room Unit: ");
            try {
                flatType2Price = Double.parseDouble(scanner.nextLine());
                if (flatType2Price >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // strict parsing
    
        java.util.Date openDate = null;
        while (true) {
            System.out.print("Enter application open date (yyyy-MM-dd): ");
            try {
                openDate = sdf.parse(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Try again.");
            }
        }
    
        java.util.Date closeDate = null;
        while (true) {
            System.out.print("Enter application close date (yyyy-MM-dd): ");
            try {
                closeDate = sdf.parse(scanner.nextLine());
                if (!closeDate.before(openDate)) break;
                System.out.println("Close date must be after open date.");
            } catch (Exception e) {
                System.out.println("Invalid date format. Try again.");
            }
        }
    
        int officerSlots = 0;
        while (true) {
            System.out.print("Enter number of officer slots: ");
            try {
                officerSlots = Integer.parseInt(scanner.nextLine());
                if (officerSlots >= 0) break;
                System.out.println("Must be non-negative.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    
        // Create updated project object with new details
        BTOProject updatedProject = new BTOProject(
                projectName, // Keep the existing name
                neighborhood,
                flatType1Units,
                flatType1Price,
                flatType2Units,
                flatType2Price,
                openDate,
                closeDate,
                manager,
                officerSlots,
                new java.util.ArrayList<>() // empty officer list (can be updated if needed)
        );
    
        hdbManagerService.editProject(projectName, updatedProject); // Call the service to update the project
    }
    
    

    public void deleteProject() {
        String projectName = "";
        while (projectName.isEmpty()) {
            System.out.print("Enter the project name to delete: ");
            projectName = scanner.nextLine().trim();
    
            if (projectName.isEmpty()) {
                System.out.println("Project name cannot be empty. Please enter a valid project name.");
            }
        }
    
        // Check if the manager owns the project
        if (!checkManagerHasProjectName(projectName)) {
            System.out.println("You are not authorized to delete this project, or the project does not exist.");
            return;
        }
    
        // Call the delete method with the project name
        hdbManagerService.deleteProject(projectName);
    }
    

    public boolean checkManagerHasProjectName(String projectName) {
        // Get the current logged-in manager
        HDBManager manager = CurrentUser.<HDBManager>getInstance().getUser();
        if (manager == null) {
            System.out.println("No manager is currently logged in.");
            return false;
        }
    
        // Check if the manager already manages a project with the given name
        List<BTOProject> projects = database.btoProjectList;
        for (BTOProject project : projects) {
            if (manager.getName().equals(project.getManager().getName())) {
                return true;  // Project found for the current manager
            }
        }
    
        return false;  // No project with that name found for the manager
    }
    
    public BTOProject getProjectByName(String projectName) {
        Database database = new Database();
        List<BTOProject> projects = database.btoProjectList;
    
        for (BTOProject project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }
    
        return null; // If not found
    }
    
    public void toggleProjectVisibility() {
        String projectName = "";
        while (projectName.isEmpty()) {
            System.out.print("Enter the project name to toggle visibility: ");
            projectName = scanner.nextLine().trim();
    
            if (projectName.isEmpty()) {
                System.out.println("Project name cannot be empty. Please enter a valid project name.");
            }
        }
    
        // Check if the manager owns the project
        if (!checkManagerHasProjectName(projectName)) {
            System.out.println("You are not authorized to toggle visibility for this project, or the project does not exist.");
            return;
        }

        BTOProject project = getProjectByName(projectName);
        if (project == null) {
            System.out.println("Project not found: " + projectName);
            return;
        }
        hdbManagerService.toggleProjectVisibility(project);
    }

    public void manageApplicantApplication() {
        System.out.println("=== Applicant Applications ===");
    
        Database db = new Database();
        List<BTOApplication> applications = db.btoApplicationList;
    
        List<BTOApplication> pendingApplications = applications.stream()
            .filter(app -> "Pending".equalsIgnoreCase(app.getStatus()))
            .collect(Collectors.toList());
    
        if (pendingApplications.isEmpty()) {
            System.out.println("No pending applications.");
            return;
        }
    
        for (int i = 0; i < pendingApplications.size(); i++) {
            BTOApplication app = pendingApplications.get(i);
            System.out.println((i + 1) + ". " + app.toString()); // Customize output as needed
        }
    
        Scanner scanner = new Scanner(System.in);
    
        int choice = -1;
        while (choice < 1 || choice > pendingApplications.size()) {
            System.out.print("Choose an application to manage (1-" + pendingApplications.size() + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    
        BTOApplication selectedApp = pendingApplications.get(choice - 1);
    
        String decision = "";
        while (!decision.equalsIgnoreCase("approve") && !decision.equalsIgnoreCase("reject")) {
            System.out.print("Do you want to approve or reject this application? (approve/reject): ");
            decision = scanner.nextLine();
        }
    
        boolean approve = decision.equalsIgnoreCase("approve");
    
        // Call service method
        hdbManagerService.manageApplicantApplication(selectedApp, approve);
    
        System.out.println("Application has been " + (approve ? "approved." : "rejected."));
    }

    public void manageWithdrawalRequest() {
        System.out.println("=== Withdrawal Requests ===");
    
        Database db = new Database();
        List<BTOApplication> applications = db.btoApplicationList;
    
        List<BTOApplication> withdrawalRequests = applications.stream()
            .filter(app -> "Pending Withdrawal".equalsIgnoreCase(app.getStatus()))
            .collect(Collectors.toList());
    
        if (withdrawalRequests.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }
    
        for (int i = 0; i < withdrawalRequests.size(); i++) {
            BTOApplication app = withdrawalRequests.get(i);
            System.out.println((i + 1) + ". " + app.toString()); // Customize output as needed
        }
    
        Scanner scanner = new Scanner(System.in);
    
        int choice = -1;
        while (choice < 1 || choice > withdrawalRequests.size()) {
            System.out.print("Choose a request to manage (1-" + withdrawalRequests.size() + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    
        BTOApplication selectedApp = withdrawalRequests.get(choice - 1);
    
        String decision = "";
        while (!decision.equalsIgnoreCase("approve") && !decision.equalsIgnoreCase("reject")) {
            System.out.print("Do you want to approve or reject this request? (approve/reject): ");
            decision = scanner.nextLine();
        }
    
        boolean approve = decision.equalsIgnoreCase("approve");
    
        // Call service method
        hdbManagerService.manageWithdrawalRequest(selectedApp, approve);
    
        System.out.println("Withdrawal request has been " + (approve ? "approved." : "rejected."));
    }
    
    public void viewEnquiries() {
        hdbManagerService.viewEnquiries();
    }

}

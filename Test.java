import data.Database;
import model.*;

public class Test {
    public static void main(String[] args) {
        Database db = new Database();

        // Print Applicants
        System.out.println("=== Applicants ===");
        for (Applicant a : db.applicantList) {
            System.out.printf("Name: %s, NRIC: %s, Age: %d, Marital Status: %s\n",
                    a.getName(), a.getUserId(), a.getAge(), a.getMaritalStatus());
        }

        // Print Managers
        System.out.println("\n=== HDB Managers ===");
        for (HDBManager m : db.hdbManagerList) {
            System.out.printf("Name: %s, NRIC: %s\n", m.getName(), m.getUserId());
        }

        // Print Officers
        System.out.println("\n=== HDB Officers ===");
        for (HDBOfficer o : db.hdbOfficerList) {
            System.out.printf("Name: %s, NRIC: %s\n", o.getName(), o.getUserId());
        }

        // Print BTO Projects
        System.out.println("\n=== BTO Projects ===");
        for (BTOProject p : db.btoProjectList) {
            System.out.printf("Project: %s, Neighborhood: %s\n", p.getProjectName(), p.getNeighborhood());
            System.out.printf("2-Room: %d units at $%.2f\n", p.getFlatType1Units(), p.getFlatType1Price());
            System.out.printf("3-Room: %d units at $%.2f\n", p.getFlatType2Units(), p.getFlatType2Price());
            System.out.printf("Application Period: %s to %s\n", p.getApplicationOpenDate(), p.getApplicationCloseDate());
            System.out.printf("Manager: %s\n", p.getManager().getName());
            System.out.printf("Officers (%d): ", p.getOfficerList().size());
            for (HDBOfficer o : p.getOfficerList()) {
                System.out.print(o.getName() + " ");
            }
            System.out.println("\n------------------------");
        }
    }
}

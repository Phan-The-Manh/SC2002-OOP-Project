package controller;

import java.util.Scanner;
import service.*;

public class ApplicantController {

    private final ApplicantService applicantService;

    public ApplicantController() {
        this.applicantService = new ApplicantServiceImpl();  // Instantiate the service
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for login details
        System.out.print("Enter NRIC: ");
        String nric = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        // Call the checkLogin method from ApplicantService
        applicantService.checkLogin(nric, password);
        
        scanner.close();
    }
}

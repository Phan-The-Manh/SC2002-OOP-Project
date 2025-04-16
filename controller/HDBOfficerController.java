package controller;

import java.util.Scanner;
import model.*;
import service.*;

public class HDBOfficerController {

    private final HDBOfficerService hdbOfficerService;
    
    public HDBOfficerController() {
        this.hdbOfficerService = new HDBOfficerServiceImpl(); 
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for login details
        System.out.print("Enter Officer ID: ");
        String nric = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        hdbOfficerService.checkLogin(nric, password);
        
        scanner.close();
    }

}
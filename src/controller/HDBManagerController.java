package controller;

import java.util.Scanner;
import service.*;
import model.*;

public class HDBManagerController {

    private final HDBManagerService hdbManagerService;

    public HDBManagerController() {
        this.hdbManagerService = new HDBManagerServiceImpl();
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for login details
        System.out.print("Enter Manager ID: ");
        String nric = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        hdbManagerService.checkLogin(nric, password);
        
        scanner.close();
    }
}
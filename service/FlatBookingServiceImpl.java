package service;
import data.Database;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import model.Applicant;
import model.BTOProject;
import model.FlatBooking;
import model.HDBOfficer;
public class FlatBookingServiceImpl implements FlatBookingService {
    private Database db;
    public FlatBookingServiceImpl(Database db){
        this.db = db;
    }
    // Method to retrieve all Flat bookings
    public List<FlatBooking> getAllFlatBookings(){
        return db.flatBookingList;
    }

        //Method to save the list to the CSV file
    private void saveToCsv(Database db){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/FlatBookingList.csv"))) {
            writer.write("Applicant ID,Project Name,Flat Type\n");
    
            for (FlatBooking booking : db.flatBookingList) {
                writer.write(
                    booking.getApplicant().getUserId()+ "," +
                    booking.getProject().getProjectName() + "," +
                    booking.getFlatType() + "\n"
                );       
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save a new Flat booking
    public void saveFlatBooking(FlatBooking booking){
        boolean updated = false;
        for (int i = 0; i < db.flatBookingList.size(); i++) {
            FlatBooking existingBooking = db.flatBookingList.get(i);
            if (existingBooking.getApplicant().getUserId().equals(booking.getApplicant().getUserId()) &&
                existingBooking.getProject().getProjectName().equals(existingBooking.getProject().getProjectName())) {
                db.flatBookingList.set(i, booking); // Replace with updated
                updated = true;
                break;
            }
        }
    
        // If not found (new), add to list
        if (!updated) {
            db.flatBookingList.add(booking);
        }
    
        // Rewrite entire CSV file
        saveToCsv(db);
    }
    

    // Method to delete a Flat booking
    public void deleteFlatBooking(FlatBooking booking){
        for (int i = 0; i < db.flatBookingList.size(); i++) {
            FlatBooking existingBooking = db.flatBookingList.get(i);
            if (existingBooking.getApplicant().getUserId().equals(booking.getApplicant().getUserId()) &&
                existingBooking.getProject().getProjectName().equals(existingBooking.getProject().getProjectName())) {
                db.flatBookingList.remove(i); // Replace with updated
                break;
            }
        }
        saveToCsv(db);
    }

    public void generateReceipt(FlatBooking booking){
        Applicant applicant = booking.getApplicant();
        BTOProject project = booking.getProject();
        int flatType = booking.getFlatType();
        System.out.println("Flat Booking Receipt");
        System.out.println("===================================");
        System.out.printf("Applicant name: %-20s | NRIC: %-20s\n", applicant.getName(), applicant.getNRIC());
        System.out.printf("Age: %-20s | Marital Status: %-20s\n", applicant.getAge(), applicant.getMaritalStatus());
        System.out.println("Room Type: " + flatType);
        System.out.println("Project Detail:");
        System.out.println("-----------------------------------");
        System.out.printf("Project: %-20s | Neighborhood: %-20s\n", project.getProjectName(), project.getNeighborhood());
        System.out.print("Manager: ");
        System.out.println(project.getManager().getName());
        System.out.print("Officers: ");
            for (HDBOfficer officer : project.getOfficerList()) {
                System.out.print(officer.getName() + " ");
            }
        System.out.println("\n-----------------------------------");
    }
}

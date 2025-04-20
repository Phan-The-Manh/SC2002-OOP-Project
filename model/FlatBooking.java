package model;

public class FlatBooking {
    private int flatType;
    private BTOProject project;
    private Applicant applicant;

    // Constructor
    public FlatBooking(Applicant applicant, BTOProject project, int flatType) {
        this.flatType = flatType;
        this.applicant = applicant;
        this.project = project;
    }

    // Getter for flatType
    public int getFlatType() {
        return flatType;
    }

    // Setter for flatType
    public void setFlatType(int flatType) {
        this.flatType = flatType;
    }

    public Applicant getApplicant(){
        return applicant;
    }

    public void setApplicant(Applicant applicant){
        this.applicant = applicant;
    }

    public BTOProject getProject(){
        return project;
    }

    public void setProject(BTOProject project){
        this.project = project;
    }

    // Method to generate a receipt
    //public void generateReceipt() {
      //  System.out.println("Receipt: Flat " + flatType + " - No. " + flatNumber + ", Booked on: " + bookingDate);
    //}
}

package model;

import java.util.Date;

public class FlatBooking {
    private String flatType;
    private int flatNumber;
    private Date bookingDate;

    // Constructor
    public FlatBooking(String flatType, int flatNumber) {
        this.flatType = flatType;
        this.flatNumber = flatNumber;
        this.bookingDate = new Date();
    }

    // Getter for flatType
    public String getFlatType() {
        return flatType;
    }

    // Setter for flatType
    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    // Getter for flatNumber
    public int getFlatNumber() {
        return flatNumber;
    }

    // Setter for flatNumber
    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    // Getter for bookingDate
    public Date getBookingDate() {
        return bookingDate;
    }

    // Setter for bookingDate
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    // Method to generate a receipt
    public void generateReceipt() {
        System.out.println("Receipt: Flat " + flatType + " - No. " + flatNumber + ", Booked on: " + bookingDate);
    }
}

package model;

import java.util.Date;

public class FlatBooking {
    private String flatType;
    private int flatNumber;
    private Date bookingDate;

    public FlatBooking(String flatType, int flatNumber) {
        this.flatType = flatType;
        this.flatNumber = flatNumber;
        this.bookingDate = new Date();
    }

    public void generateReceipt() {
        System.out.println("Receipt: Flat " + flatType + " - No. " + flatNumber + ", Booked on: " + bookingDate);
    }
}
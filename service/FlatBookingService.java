package service;
import model.FlatBooking;
import java.util.*;
public interface FlatBookingService {

    // Method to retrieve all Flat bookings
    List<FlatBooking> getAllFlatBookings();

    // Method to save a new Flat booking
    void saveFlatBooking(FlatBooking booking);

    // Method to delete a Flat booking
    void deleteFlatBooking(FlatBooking booking);

    void generateReceipt(FlatBooking booking);
}

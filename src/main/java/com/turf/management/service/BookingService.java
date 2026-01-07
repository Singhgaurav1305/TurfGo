package com.turf.management.service;

import com.turf.management.model.Booking;
import com.turf.management.repository.BookingRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // 1. Get All Bookings for Table
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // 2. Verify Booking Logic
    public void verifyBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setStatus("Confirmed"); // Status update
            bookingRepository.save(booking);
        }
    }

    // 3. Analytics Helper Methods
    public long getTotalBookingsCount() {
        return bookingRepository.count();
    }

    public double getTotalEarnings() {
        // Custom query logic needed in repository mostly, but simple version:
        List<Booking> confirmed = bookingRepository.findAll(); // Filter in DB is better usually
        double total = 0;
        for(Booking b : confirmed) {
            if("Confirmed".equalsIgnoreCase(b.getStatus())) {
                total += b.getTotalAmount();
            }
        }
        return total;
    }
}
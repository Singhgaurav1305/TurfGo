package com.turf.management.repository;

import com.turf.management.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Find all bookings for a specific user
    List<Booking> findByUserId(Long userId);
}
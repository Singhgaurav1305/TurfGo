package com.turf.management.model;

import jakarta.persistence.*; // javax ki jagah jakarta
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "turf_id")
    private Turf turf;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate bookingDate;
    private String timeSlot;
    private double totalAmount;
    private String status; // Pending, Confirmed, Cancelled

    // Constructors
    public Booking() {}

    public Booking(Turf turf, User user, LocalDate bookingDate, String timeSlot, double totalAmount, String status) {
        this.turf = turf;
        this.user = user;
        this.bookingDate = bookingDate;
        this.timeSlot = timeSlot;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Turf getTurf() { return turf; }
    public void setTurf(Turf turf) { this.turf = turf; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
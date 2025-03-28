package com.DriveAway.project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Data
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Foreign Key (User who rented)

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Vehicle vehicle; // Foreign Key (Selected Vehicle)

    @Column(nullable = false)
    private int rentalPeriod; // Rental duration in days (User input)

    @Column(nullable = false)
    private String rentalStatus = "Available"; // Default status: pending

    @Column(nullable = false)
    private LocalDate bookingDate; // Date when user wants to book

    @Column(nullable = false)
    private String bookingTime; // Time when user wants to pick up car

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now(); // Auto-generated requested time

    @Column(nullable = false)
    private LocalDateTime expiryTime; // Auto-generated expiry time

    @Column(nullable = false)
    private double paymentAmount; // (rentalPeriod * price) + securityAmount

    // Calculate expiry time (booking date + rental period)
    public void calculateExpiryTime() {
        this.expiryTime = bookingDate.atStartOfDay().plusDays(rentalPeriod);
    }

    // Calculate total payment amount
    public void calculatePaymentAmount() {
        this.paymentAmount = (rentalPeriod * vehicle.getPrice()) + vehicle.getSecurityAmount();
    }
}

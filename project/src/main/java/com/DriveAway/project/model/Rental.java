package com.DriveAway.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "rentals")
@Data

public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Represents the user who booked the car

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false, referencedColumnName = "carId")
    private Vehicle car ; // Represents the rented car

    @Column(nullable = false)
    private int rentalPeriod; // Rental duration (e.g., in days or hours)

    @Column(nullable = false)
    private String rentalStatus; // Status (Pending, Confirmed, Completed, Cancelled)

    private LocalDate bookingDate;
    private LocalTime bookingTime;

    @Column(nullable = false)
    private LocalDateTime createdTime; // Auto-assigned at creation

    private LocalDateTime expiryTime; // 15-minute expiry rule

    @Column(nullable = false)
    private Integer securityAmount; // New field: amount to be paid before booking

    private Integer paymentAmount; // Final rental payment

    @PrePersist
    private void prePersist() {
        this.createdTime = LocalDateTime.now();
        this.expiryTime = this.createdTime.plusMinutes(15);
    }
}

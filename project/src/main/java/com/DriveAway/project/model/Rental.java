package com.DriveAway.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "rentals")
@Data

public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Represents the user who booked the car

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false, referencedColumnName = "carId")
//    @JsonIgnore
    private Vehicle car ; // Represents the rented car

    @Column(nullable = false)
    private int rentalPeriod; // Rental duration (e.g., in days or hours)

    @Column(nullable = false)
    private String rentalStatus = "PENDING"; // Status (Pending, Confirmed, Completed, Cancelled)

    private LocalDate bookingDate;
    private LocalTime bookingTime;

    @Column(nullable = false)
    private LocalDateTime createdTime;
    
    private LocalDateTime expiryTime;

    private Integer totalPaymentAmount;
    
    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
        this.expiryTime = this.createdTime.plusDays(this.rentalPeriod);
    }
}

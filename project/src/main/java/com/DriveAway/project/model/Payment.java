package com.DriveAway.project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String currency = "INR"; // Changed default to INR

    @Column(nullable = false)
    private String paymentStatus;

    private String paymentIntentId;
    
    private LocalDateTime paymentDate;

    @PrePersist
    protected void onCreate() {
        this.paymentDate = LocalDateTime.now();
    }
}
package com.DriveAway.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Vehicle {	  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String fuelType;

    @Column(nullable = false)
    private String transmission;

    @Column(unique = true, nullable = false)
    private String numberPlate;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String status = "available";  // Default status

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int seater;

    @Column(nullable = false)
    private double securityAmount;
}

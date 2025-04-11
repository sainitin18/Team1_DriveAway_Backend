package com.DriveAway.project.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vehicles")
@Data
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
    private String status = "AVAILABLE";

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int seater;

    @Column(nullable = false)
    private double securityAmount;
    
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images;
    
    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true )
    @JsonManagedReference
    private CarFeature carFeature;
}

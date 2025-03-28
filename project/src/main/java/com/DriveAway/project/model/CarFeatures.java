package com.DriveAway.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "car_features")
@Data
public class CarFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carId", nullable = false)
    private Vehicle vehicle; // Foreign Key from Vehicle table

    private boolean spareTyre;
    private boolean toolkit;
    private boolean reverseCamera;
    private boolean adas;
    private boolean abs;
    private boolean tractionControl;
    private boolean twoFrontAirbags;
    private boolean twoSideAirbags;
    private boolean twoRearAirbags;
    private boolean powerWindows;
    private boolean powerSteering;
    private boolean airConditioning;
    private boolean sunroof;
    private boolean fullBootSpace;
    private boolean pushButtonStart;
    private boolean cruiseControl;
    private boolean panoramicSunroof;
    private boolean voiceControl;
    private boolean airPurifier;
    private boolean musicSystem;
    private boolean airFreshener;
    private boolean auxInput;
    private boolean auxCable;
    private boolean bluetooth;
    private boolean usbCharger;
    private boolean ventilatedFrontSeats;
    private boolean sixAirbags;
}

package com.DriveAway.project.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "car_features")
public class CarFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureId;

    @OneToOne
    @JoinColumn(name = "car_id", nullable = false)
    @JsonBackReference
    private Vehicle vehicle;

    private boolean spareTyre;
    private boolean toolkit;
    private boolean reverseCamera;
    private boolean adas;
    private boolean abs;
    private boolean tractionControl;
    private boolean frontAirbags;
    private boolean sideAirbags;
    private boolean rearAirbags;
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

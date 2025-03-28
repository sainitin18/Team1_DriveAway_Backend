package com.DriveAway.project.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car_features")
public class CarFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int featureId;

    @OneToOne
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Vehicle vehicle; // FK from Vehicle table

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

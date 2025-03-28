package com.DriveAway.project.dto;

import com.DriveAway.project.model.*;

import lombok.Data;

@Data
public class CarFeaturesDTO {

    private Long id;
    private Long vehicleId; // Foreign Key as vehicle ID
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

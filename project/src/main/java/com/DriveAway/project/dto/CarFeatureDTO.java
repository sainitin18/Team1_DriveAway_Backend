package com.DriveAway.project.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarFeatureDTO {

    @NotNull
    private Long carId;

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

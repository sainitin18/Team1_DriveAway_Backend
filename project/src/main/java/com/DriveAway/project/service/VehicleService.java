package com.DriveAway.project.service;



import java.util.List;

import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.model.Vehicle;

public interface VehicleService {
    Vehicle addVehicle(VehicleDTO vehicleDTO);
    List<Vehicle> getAllVehicles();
    Vehicle getVehicleById(Long carId);
    Vehicle updateVehicle(Long carId, VehicleDTO vehicleDTO);
    void deleteVehicle(Long carId);
}


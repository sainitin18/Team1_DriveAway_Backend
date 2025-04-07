package com.DriveAway.project.service;



import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.model.Vehicle;

public interface VehicleService {
	Vehicle addVehicle(VehicleDTO vehicleDTO, MultipartFile[] images);
    List<Vehicle> getAllVehicles();
    Vehicle getVehicleById(Long carId);
    Vehicle updateVehicle(Long carId, VehicleDTO vehicleDTO, MultipartFile[] images);
    void deleteVehicle(Long carId);
}


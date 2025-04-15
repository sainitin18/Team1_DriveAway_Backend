package com.DriveAway.project.controller;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.exception.VehicleNotFoundException;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins="*")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // Add Vehicle (Ensuring Validation)
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<Vehicle> addVehicle(
            @RequestPart("vehicle") @Valid VehicleDTO vehicleDTO,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "features", required = false) CarFeatureDTO featureDTO) {
        Vehicle vehicle = vehicleService.addVehicle(vehicleDTO, images, featureDTO);
        return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // Get Vehicle by ID
    @GetMapping("/{carId}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long carId) {
        return ResponseEntity.ok(vehicleService.getVehicleById(carId));
    }

    // Update Vehicle (Ensuring Validation)
    @PutMapping(value = "/{carId}", consumes = { "multipart/form-data" })
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long carId,
            @RequestPart("vehicle") @Valid VehicleDTO vehicleDTO,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "features", required = false) CarFeatureDTO featureDTO) {
    	System.out.println("Feature DTO in controller: " + featureDTO);
        Vehicle updatedVehicle = vehicleService.updateVehicle(carId, vehicleDTO, images, featureDTO);
        return ResponseEntity.ok(updatedVehicle);
    }


    // Delete Vehicle by ID
    @DeleteMapping("/{carId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long carId) {
        vehicleService.deleteVehicle(carId);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
    
    @GetMapping("/available/count")
    public ResponseEntity<Integer> getAvailableVehicleCount() {
        int count = vehicleService.getAvailableVehicleCount();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/{carId}/status")
    public ResponseEntity<?> getCarStatus(@PathVariable Long carId) {
        try {
            String status = vehicleService.getCarStatusById(carId);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (VehicleNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }
}

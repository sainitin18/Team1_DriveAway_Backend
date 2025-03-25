package com.DriveAway.project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // Add a new vehicle
    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleService.addVehicle(vehicleDTO);
        return ResponseEntity.ok(vehicle);
    }

    // Get all vehicles
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // Get vehicle by ID
    @GetMapping("/{carId}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long carId) {
        return ResponseEntity.ok(vehicleService.getVehicleById(carId));
    }

    // Update vehicle details
    @PutMapping("/{carId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long carId, @Valid @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.updateVehicle(carId, vehicleDTO));
    }

    // Delete vehicle by ID
    @DeleteMapping("/{carId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long carId) {
        vehicleService.deleteVehicle(carId);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
}

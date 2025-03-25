package com.DriveAway.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.exception.VehicleAlreadyExistsException;
import com.DriveAway.project.exception.VehicleNotFoundException;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.VehicleRepository;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Vehicle addVehicle(VehicleDTO vehicleDTO) {
        // ✅ Check if vehicle with the same number plate already exists
        Optional<Vehicle> existingVehicle = vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (existingVehicle.isPresent()) {
            throw new VehicleAlreadyExistsException("Vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setFuelType(vehicleDTO.getFuelType());
        vehicle.setTransmission(vehicleDTO.getTransmission());
        vehicle.setNumberPlate(vehicleDTO.getNumberPlate());
        vehicle.setPrice(vehicleDTO.getPrice());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setSeater(vehicleDTO.getSeater());
        vehicle.setSecurityAmount(vehicleDTO.getSecurityAmount());

        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleById(Long carId) {
        return vehicleRepository.findById(carId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + carId));
    }

    @Override
    public Vehicle updateVehicle(Long carId, VehicleDTO vehicleDTO) {
        Vehicle existingVehicle = vehicleRepository.findById(carId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + carId));

        // ✅ Check if another vehicle with the same number plate exists (excluding the current one)
        Optional<Vehicle> vehicleWithSameNumberPlate = vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (vehicleWithSameNumberPlate.isPresent() && !vehicleWithSameNumberPlate.get().getCarId().equals(carId)) {
            throw new VehicleAlreadyExistsException("Another vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists.");
        }

        existingVehicle.setModel(vehicleDTO.getModel());
        existingVehicle.setBrand(vehicleDTO.getBrand());
        existingVehicle.setType(vehicleDTO.getType());
        existingVehicle.setYear(vehicleDTO.getYear());
        existingVehicle.setFuelType(vehicleDTO.getFuelType());
        existingVehicle.setTransmission(vehicleDTO.getTransmission());
        existingVehicle.setNumberPlate(vehicleDTO.getNumberPlate());
        existingVehicle.setPrice(vehicleDTO.getPrice());
        existingVehicle.setStatus(vehicleDTO.getStatus());
        existingVehicle.setColor(vehicleDTO.getColor());
        existingVehicle.setSeater(vehicleDTO.getSeater());
        existingVehicle.setSecurityAmount(vehicleDTO.getSecurityAmount());

        return vehicleRepository.save(existingVehicle);
    }

    @Override
    public void deleteVehicle(Long carId) {
        if (!vehicleRepository.existsById(carId)) {
            throw new VehicleNotFoundException("Vehicle not found with ID: " + carId);
        }
        vehicleRepository.deleteById(carId);
    }
}

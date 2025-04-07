package com.DriveAway.project.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.model.CarFeature;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.CarFeatureRepository;
import com.DriveAway.project.repository.VehicleRepository;

import java.util.Optional;

@Service
public class CarFeatureServiceImpl implements CarFeatureService {

    @Autowired
    private CarFeatureRepository carFeatureRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Add Features
    public CarFeatureDTO addCarFeatures(CarFeatureDTO carFeatureDTO) {
        Vehicle vehicle = vehicleRepository.findById(carFeatureDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + carFeatureDTO.getCarId()));

        Optional<CarFeature> existing = carFeatureRepository.findByVehicle_CarId(vehicle.getCarId());
        if (existing.isPresent()) {
            throw new RuntimeException("Features already exist for this vehicle.");
        }

        CarFeature feature = modelMapper.map(carFeatureDTO, CarFeature.class);
        feature.setFeatureId(null);
        feature.setVehicle(vehicle);

        CarFeature savedFeature = carFeatureRepository.save(feature);
        return modelMapper.map(savedFeature, CarFeatureDTO.class);
    }


    // Update Features
    public CarFeatureDTO updateCarFeatures(Long carId, CarFeatureDTO carFeatureDTO) {
        CarFeature feature = carFeatureRepository.findByVehicle_CarId(carId)
                .orElseThrow(() -> new RuntimeException("Features not found for Car ID: " + carId));

        modelMapper.map(carFeatureDTO, feature);
        CarFeature updatedFeature = carFeatureRepository.save(feature);
        return modelMapper.map(updatedFeature, CarFeatureDTO.class);
    }

    // Get Features
    public CarFeatureDTO getCarFeatures(Long carId) {
        CarFeature feature = carFeatureRepository.findByVehicle_CarId(carId)
                .orElseThrow(() -> new RuntimeException("Features not found for Car ID: " + carId));

        return modelMapper.map(feature, CarFeatureDTO.class);
    }
} 

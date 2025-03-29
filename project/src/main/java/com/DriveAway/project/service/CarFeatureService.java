package com.DriveAway.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.model.CarFeature;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.CarFeatureRepository;
import com.DriveAway.project.repository.VehicleRepository;

import java.util.Optional;

@Service
public class CarFeatureService {

    @Autowired
    private CarFeatureRepository carFeatureRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    // ✅ Add Features
    public CarFeatureDTO addCarFeatures(CarFeatureDTO carFeatureDTO) {
        Vehicle vehicle = vehicleRepository.findById(carFeatureDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + carFeatureDTO.getCarId()));

        CarFeature feature = new CarFeature();
        feature.setVehicle(vehicle);
        setFeatures(feature, carFeatureDTO);

        CarFeature savedFeature = carFeatureRepository.save(feature);
        return convertToDTO(savedFeature);
    }

    // ✅ Update Features
    public CarFeatureDTO updateCarFeatures(Long carId, CarFeatureDTO carFeatureDTO) {
        CarFeature feature = carFeatureRepository.findByVehicle_CarId(carId)
                .orElseThrow(() -> new RuntimeException("Features not found for Car ID: " + carId));

        setFeatures(feature, carFeatureDTO);
        CarFeature updatedFeature = carFeatureRepository.save(feature);
        return convertToDTO(updatedFeature);
    }

    // ✅ Get Features (Only "Yes" Features)
    public CarFeatureDTO getCarFeatures(Long carId) {
        CarFeature feature = carFeatureRepository.findByVehicle_CarId(carId)
                .orElseThrow(() -> new RuntimeException("Features not found for Car ID: " + carId));

        return convertToDTO(feature);
    }

    // ✅ Delete Features
    public String deleteCarFeatures(Long carId) {
        CarFeature feature = carFeatureRepository.findByVehicle_CarId(carId)
                .orElseThrow(() -> new RuntimeException("Features not found for Car ID: " + carId));

        carFeatureRepository.delete(feature);
        return "Car features deleted successfully for Car ID: " + carId;
    }

    // ✅ Helper method to set features
    private void setFeatures(CarFeature feature, CarFeatureDTO carFeatureDTO) {
        feature.setSpareTyre(carFeatureDTO.isSpareTyre());
        feature.setToolkit(carFeatureDTO.isToolkit());
        feature.setReverseCamera(carFeatureDTO.isReverseCamera());
        feature.setAdas(carFeatureDTO.isAdas());
        feature.setAbs(carFeatureDTO.isAbs());
        feature.setTractionControl(carFeatureDTO.isTractionControl());
        feature.setFrontAirbags(carFeatureDTO.isFrontAirbags());
        feature.setSideAirbags(carFeatureDTO.isSideAirbags());
        feature.setRearAirbags(carFeatureDTO.isRearAirbags());
        feature.setPowerWindows(carFeatureDTO.isPowerWindows());
        feature.setPowerSteering(carFeatureDTO.isPowerSteering());
        feature.setAirConditioning(carFeatureDTO.isAirConditioning());
        feature.setSunroof(carFeatureDTO.isSunroof());
        feature.setFullBootSpace(carFeatureDTO.isFullBootSpace());
        feature.setPushButtonStart(carFeatureDTO.isPushButtonStart());
        feature.setCruiseControl(carFeatureDTO.isCruiseControl());
        feature.setPanoramicSunroof(carFeatureDTO.isPanoramicSunroof());
        feature.setVoiceControl(carFeatureDTO.isVoiceControl());
        feature.setAirPurifier(carFeatureDTO.isAirPurifier());
        feature.setMusicSystem(carFeatureDTO.isMusicSystem());
        feature.setAirFreshener(carFeatureDTO.isAirFreshener());
        feature.setAuxInput(carFeatureDTO.isAuxInput());
        feature.setAuxCable(carFeatureDTO.isAuxCable());
        feature.setBluetooth(carFeatureDTO.isBluetooth());
        feature.setUsbCharger(carFeatureDTO.isUsbCharger());
        feature.setVentilatedFrontSeats(carFeatureDTO.isVentilatedFrontSeats());
        feature.setSixAirbags(carFeatureDTO.isSixAirbags());
    }

    // ✅ Convert Entity to DTO (Only "Yes" Features)
    private CarFeatureDTO convertToDTO(CarFeature feature) {
        CarFeatureDTO dto = new CarFeatureDTO();
        dto.setCarId(feature.getVehicle().getCarId());

        if (feature.isSpareTyre()) dto.setSpareTyre(true);
        if (feature.isToolkit()) dto.setToolkit(true);
        if (feature.isReverseCamera()) dto.setReverseCamera(true);
        if (feature.isAdas()) dto.setAdas(true);
        if (feature.isAbs()) dto.setAbs(true);
        if (feature.isTractionControl()) dto.setTractionControl(true);
        if (feature.isFrontAirbags()) dto.setFrontAirbags(true);
        if (feature.isSideAirbags()) dto.setSideAirbags(true);
        if (feature.isRearAirbags()) dto.setRearAirbags(true);
        if (feature.isPowerWindows()) dto.setPowerWindows(true);
        if (feature.isPowerSteering()) dto.setPowerSteering(true);
        if (feature.isAirConditioning()) dto.setAirConditioning(true);
        if (feature.isSunroof()) dto.setSunroof(true);
        if (feature.isFullBootSpace()) dto.setFullBootSpace(true);
        if (feature.isPushButtonStart()) dto.setPushButtonStart(true);
        if (feature.isCruiseControl()) dto.setCruiseControl(true);
        if (feature.isPanoramicSunroof()) dto.setPanoramicSunroof(true);
        if (feature.isVoiceControl()) dto.setVoiceControl(true);
        if (feature.isAirPurifier()) dto.setAirPurifier(true);
        if (feature.isMusicSystem()) dto.setMusicSystem(true);
        if (feature.isAirFreshener()) dto.setAirFreshener(true);
        if (feature.isAuxInput()) dto.setAuxInput(true);
        if (feature.isAuxCable()) dto.setAuxCable(true);
        if (feature.isBluetooth()) dto.setBluetooth(true);
        if (feature.isUsbCharger()) dto.setUsbCharger(true);
        if (feature.isVentilatedFrontSeats()) dto.setVentilatedFrontSeats(true);
        if (feature.isSixAirbags()) dto.setSixAirbags(true);

        return dto;
    }
}

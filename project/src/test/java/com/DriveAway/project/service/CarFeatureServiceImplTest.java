package com.DriveAway.project.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.DriveAway.project.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.model.CarFeature;
import com.DriveAway.project.repository.CarFeatureRepository;
import com.DriveAway.project.repository.VehicleRepository;

public class CarFeatureServiceImplTest {

    @Mock
    private CarFeatureRepository carFeatureRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private CarFeatureServiceImpl carFeatureService;

    private CarFeatureDTO featureDTO;
    private Vehicle vehicle;
    private CarFeature feature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock vehicle
        vehicle = new Vehicle();
        vehicle.setCarId(1L);
        vehicle.setModel("Civic");
        vehicle.setBrand("Honda");

        // Create DTO
        featureDTO = new CarFeatureDTO();
        featureDTO.setCarId(1L);
        featureDTO.setSpareTyre(true);
        featureDTO.setReverseCamera(true);
        featureDTO.setBluetooth(true);

        // Create CarFeature entity
        feature = new CarFeature();
        feature.setVehicle(vehicle);
        feature.setSpareTyre(true);
        feature.setReverseCamera(true);
        feature.setBluetooth(true);
    }

    @Test
    @DisplayName("✅ Add car features - success")
    void givenCarFeatureDTO_whenAddCarFeatures_thenReturnSavedDTO() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(carFeatureRepository.save(any(CarFeature.class))).thenReturn(feature);

        CarFeatureDTO result = carFeatureService.addCarFeatures(featureDTO);

        assertThat(result).isNotNull();
        assertThat(result.getCarId()).isEqualTo(1L);
        assertThat(result.isSpareTyre()).isTrue();
        assertThat(result.isBluetooth()).isTrue();
    }

    @Test
    @DisplayName("❌ Add car features - vehicle not found")
    void givenInvalidCarId_whenAddCarFeatures_thenThrowException() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                carFeatureService.addCarFeatures(featureDTO));

        assertThat(ex.getMessage()).contains("Vehicle not found with ID");
    }

    @Test
    @DisplayName("✅ Update car features - success")
    void givenExistingFeature_whenUpdateCarFeatures_thenReturnUpdatedDTO() {
        when(carFeatureRepository.findByVehicle_CarId(1L)).thenReturn(Optional.of(feature));
        when(carFeatureRepository.save(any(CarFeature.class))).thenReturn(feature);

        CarFeatureDTO updatedDTO = carFeatureService.updateCarFeatures(1L, featureDTO);

        assertThat(updatedDTO).isNotNull();
        assertThat(updatedDTO.getCarId()).isEqualTo(1L);
        assertThat(updatedDTO.isReverseCamera()).isTrue();
    }

    @Test
    @DisplayName("❌ Update car features - not found")
    void givenNonExistingFeature_whenUpdateCarFeatures_thenThrowException() {
        when(carFeatureRepository.findByVehicle_CarId(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                carFeatureService.updateCarFeatures(1L, featureDTO));

        assertThat(ex.getMessage()).contains("Features not found for Car ID");
    }

    @Test
    @DisplayName("✅ Get car features - success")
    void givenValidCarId_whenGetCarFeatures_thenReturnDTO() {
        when(carFeatureRepository.findByVehicle_CarId(1L)).thenReturn(Optional.of(feature));

        CarFeatureDTO dto = carFeatureService.getCarFeatures(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getCarId()).isEqualTo(1L);
        assertThat(dto.isSpareTyre()).isTrue();
        assertThat(dto.isBluetooth()).isTrue();
    }

    @Test
    @DisplayName("❌ Get car features - not found")
    void givenInvalidCarId_whenGetCarFeatures_thenThrowException() {
        when(carFeatureRepository.findByVehicle_CarId(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                carFeatureService.getCarFeatures(1L));

        assertThat(ex.getMessage()).contains("Features not found for Car ID");
    }
}

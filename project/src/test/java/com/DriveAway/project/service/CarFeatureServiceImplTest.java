package com.DriveAway.project.service;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.model.CarFeature;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.CarFeatureRepository;
import com.DriveAway.project.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CarFeatureServiceImplTest {

    @InjectMocks
    private CarFeatureServiceImpl carFeatureService;

    @Mock
    private CarFeatureRepository carFeatureRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Add Car Features - Success")
    void testAddCarFeatures_Success() {
        Long carId = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setCarId(carId);

        CarFeatureDTO inputDTO = new CarFeatureDTO();
        inputDTO.setCarId(carId);
        inputDTO.setAbs(true);

        CarFeature mappedEntity = new CarFeature();
        mappedEntity.setVehicle(vehicle);
        mappedEntity.setAbs(true);

        CarFeature savedEntity = new CarFeature();
        savedEntity.setFeatureId(100L);
        savedEntity.setVehicle(vehicle);
        savedEntity.setAbs(true);

        CarFeatureDTO expectedDTO = new CarFeatureDTO();
        expectedDTO.setCarId(carId);
        expectedDTO.setAbs(true);

        when(vehicleRepository.findById(carId)).thenReturn(Optional.of(vehicle));
        when(carFeatureRepository.findByVehicle_CarId(carId)).thenReturn(Optional.empty());
        when(modelMapper.map(inputDTO, CarFeature.class)).thenReturn(mappedEntity);
        when(carFeatureRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, CarFeatureDTO.class)).thenReturn(expectedDTO);

        CarFeatureDTO result = carFeatureService.addCarFeatures(inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.isAbs()).isTrue();

        verify(vehicleRepository).findById(carId);
        verify(carFeatureRepository).save(mappedEntity);
    }

    @Test
    @DisplayName("Add Car Features - Vehicle Not Found")
    void testAddCarFeatures_VehicleNotFound() {
        Long carId = 99L;
        CarFeatureDTO dto = new CarFeatureDTO();
        dto.setCarId(carId);

        when(vehicleRepository.findById(carId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> carFeatureService.addCarFeatures(dto));

        assertThat(exception.getMessage()).contains("Vehicle not found");
    }

    @Test
    @DisplayName("Add Car Features - Already Exists")
    void testAddCarFeatures_AlreadyExists() {
        Long carId = 1L;
        CarFeatureDTO dto = new CarFeatureDTO();
        dto.setCarId(carId);

        Vehicle vehicle = new Vehicle();
        vehicle.setCarId(carId);

        CarFeature existingFeature = new CarFeature();

        when(vehicleRepository.findById(carId)).thenReturn(Optional.of(vehicle));
        when(carFeatureRepository.findByVehicle_CarId(carId)).thenReturn(Optional.of(existingFeature));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> carFeatureService.addCarFeatures(dto));

        assertThat(exception.getMessage()).contains("already exist");
    }

    @Test
    @DisplayName("Update Car Features - Success")
    void testUpdateCarFeatures_Success() {
        Long carId = 1L;

        CarFeature existingFeature = new CarFeature();
        existingFeature.setFeatureId(100L);
        existingFeature.setAbs(false);

        CarFeatureDTO inputDTO = new CarFeatureDTO();
        inputDTO.setCarId(carId);
        inputDTO.setAbs(true);

        CarFeature updatedEntity = new CarFeature();
        updatedEntity.setFeatureId(100L);
        updatedEntity.setAbs(true);

        CarFeatureDTO resultDTO = new CarFeatureDTO();
        resultDTO.setCarId(carId);
        resultDTO.setAbs(true);

        when(carFeatureRepository.findByVehicle_CarId(carId)).thenReturn(Optional.of(existingFeature));
        doAnswer(invocation -> {
            CarFeatureDTO src = invocation.getArgument(0);
            CarFeature dest = invocation.getArgument(1);
            dest.setAbs(src.isAbs());
            return null;
        }).when(modelMapper).map(inputDTO, existingFeature);

        when(carFeatureRepository.save(existingFeature)).thenReturn(updatedEntity);
        when(modelMapper.map(updatedEntity, CarFeatureDTO.class)).thenReturn(resultDTO);

        CarFeatureDTO result = carFeatureService.updateCarFeatures(carId, inputDTO);

        assertThat(result).isNotNull();
        assertThat(result.isAbs()).isTrue();
    }

    @Test
    @DisplayName("Update Car Features - Not Found")
    void testUpdateCarFeatures_NotFound() {
        Long carId = 2L;
        CarFeatureDTO dto = new CarFeatureDTO();

        when(carFeatureRepository.findByVehicle_CarId(carId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carFeatureService.updateCarFeatures(carId, dto));
    }

    @Test
    @DisplayName("Get Car Features - Success")
    void testGetCarFeatures_Success() {
        Long carId = 1L;
        CarFeature entity = new CarFeature();
        entity.setAbs(true);

        CarFeatureDTO dto = new CarFeatureDTO();
        dto.setCarId(carId);
        dto.setAbs(true);

        when(carFeatureRepository.findByVehicle_CarId(carId)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, CarFeatureDTO.class)).thenReturn(dto);

        CarFeatureDTO result = carFeatureService.getCarFeatures(carId);

        assertThat(result).isNotNull();
        assertThat(result.isAbs()).isTrue();
    }

    @Test
    @DisplayName("Get Car Features - Not Found")
    void testGetCarFeatures_NotFound() {
        when(carFeatureRepository.findByVehicle_CarId(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carFeatureService.getCarFeatures(999L));
    }
}

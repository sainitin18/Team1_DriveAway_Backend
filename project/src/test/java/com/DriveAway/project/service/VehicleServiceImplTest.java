package com.DriveAway.project.service;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.exception.VehicleAlreadyExistsException;
import com.DriveAway.project.exception.VehicleNotFoundException;
import com.DriveAway.project.model.CarFeature;
import com.DriveAway.project.model.Image;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.ImageRepository;
import com.DriveAway.project.repository.VehicleRepository;
import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock private VehicleRepository vehicleRepository;
    @Mock private ImageRepository imageRepository;
    @Mock private Cloudinary cloudinary;
    @Mock private ModelMapper modelMapper;

    @InjectMocks private VehicleServiceImpl vehicleService;

    private VehicleDTO vehicleDTO;
    private Vehicle vehicle;
    private CarFeatureDTO carFeatureDTO;

    @BeforeEach
    void setup() {
        vehicleDTO = new VehicleDTO();
        vehicleDTO.setNumberPlate("MH12AB1234");
        vehicleDTO.setBrand("Toyota");
        vehicleDTO.setModel("Camry");
        vehicleDTO.setYear(2022);
        vehicleDTO.setColor("Black");
        vehicleDTO.setStatus("AVAILABLE");

        carFeatureDTO = new CarFeatureDTO();
        carFeatureDTO.setAbs(true);
        carFeatureDTO.setCruiseControl(true);

        vehicle = new Vehicle();
        vehicle.setCarId(1L);
        vehicle.setNumberPlate("MH12AB1234");
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setYear(2022);
        vehicle.setStatus("AVAILABLE");
        vehicle.setImages(new ArrayList<>());
    }

    @Test
    void addVehicle_Success() {
        MultipartFile[] images = new MultipartFile[0];

        when(vehicleRepository.findByNumberPlate("MH12AB1234")).thenReturn(Optional.empty());
        when(modelMapper.map(vehicleDTO, Vehicle.class)).thenReturn(vehicle);
        when(modelMapper.map(carFeatureDTO, CarFeature.class)).thenReturn(new CarFeature());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle result = vehicleService.addVehicle(vehicleDTO, images, carFeatureDTO);

        assertThat(result).isNotNull();
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void addVehicle_AlreadyExists() {
        when(vehicleRepository.findByNumberPlate("MH12AB1234")).thenReturn(Optional.of(vehicle));

        assertThatThrownBy(() -> vehicleService.addVehicle(vehicleDTO, null, null))
            .isInstanceOf(VehicleAlreadyExistsException.class)
            .hasMessageContaining("already exists");
    }

    @Test
    void getAllVehicles_ShouldReturnList() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        List<Vehicle> result = vehicleService.getAllVehicles();

        assertThat(result).hasSize(1);
    }

    @Test
    void getVehicleById_Found() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getVehicleById(1L);

        assertThat(result.getBrand()).isEqualTo("Toyota");
    }

    @Test
    void getVehicleById_NotFound() {
        when(vehicleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.getVehicleById(2L))
            .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    void deleteVehicle_Success() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(1L);

        vehicleService.deleteVehicle(1L);

        verify(vehicleRepository).deleteById(1L);
    }

    @Test
    void deleteVehicle_NotFound() {
        when(vehicleRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.deleteVehicle(1L))
            .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    void getCarStatusById_Found() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        String status = vehicleService.getCarStatusById(1L);

        assertThat(status).isEqualTo("AVAILABLE");
    }

    @Test
    void getCarStatusById_NotFound() {
        when(vehicleRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.getCarStatusById(99L))
            .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    void getAvailableVehicleCount_ShouldReturnCount() {
        when(vehicleRepository.countByStatus("AVAILABLE")).thenReturn(5);

        int count = vehicleService.getAvailableVehicleCount();

        assertThat(count).isEqualTo(5);
    }
}
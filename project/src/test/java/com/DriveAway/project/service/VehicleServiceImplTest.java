package com.DriveAway.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.exception.VehicleAlreadyExistsException;
import com.DriveAway.project.exception.VehicleNotFoundException;
import com.DriveAway.project.model.Image;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.ImageRepository;
import com.DriveAway.project.repository.VehicleRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private VehicleDTO vehicleDTO;
    private CarFeatureDTO carFeatureDTO;
    private MultipartFile[] images;

    @BeforeEach
    public void setup() {
        vehicleDTO = new VehicleDTO();
        vehicleDTO.setModel("Model A");
        vehicleDTO.setBrand("Brand A");
        vehicleDTO.setNumberPlate("XYZ123");
        vehicleDTO.setPrice(50000.0);
        vehicleDTO.setStatus("AVAILABLE");

        carFeatureDTO = new CarFeatureDTO();
        carFeatureDTO.setSpareTyre(true);
        carFeatureDTO.setToolkit(true);
        // Set other feature fields as needed

        images = new MultipartFile[1];
        // Mock a MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);
        images[0] = mockFile;
    }

    @Test
    public void testAddVehicle_Success() throws IOException {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setCarId(1L);
        when(vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Map<String, Object> uploadResult = Map.of("secure_url", "http://image.url");
        when(cloudinary.uploader().upload(any(byte[].class), eq(ObjectUtils.emptyMap()))).thenReturn(uploadResult);

        // Act
        Vehicle savedVehicle = vehicleService.addVehicle(vehicleDTO, images, carFeatureDTO);

        // Assert
        assertNotNull(savedVehicle);
        assertEquals(vehicle.getCarId(), savedVehicle.getCarId());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void testAddVehicle_AlreadyExists() {
        // Arrange
        when(vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate()))
                .thenReturn(Optional.of(new Vehicle()));

        // Act & Assert
        assertThrows(VehicleAlreadyExistsException.class, () -> {
            vehicleService.addVehicle(vehicleDTO, images, carFeatureDTO);
        });
    }

    @Test
    public void testGetVehicleById_Success() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setCarId(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        // Act
        Vehicle foundVehicle = vehicleService.getVehicleById(1L);

        // Assert
        assertNotNull(foundVehicle);
        assertEquals(vehicle.getCarId(), foundVehicle.getCarId());
    }

    @Test
    public void testGetVehicleById_NotFound() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.getVehicleById(1L);
        });
    }

    @Test
    public void testUpdateVehicle_Success() throws IOException {
        // Arrange
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setCarId(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existingVehicle));

        when(vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(existingVehicle);

        Map<String, Object> uploadResult = Map.of("secure_url", "http://image.url");
        when(cloudinary.uploader().upload(any(byte[].class), eq(ObjectUtils.emptyMap()))).thenReturn(uploadResult);

        // Act
        Vehicle updatedVehicle = vehicleService.updateVehicle(1L, vehicleDTO, images, carFeatureDTO);

        // Assert
        assertNotNull(updatedVehicle);
        assertEquals(existingVehicle.getCarId(), updatedVehicle.getCarId());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void testUpdateVehicle_AlreadyExists() {
        // Arrange
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setCarId(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existingVehicle));

        // Simulate another vehicle with the same number plate
        when(vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate())).thenReturn(Optional.of(new Vehicle()));

        // Act & Assert
        assertThrows(VehicleAlreadyExistsException.class, () -> {
            vehicleService.updateVehicle(1L, vehicleDTO, images, carFeatureDTO);
        });
    }

    @Test
    public void testDeleteVehicle_Success() {
        // Arrange
        when(vehicleRepository.existsById(1L)).thenReturn(true);

        // Act
        vehicleService.deleteVehicle(1L);

        // Assert
        verify(vehicleRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteVehicle_NotFound() {
        // Arrange
        when(vehicleRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(VehicleNotFoundException.class, () -> {
            vehicleService.deleteVehicle(1L);
        });
    }
}

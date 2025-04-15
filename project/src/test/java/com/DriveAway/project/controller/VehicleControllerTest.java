package com.DriveAway.project.controller;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.exception.VehicleNotFoundException;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private Vehicle vehicle;
    private VehicleDTO vehicleDTO;
    private CarFeatureDTO featureDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicle = new Vehicle();
        vehicle.setCarId(1L);
        vehicle.setModel("Civic");
        vehicle.setBrand("Honda");
        vehicle.setNumberPlate("AB12CD3456");
        vehicle.setPrice(27000.0);

        vehicleDTO = new VehicleDTO();
        vehicleDTO.setModel("Civic");
        vehicleDTO.setBrand("Honda");
        vehicleDTO.setNumberPlate("AB12CD3456");
        vehicleDTO.setPrice(27000.0);

        featureDTO = new CarFeatureDTO();
        featureDTO.setAbs(true);
        featureDTO.setAirConditioning(true);
    }

    @Test
    @DisplayName("Add Vehicle with Multipart + Features - Success")
    void givenVehicleDTO_whenAddVehicleWithMultipart_thenReturnSavedVehicle() {
        MultipartFile[] images = new MultipartFile[]{
                new MockMultipartFile("images", "image1.jpg", "image/jpeg", "dummy image content".getBytes())
        };

        when(vehicleService.addVehicle(vehicleDTO, images, featureDTO)).thenReturn(vehicle);

        ResponseEntity<Vehicle> response = vehicleController.addVehicle(vehicleDTO, images, featureDTO);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCarId()).isEqualTo(1L);
        verify(vehicleService).addVehicle(vehicleDTO, images, featureDTO);
    }

    @Test
    @DisplayName("Get All Vehicles - Success")
    void whenGetAllVehicles_thenReturnVehicleList() {
        when(vehicleService.getAllVehicles()).thenReturn(Arrays.asList(vehicle));

        ResponseEntity<List<Vehicle>> response = vehicleController.getAllVehicles();

        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().size()).isEqualTo(1);
        verify(vehicleService).getAllVehicles();
    }

    @Test
    @DisplayName("Get Vehicle by ID - Success")
    void givenVehicleId_whenGetById_thenReturnVehicle() {
        when(vehicleService.getVehicleById(1L)).thenReturn(vehicle);

        ResponseEntity<Vehicle> response = vehicleController.getVehicleById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCarId()).isEqualTo(1L);
        verify(vehicleService).getVehicleById(1L);
    }

    @Test
    @DisplayName("Update Vehicle with Multipart + Features - Success")
    void givenVehicleDTO_whenUpdateVehicle_thenReturnUpdatedVehicle() {
        MultipartFile[] images = new MultipartFile[]{
                new MockMultipartFile("images", "image2.jpg", "image/jpeg", "new dummy image".getBytes())
        };

        when(vehicleService.updateVehicle(1L, vehicleDTO, images, featureDTO)).thenReturn(vehicle);

        ResponseEntity<Vehicle> response = vehicleController.updateVehicle(1L, vehicleDTO, images, featureDTO);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCarId()).isEqualTo(1L);
        verify(vehicleService).updateVehicle(1L, vehicleDTO, images, featureDTO);
    }

    @Test
    @DisplayName("Delete Vehicle - Success")
    void givenVehicleId_whenDelete_thenSuccessMessage() {
        doNothing().when(vehicleService).deleteVehicle(1L);

        ResponseEntity<String> response = vehicleController.deleteVehicle(1L);

        assertThat(response.getBody()).isEqualTo("Vehicle deleted successfully");
        verify(vehicleService).deleteVehicle(1L);
    }

    @Test
    @DisplayName("Get Available Vehicle Count - Success")
    void whenGetAvailableCount_thenReturnCount() {
        when(vehicleService.getAvailableVehicleCount()).thenReturn(3);

        ResponseEntity<Integer> response = vehicleController.getAvailableVehicleCount();

        assertThat(response.getBody()).isEqualTo(3);
        verify(vehicleService).getAvailableVehicleCount();
    }

    // ✅ NEW TEST: Get Car Status by ID - Success
    @Test
    @DisplayName("Get Vehicle Status by ID - Success")
    void givenCarId_whenGetStatus_thenReturnStatus() {
        when(vehicleService.getCarStatusById(1L)).thenReturn("AVAILABLE");

        ResponseEntity<?> response = vehicleController.getCarStatus(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(((Map<?, ?>) response.getBody()).get("status")).isEqualTo("AVAILABLE");
        verify(vehicleService).getCarStatusById(1L);
    }

    // ✅ NEW TEST: Get Car Status by ID - Not Found
    @Test
    @DisplayName("Get Vehicle Status by ID - Not Found")
    void givenInvalidCarId_whenGetStatus_thenReturnError() {
        when(vehicleService.getCarStatusById(99L)).thenThrow(new VehicleNotFoundException("Vehicle not found"));

        ResponseEntity<?> response = vehicleController.getCarStatus(99L);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        assertThat(((Map<?, ?>) response.getBody()).get("error")).isEqualTo("Vehicle not found");
        verify(vehicleService).getCarStatusById(99L);
    }
}

//package com.DriveAway.project.controller;
//
//import com.DriveAway.project.dto.VehicleDTO;
//import com.DriveAway.project.model.Vehicle;
//import com.DriveAway.project.service.VehicleService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class VehicleControllerTest {
//
//    @Mock
//    private VehicleService vehicleService;
//
//    @InjectMocks
//    private VehicleController vehicleController;
//
//    private Vehicle vehicle;
//    private VehicleDTO vehicleDTO;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // ✅ Sample Vehicle Data
//        vehicle = new Vehicle();
//        vehicle.setCarId(1L);
//        vehicle.setModel("Civic");
//        vehicle.setBrand("Honda");
//        vehicle.setType("Sedan");
//        vehicle.setYear(2024);
//        vehicle.setFuelType("Hybrid");
//        vehicle.setTransmission("Automatic");
//        vehicle.setNumberPlate("AB12CD3456");
//        vehicle.setPrice(27000.0);
//        vehicle.setColor("Black");
//        vehicle.setSeater(5);
//        vehicle.setSecurityAmount(5000.0);
//
//        // ✅ Sample VehicleDTO Data
//        vehicleDTO = new VehicleDTO();
//        vehicleDTO.setModel("Civic");
//        vehicleDTO.setBrand("Honda");
//        vehicleDTO.setType("Sedan");
//        vehicleDTO.setYear(2024);
//        vehicleDTO.setFuelType("Hybrid");
//        vehicleDTO.setTransmission("Automatic");
//        vehicleDTO.setNumberPlate("AB12CD3456");
//        vehicleDTO.setPrice(27000.0);
//        vehicleDTO.setColor("Black");
//        vehicleDTO.setSeater(5);
//        vehicleDTO.setSecurityAmount(5000.0);
//    }
//
//    // ✅ Test: Adding a Vehicle
//    @Test
//    @DisplayName("JUnit test for adding a vehicle (Success)")
//    void givenVehicleDTO_whenAddVehicle_thenReturnSavedVehicle() {
//        when(vehicleService.addVehicle(vehicleDTO)).thenReturn(vehicle);
//
//        ResponseEntity<Vehicle> response = vehicleController.addVehicle(vehicleDTO);
//
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().getCarId()).isEqualTo(1L);
//        verify(vehicleService, times(1)).addVehicle(vehicleDTO);
//    }
//
//    // ✅ Test: Getting All Vehicles
//    @Test
//    @DisplayName("JUnit test for getting all vehicles (Success)")
//    void whenGetAllVehicles_thenReturnVehicleList() {
//        when(vehicleService.getAllVehicles()).thenReturn(Arrays.asList(vehicle));
//
//        ResponseEntity<List<Vehicle>> response = vehicleController.getAllVehicles();
//
//        assertThat(response.getBody()).isNotEmpty();
//        assertThat(response.getBody().size()).isEqualTo(1);
//        verify(vehicleService, times(1)).getAllVehicles();
//    }
//
//    // ✅ Test: Getting Vehicle by ID
//    @Test
//    @DisplayName("JUnit test for getting vehicle by ID (Success)")
//    void givenVehicleId_whenGetVehicleById_thenReturnVehicle() {
//        when(vehicleService.getVehicleById(1L)).thenReturn(vehicle);
//
//        ResponseEntity<Vehicle> response = vehicleController.getVehicleById(1L);
//
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().getCarId()).isEqualTo(1L);
//        verify(vehicleService, times(1)).getVehicleById(1L);
//    }
//
//    // ✅ Test: Updating a Vehicle
//    @Test
//    @DisplayName("JUnit test for updating a vehicle (Success)")
//    void givenVehicleDTO_whenUpdateVehicle_thenReturnUpdatedVehicle() {
//        when(vehicleService.updateVehicle(1L, vehicleDTO)).thenReturn(vehicle);
//
//        ResponseEntity<Vehicle> response = vehicleController.updateVehicle(1L, vehicleDTO);
//
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().getCarId()).isEqualTo(1L);
//        verify(vehicleService, times(1)).updateVehicle(1L, vehicleDTO);
//    }
//
//    // ✅ Test: Deleting a Vehicle
//    @Test
//    @DisplayName("JUnit test for deleting a vehicle (Success)")
//    void givenVehicleId_whenDeleteVehicle_thenRemoveVehicle() {
//        doNothing().when(vehicleService).deleteVehicle(1L);
//
//        ResponseEntity<String> response = vehicleController.deleteVehicle(1L);
//
//        assertThat(response.getBody()).isEqualTo("Vehicle deleted successfully");
//        verify(vehicleService, times(1)).deleteVehicle(1L);
//    }
//}

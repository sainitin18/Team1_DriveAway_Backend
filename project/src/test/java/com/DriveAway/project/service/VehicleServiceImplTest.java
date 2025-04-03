package com.DriveAway.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.DriveAway.project.dto.VehicleDTO;

import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle vehicle;
    private VehicleDTO vehicleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ✅ Sample Test Data
        vehicle = new Vehicle();
        vehicle.setCarId(1L);
        vehicle.setModel("Civic");
        vehicle.setBrand("Honda");
        vehicle.setType("Sedan");
        vehicle.setYear(2024);
        vehicle.setFuelType("Hybrid");
        vehicle.setTransmission("Automatic");
        vehicle.setNumberPlate("AB12CD3456");
        vehicle.setPrice(27000.0);
        vehicle.setColor("Black");
        vehicle.setSeater(5);
        vehicle.setSecurityAmount(5000.0);

        vehicleDTO = new VehicleDTO();
        vehicleDTO.setModel("Civic");
        vehicleDTO.setBrand("Honda");
        vehicleDTO.setType("Sedan");
        vehicleDTO.setYear(2024);
        vehicleDTO.setFuelType("Hybrid");
        vehicleDTO.setTransmission("Automatic");
        vehicleDTO.setNumberPlate("AB12CD3456");
        vehicleDTO.setPrice(27000.0);
        vehicleDTO.setColor("Black");
        vehicleDTO.setSeater(5);
        vehicleDTO.setSecurityAmount(5000.0);
    }

    // ✅ Test for adding a vehicle successfully
    @Test
    @DisplayName("JUnit test for adding a vehicle (Success)")
    void givenVehicleDTO_whenAddVehicle_thenReturnSavedVehicle() {
        when(vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle savedVehicle = vehicleService.addVehicle(vehicleDTO);

        assertThat(savedVehicle).isNotNull();
        assertThat(savedVehicle.getCarId()).isEqualTo(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    // ✅ Test for retrieving all vehicles successfully
    @Test
    @DisplayName("JUnit test for getting all vehicles (Success)")
    void whenGetAllVehicles_thenReturnVehicleList() {
        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(vehicle));

        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        assertThat(vehicles).isNotEmpty();
        assertThat(vehicles.size()).isEqualTo(1);
        verify(vehicleRepository, times(1)).findAll();
    }

    // ✅ Test for retrieving a vehicle by ID successfully
    @Test
    @DisplayName("JUnit test for getting vehicle by ID (Success)")
    void givenVehicleId_whenGetVehicleById_thenReturnVehicle() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Vehicle foundVehicle = vehicleService.getVehicleById(1L);

        assertThat(foundVehicle).isNotNull();
        assertThat(foundVehicle.getCarId()).isEqualTo(1L);
        verify(vehicleRepository, times(1)).findById(1L);
    }

    // ✅ Test for updating an existing vehicle successfully
    @Test
    @DisplayName("JUnit test for updating a vehicle (Success)")
    void givenVehicleDTO_whenUpdateVehicle_thenReturnUpdatedVehicle() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle updatedVehicle = vehicleService.updateVehicle(1L, vehicleDTO);

        assertThat(updatedVehicle).isNotNull();
        assertThat(updatedVehicle.getCarId()).isEqualTo(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    // ✅ Test for deleting a vehicle successfully
    @Test
    @DisplayName("JUnit test for deleting a vehicle (Success)")
    void givenVehicleId_whenDeleteVehicle_thenRemoveVehicle() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(1L);

        vehicleService.deleteVehicle(1L);

        verify(vehicleRepository, times(1)).deleteById(1L);
    }
}

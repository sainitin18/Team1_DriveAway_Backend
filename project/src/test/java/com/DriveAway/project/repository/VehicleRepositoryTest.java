package com.DriveAway.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.DriveAway.project.model.Vehicle;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle vehicle;

    @BeforeEach
    public void setUp() {
        vehicle = new Vehicle();
        vehicle.setModel("Civic");
        vehicle.setBrand("Honda");
        vehicle.setType("Sedan");
        vehicle.setYear(2024);
        vehicle.setFuelType("Hybrid");
        vehicle.setTransmission("Automatic");
        vehicle.setNumberPlate("NP" + UUID.randomUUID().toString().substring(0, 6)); // Ensures unique plate
        vehicle.setPrice(27000.0);
        vehicle.setColor("Black");
        vehicle.setSeater(5);
        vehicle.setSecurityAmount(5500.0);
    }

    // ✅ Test for saving a vehicle
    @Test
    @DisplayName("JUnit test for saving a vehicle")
    public void givenVehicleObject_whenSave_thenReturnSavedVehicle() {
        // when
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // then
        assertThat(savedVehicle).isNotNull();
        assertThat(savedVehicle.getCarId()).isGreaterThan(0);
    }

    // ✅ Test for finding a vehicle by ID
    @Test
    @DisplayName("JUnit test for finding a vehicle by ID")
    public void givenVehicleId_whenFindById_thenReturnVehicle() {
        // given
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // when
        Optional<Vehicle> foundVehicle = vehicleRepository.findById(savedVehicle.getCarId());

        // then
        assertThat(foundVehicle).isPresent();
        assertThat(foundVehicle.get().getCarId()).isEqualTo(savedVehicle.getCarId());
    }

    // ✅ Test for finding a vehicle by number plate
    @Test
    @DisplayName("JUnit test for finding a vehicle by number plate")
    public void givenNumberPlate_whenFindByNumberPlate_thenReturnVehicle() {
        // given
        vehicleRepository.save(vehicle);

        // when
        Optional<Vehicle> foundVehicle = vehicleRepository.findByNumberPlate(vehicle.getNumberPlate());

        // then
        assertThat(foundVehicle).isPresent();
        assertThat(foundVehicle.get().getNumberPlate()).isEqualTo(vehicle.getNumberPlate());
    }

    // ✅ Test for deleting a vehicle
    @Test
    @DisplayName("JUnit test for deleting a vehicle")
    public void givenVehicle_whenDelete_thenRemoveVehicle() {
        // given
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // when
        vehicleRepository.deleteById(savedVehicle.getCarId());
        Optional<Vehicle> deletedVehicle = vehicleRepository.findById(savedVehicle.getCarId());

        // then
        assertThat(deletedVehicle).isEmpty();
    }

    @AfterEach
    public void tearDown() {
        vehicleRepository.deleteAll();
    }
}

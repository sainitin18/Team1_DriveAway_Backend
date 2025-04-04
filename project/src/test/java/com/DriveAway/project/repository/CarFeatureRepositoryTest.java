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

import com.DriveAway.project.model.CarFeature;
import com.DriveAway.project.model.Vehicle;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarFeatureRepositoryTest {

    @Autowired
    private CarFeatureRepository carFeatureRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle vehicle;
    private CarFeature carFeature;

    @BeforeEach
    public void setUp() {
        // Create a vehicle object
        vehicle = new Vehicle();
        vehicle.setModel("Civic");
        vehicle.setBrand("Honda");
        vehicle.setType("Sedan");
        vehicle.setYear(2024);
        vehicle.setFuelType("Hybrid");
        vehicle.setTransmission("Automatic");
        vehicle.setNumberPlate("NP" + UUID.randomUUID().toString().substring(0, 6));
        vehicle.setPrice(27000.0);
        vehicle.setColor("Black");
        vehicle.setSeater(5);
        vehicle.setSecurityAmount(5500.0);
        vehicle = vehicleRepository.save(vehicle); // Save vehicle before using it in CarFeature

        // Create car feature object
        carFeature = new CarFeature();
        carFeature.setVehicle(vehicle);
        carFeature.setSpareTyre(true);
        carFeature.setToolkit(false);
        carFeature.setReverseCamera(true);
        carFeature.setAdas(false);
        carFeature.setAbs(true);
        carFeature.setBluetooth(true);
        carFeature = carFeatureRepository.save(carFeature);
    }

    // ✅ Test for saving a car feature
    @Test
    @DisplayName("JUnit test for saving a car feature")
    public void givenCarFeatureObject_whenSave_thenReturnSavedCarFeature() {
        // when
        CarFeature savedFeature = carFeatureRepository.save(carFeature);

        // then
        assertThat(savedFeature).isNotNull();
        assertThat(savedFeature.getFeatureId()).isGreaterThan(0);
    }

    // ✅ Test for finding a car feature by car ID
    @Test
    @DisplayName("JUnit test for finding a car feature by car ID")
    public void givenCarId_whenFindByVehicleCarId_thenReturnCarFeature() {
        // when
        Optional<CarFeature> foundFeature = carFeatureRepository.findByVehicle_CarId(vehicle.getCarId());

        // then
        assertThat(foundFeature).isPresent();
        assertThat(foundFeature.get().getVehicle().getCarId()).isEqualTo(vehicle.getCarId());
    }

    // ✅ Test for deleting a car feature
    @Test
    @DisplayName("JUnit test for deleting a car feature")
    public void givenCarFeature_whenDelete_thenRemoveCarFeature() {
        // when
        carFeatureRepository.deleteById(carFeature.getFeatureId());
        Optional<CarFeature> deletedFeature = carFeatureRepository.findById(carFeature.getFeatureId());

        // then
        assertThat(deletedFeature).isEmpty();
    }

    @AfterEach
    public void tearDown() {
        carFeatureRepository.deleteAll();
        vehicleRepository.deleteAll();
    }
}

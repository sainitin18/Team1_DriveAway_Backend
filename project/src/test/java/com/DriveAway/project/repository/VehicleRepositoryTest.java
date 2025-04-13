package com.DriveAway.project.repository;

import com.DriveAway.project.model.Vehicle;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle vehicle;

    @BeforeEach
    public void setup() {
        vehicle = new Vehicle();
        vehicle.setModel("Civic");
        vehicle.setBrand("Honda");
        vehicle.setType("Sedan");
        vehicle.setYear(2024);
        vehicle.setFuelType("Hybrid");
        vehicle.setTransmission("Automatic");
        vehicle.setNumberPlate("NP" + UUID.randomUUID().toString().substring(0, 6));
        vehicle.setPrice(25000.0);
        vehicle.setColor("Red");
        vehicle.setSeater(5);
        vehicle.setSecurityAmount(5000.0);
        vehicle.setStatus("AVAILABLE");
    }

    @Test
    @DisplayName("Test saving a vehicle")
    @Order(1)
    public void givenVehicle_whenSave_thenReturnSavedVehicle() {
        Vehicle saved = vehicleRepository.save(vehicle);

        assertThat(saved).isNotNull();
        assertThat(saved.getCarId()).isGreaterThan(0);
        assertThat(saved.getModel()).isEqualTo("Civic");
    }

    @Test
    @DisplayName("Test finding a vehicle by ID")
    @Order(2)
    public void givenVehicleId_whenFindById_thenReturnVehicle() {
        Vehicle saved = vehicleRepository.save(vehicle);
        Optional<Vehicle> result = vehicleRepository.findById(saved.getCarId());

        assertThat(result).isPresent();
        assertThat(result.get().getCarId()).isEqualTo(saved.getCarId());
    }

    @Test
    @DisplayName("Test finding a vehicle by number plate")
    @Order(3)
    public void givenNumberPlate_whenFindByNumberPlate_thenReturnVehicle() {
        Vehicle saved = vehicleRepository.save(vehicle);
        Optional<Vehicle> found = vehicleRepository.findByNumberPlate(saved.getNumberPlate());

        assertThat(found).isPresent();
        assertThat(found.get().getBrand()).isEqualTo("Honda");
    }

    @Test
    @DisplayName("Test counting vehicles by status")
    @Order(4)
    public void givenStatus_whenCountByStatus_thenReturnCount() {
        vehicle.setStatus("AVAILABLE");
        vehicleRepository.save(vehicle);

        int count = vehicleRepository.countByStatus("AVAILABLE");
        assertThat(count).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Test deleting a vehicle by ID")
    @Order(5)
    public void givenVehicleId_whenDelete_thenVehicleShouldBeRemoved() {
        Vehicle saved = vehicleRepository.save(vehicle);
        Long id = saved.getCarId();

        vehicleRepository.deleteById(id);
        Optional<Vehicle> deleted = vehicleRepository.findById(id);

        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Test finding all vehicles")
    @Order(6)
    public void givenVehicles_whenFindAll_thenReturnVehicleList() {
        Vehicle v1 = new Vehicle();
        v1.setModel("Swift");
        v1.setBrand("Suzuki");
        v1.setType("Hatchback");
        v1.setYear(2022);
        v1.setFuelType("Petrol");
        v1.setTransmission("Manual");
        v1.setNumberPlate("MH12AB" + UUID.randomUUID().toString().substring(0, 4));
        v1.setPrice(9000.0);
        v1.setColor("Blue");
        v1.setSeater(5);
        v1.setSecurityAmount(1000.0);
        v1.setStatus("AVAILABLE");

        vehicleRepository.save(vehicle);
        vehicleRepository.save(v1);

        List<Vehicle> all = vehicleRepository.findAll();
        assertThat(all).isNotEmpty();
        assertThat(all.size()).isGreaterThanOrEqualTo(2);
    }

    @AfterEach
    public void cleanUp() {
        vehicleRepository.deleteAll();
    }
}

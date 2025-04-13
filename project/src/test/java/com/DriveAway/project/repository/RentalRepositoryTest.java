package com.DriveAway.project.repository;

import com.DriveAway.project.model.Rental;
import com.DriveAway.project.model.User;
import com.DriveAway.project.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Vehicle vehicle;
    private Rental rental;

    @BeforeEach
    void setUp() {
        // Create and persist the User with a unique mobile number
        user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setAadharNumber("123456789012");
        user.setDrivingLicense("DL1234567890");
        user.setMobileNumber("9876543210" + System.currentTimeMillis()); // Ensure unique mobile number
        user.setRole("USER");
        user.setStatus("APPROVED");
        entityManager.persist(user);

        // Create and persist the Vehicle
        vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setType("Sedan");
        vehicle.setYear(2022);
        vehicle.setFuelType("Petrol");
        vehicle.setTransmission("Automatic");
        vehicle.setNumberPlate("KA01AB1234");
        vehicle.setPrice(3000);
        vehicle.setStatus("AVAILABLE");
        vehicle.setColor("White");
        vehicle.setSeater(5);
        vehicle.setSecurityAmount(5000);
        entityManager.persist(vehicle);

        // Create and persist the Rental
        rental = new Rental();
        rental.setUser(user);
        rental.setCar(vehicle);
        rental.setRentalPeriod(3);
        rental.setRentalStatus("PENDING");
        rental.setBookingDate(LocalDate.now());
        rental.setBookingTime(LocalTime.now());
        rental.setCreatedTime(LocalDateTime.now());
        rental.setExpiryTime(LocalDateTime.now().plusDays(3));
        rental.setTotalPaymentAmount(9000);
        entityManager.persist(rental);
    }

    @Test
    void testFindByUserAndStatus() {
        List<Rental> rentals = rentalRepository.findByUserAndStatus(user.getUserId(), "PENDING");
        assertThat(rentals).isNotEmpty();
        assertThat(rentals.get(0).getRentalStatus()).isEqualTo("PENDING");
    }

    @Test
    void testFindByRentalStatus() {
        List<Rental> rentals = rentalRepository.findByRentalStatus("PENDING");
        assertThat(rentals).hasSize(1);
    }

    @Test
    void testExistsByUserIdCarIdAndStatus() {
        boolean exists = rentalRepository.existsByUserIdCarIdAndStatus(user.getUserId(), vehicle.getCarId(), "PENDING");
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByUserId() {
        List<Rental> rentals = rentalRepository.findByUserId(user.getUserId());
        assertThat(rentals).hasSize(1);
        assertThat(rentals.get(0).getUser().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testCountByRentalStatus() {
        int count = rentalRepository.countByRentalStatus("PENDING");
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testFindAll() {
        List<Rental> rentals = rentalRepository.findAll();
        assertThat(rentals).isNotEmpty();
    }
}

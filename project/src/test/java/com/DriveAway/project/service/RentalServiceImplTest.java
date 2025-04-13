package com.DriveAway.project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.DriveAway.project.dto.RentalDTO;
import com.DriveAway.project.dto.RentalResponseDTO;
import com.DriveAway.project.dto.UserBookingDTO;
import com.DriveAway.project.model.Image;
import com.DriveAway.project.model.Rental;
import com.DriveAway.project.model.User;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.RentalRepository;
import com.DriveAway.project.repository.UserRepository;
import com.DriveAway.project.repository.VehicleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentalServiceImplTest {

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    private User user;
    private Vehicle vehicle;
    private Rental rental;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("John Doe");

        vehicle = new Vehicle();
        vehicle.setCarId(1L);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setStatus("AVAILABLE");

        // Mock image and set to vehicle
        Image image = new Image();
        image.setId(1L);
        image.setUrl("http://example.com/image.jpg");
        vehicle.setImages(List.of(image)); // 💡 Prevents NullPointerException

        rental = new Rental();
        rental.setRentalId(1L);
        rental.setUser(user);
        rental.setCar(vehicle);
        rental.setRentalPeriod(7);
        rental.setRentalStatus("PENDING");
        rental.setTotalPaymentAmount(2000);
        rental.setBookingDate(LocalDate.parse("2025-04-13"));
        rental.setBookingTime(LocalTime.parse("10:00"));
    }

    @Test
    void testGetRentalsByStatus() {
        when(rentalRepository.findByRentalStatus("PENDING")).thenReturn(List.of(rental));

        List<RentalResponseDTO> result = rentalService.getRentalsByStatus("PENDING");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getRentalStatus());
        verify(rentalRepository).findByRentalStatus("PENDING");
    }

    @Test
    void testGetUserBookings() {
        when(rentalRepository.findByUserId(1L)).thenReturn(List.of(rental));

        List<UserBookingDTO> result = rentalService.getUserBookings(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PENDING", result.get(0).getRentalStatus());
        verify(rentalRepository).findByUserId(1L);
    }

    @Test
    void testCreateBooking() {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setUserId(1L);
        rentalDTO.setCarId(1L);
        rentalDTO.setRentalPeriod(7);
        rentalDTO.setTotalPaymentAmount(2000);
        rentalDTO.setBookingDate(LocalDate.parse("2025-04-13"));
        rentalDTO.setBookingTime(LocalTime.parse("10:00"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        RentalDTO result = rentalService.createBooking(rentalDTO);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getCarId());
        assertEquals("PENDING", result.getRentalStatus());
        assertEquals(LocalDate.parse("2025-04-13"), result.getBookingDate());
        assertEquals(LocalTime.parse("10:00"), result.getBookingTime());
        verify(rentalRepository).save(any(Rental.class));
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void testUpdateRentalStatus() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalService.updateRentalStatus(1L, "FINISHED THE RIDE");

        assertEquals("FINISHED THE RIDE", rental.getRentalStatus());
        verify(rentalRepository).save(rental);
    }

    @Test
    void testCancelBooking() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        boolean result = rentalService.cancelBooking(1L);

        assertTrue(result);
        assertEquals("USER CANCELLED", rental.getRentalStatus());
        verify(rentalRepository).save(rental);
    }
}

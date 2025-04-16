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
        user.setEmail("john@example.com");

        vehicle = new Vehicle();
        vehicle.setCarId(1L);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Camry");
        vehicle.setStatus("AVAILABLE");

        Image image = new Image();
        image.setId(1L);
        image.setUrl("http://example.com/image.jpg");
        vehicle.setImages(List.of(image));

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
    }

    @Test
    void testGetRentalsByStatus_All() {
        when(rentalRepository.findAll()).thenReturn(List.of(rental));

        List<RentalResponseDTO> result = rentalService.getRentalsByStatus("All");

        assertEquals(1, result.size());
    }

    @Test
    void testCreateBooking_Success() {
        RentalDTO dto = new RentalDTO();
        dto.setUserId(1L);
        dto.setCarId(1L);
        dto.setRentalPeriod(7);
        dto.setTotalPaymentAmount(2000);
        dto.setBookingDate(LocalDate.now());
        dto.setBookingTime(LocalTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        RentalDTO created = rentalService.createBooking(dto);

        assertNotNull(created);
        assertEquals(1L, created.getUserId());
        verify(vehicleRepository).save(any(Vehicle.class));
        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void testCreateBooking_UserOrCarNotFound() {
        RentalDTO dto = new RentalDTO();
        dto.setUserId(1L);
        dto.setCarId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            rentalService.createBooking(dto);
        });

        assertEquals("User or Car not found", ex.getMessage());
    }

    @Test
    void testGetUserBookings_FilteredStatuses() {
        rental.setRentalStatus("FINISHED THE RIDE");
        when(rentalRepository.findByUserId(1L)).thenReturn(List.of(rental));

        List<UserBookingDTO> result = rentalService.getUserBookings(1L);

        assertEquals(1, result.size());
        assertEquals("FINISHED THE RIDE", result.get(0).getRentalStatus());
    }

    @Test
    void testUpdateRentalStatus_UserCancelled_Valid() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalService.updateRentalStatus(1L, "USER CANCELLED");

        assertEquals("USER CANCELLED", rental.getRentalStatus());
        verify(rentalRepository).save(rental);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void testUpdateRentalStatus_UserCancelled_Invalid() {
        rental.setRentalStatus("ACCEPTED CAR FOR RIDE");
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
                rentalService.updateRentalStatus(1L, "USER CANCELLED"));

        assertEquals("Booking cannot be cancelled as it is already Approved or Ongoing.", ex.getMessage());
    }

    @Test
    void testUpdateRentalStatus_Decline() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        rentalService.updateRentalStatus(1L, "DECLINE CAR FOR RIDE");
        assertEquals("DECLINE CAR FOR RIDE", rental.getRentalStatus());
    }

    @Test
    void testUpdateRentalStatus_Unsupported() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
                rentalService.updateRentalStatus(1L, "INVALID STATUS"));

        assertEquals("Unsupported rental status: INVALID STATUS", ex.getMessage());
    }

    @Test
    void testCancelBooking_Success() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        boolean result = rentalService.cancelBooking(1L);

        assertTrue(result);
        assertEquals("USER CANCELLED", rental.getRentalStatus());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void testCancelBooking_AlreadyCancelled() {
        rental.setRentalStatus("USER CANCELLED");
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        boolean result = rentalService.cancelBooking(1L);

        assertFalse(result);
    }

    @Test
    void testDeleteRental_Success() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        rentalService.deleteRental(1L);

        verify(vehicleRepository).save(vehicle);
        verify(rentalRepository).deleteById(1L);
    }

    @Test
    void testDeleteRental_NotFound() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> 
                rentalService.deleteRental(1L));

        assertEquals("Rental with ID 1 not found", ex.getMessage());
    }

    @Test
    void testGetRentalCountByStatus() {
        when(rentalRepository.countByRentalStatus("PENDING")).thenReturn(5);

        int count = rentalService.getRentalCountByStatus("PENDING");

        assertEquals(5, count);
    }

    // Add test for the getUserBookings() method when no rentals are found
    @Test
    void testGetUserBookings_NoRentals() {
        when(rentalRepository.findByUserId(1L)).thenReturn(List.of());

        List<UserBookingDTO> result = rentalService.getUserBookings(1L);

        assertTrue(result.isEmpty());
    }
}

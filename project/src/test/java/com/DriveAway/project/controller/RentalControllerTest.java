package com.DriveAway.project.controller;

import com.DriveAway.project.dto.RentalDTO;
import com.DriveAway.project.dto.RentalResponseDTO;
import com.DriveAway.project.dto.UserBookingDTO;
import com.DriveAway.project.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RentalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    private RentalDTO rentalDTO;
    private RentalResponseDTO rentalResponseDTO;
    private UserBookingDTO userBookingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();

        // Sample Rental DTO
        rentalDTO = new RentalDTO();
        rentalDTO.setRentalPeriod(3);
        rentalDTO.setRentalStatus("PENDING");
        rentalDTO.setTotalPaymentAmount(9000);

        // Sample Rental Response DTO
        rentalResponseDTO = new RentalResponseDTO();
        rentalResponseDTO.setRentalStatus("PENDING");
        rentalResponseDTO.setTotalPaymentAmount(9000);

        // Sample User Booking DTO
        userBookingDTO = new UserBookingDTO();
        userBookingDTO.setRentalStatus("PENDING");
        userBookingDTO.setTotalPaymentAmount(9000);
    }

    // ✅ Test: Create Booking
    @Test
    @DisplayName("Create Booking - Success")
    void testCreateBooking() throws Exception {
        when(rentalService.createBooking(any(RentalDTO.class))).thenReturn(rentalDTO);

        mockMvc.perform(post("/RentARide/bookings")
                        .contentType("application/json")
                        .content("{\"rentalPeriod\": 3, \"rentalStatus\": \"PENDING\", \"totalPaymentAmount\": 9000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentalStatus").value("PENDING"))
                .andExpect(jsonPath("$.totalPaymentAmount").value(9000));

        verify(rentalService, times(1)).createBooking(any(RentalDTO.class));
    }

    // ✅ Test: Get Rentals by Status
    @Test
    @DisplayName("Get Rentals by Status - Success")
    void testGetRentalsByStatus() throws Exception {
        List<RentalResponseDTO> rentalList = Arrays.asList(rentalResponseDTO);

        when(rentalService.getRentalsByStatus("PENDING")).thenReturn(rentalList);

        mockMvc.perform(get("/RentARide/bookings/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rentalStatus").value("PENDING"))
                .andExpect(jsonPath("$[0].totalPaymentAmount").value(9000));

        verify(rentalService, times(1)).getRentalsByStatus("PENDING");
    }

    // ✅ Test: Get Rental Count by Status
    @Test
    @DisplayName("Get Rental Count by Status - Success")
    void testGetRentalCountByStatus() throws Exception {
        when(rentalService.getRentalCountByStatus("PENDING")).thenReturn(1);

        mockMvc.perform(get("/RentARide/bookings/count/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

        verify(rentalService, times(1)).getRentalCountByStatus("PENDING");
    }

    // ✅ Test: Update Rental Status
    @Test
    @DisplayName("Update Rental Status - Success")
    void testUpdateRentalStatus() throws Exception {
        doNothing().when(rentalService).updateRentalStatus(1L, "CONFIRMED");

        mockMvc.perform(put("/RentARide/bookings/updateStatus/1/CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rental status updated to CONFIRMED"));

        verify(rentalService, times(1)).updateRentalStatus(1L, "CONFIRMED");
    }

    // ✅ Test: Get User Bookings
    @Test
    @DisplayName("Get User Bookings - Success")
    void testGetUserBookings() throws Exception {
        List<UserBookingDTO> bookingList = Arrays.asList(userBookingDTO);

        when(rentalService.getUserBookings(1L)).thenReturn(bookingList);

        mockMvc.perform(get("/RentARide/bookings/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rentalStatus").value("PENDING"))
                .andExpect(jsonPath("$[0].totalPaymentAmount").value(9000));

        verify(rentalService, times(1)).getUserBookings(1L);
    }

    // ✅ Test: Cancel Booking
    @Test
    @DisplayName("Cancel Booking - Success")
    void testCancelBooking() throws Exception {
        when(rentalService.cancelBooking(1L)).thenReturn(true);

        mockMvc.perform(put("/RentARide/bookings/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking cancelled successfully."));

        verify(rentalService, times(1)).cancelBooking(1L);
    }

    // ✅ Test: Delete Rental
    @Test
    @DisplayName("Delete Rental - Success")
    void testDeleteRental() throws Exception {
        doNothing().when(rentalService).deleteRental(1L);

        mockMvc.perform(delete("/RentARide/bookings/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rental with ID 1 has been deleted successfully."));

        verify(rentalService, times(1)).deleteRental(1L);
    }
}

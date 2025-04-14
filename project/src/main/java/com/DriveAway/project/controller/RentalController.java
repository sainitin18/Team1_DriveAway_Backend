package com.DriveAway.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DriveAway.project.dto.RentalDTO;
import com.DriveAway.project.dto.RentalResponseDTO;
import com.DriveAway.project.dto.UserBookingDTO;
import com.DriveAway.project.service.RentalService;

import java.util.List;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/RentARide/bookings")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RentalResponseDTO>> getRentalsByStatus(@PathVariable String status) {
        List<RentalResponseDTO> rentals = rentalService.getRentalsByStatus(status);
        return ResponseEntity.ok(rentals);
    }
    
    @PostMapping
    public RentalDTO createBooking(@RequestBody RentalDTO rentalDTO) {
        return rentalService.createBooking(rentalDTO);
    }
    
    @GetMapping("/count/{status}")
    public ResponseEntity<Integer> getRentalCountByStatus(@PathVariable String status) {
        int count = rentalService.getRentalCountByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseEntity<String> updateRentalStatus(
            @PathVariable Long id,
            @PathVariable String status) {
        rentalService.updateRentalStatus(id, status);
        return ResponseEntity.ok("Rental status updated to " + status.toUpperCase());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserBookingDTO>> getUserBookings(@PathVariable Long userId) {
        List<UserBookingDTO> bookings = rentalService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }
    
    @PutMapping("/cancel/{rentalId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long rentalId) {
        boolean isCancelled = rentalService.cancelBooking(rentalId);
        if (isCancelled) {
            return ResponseEntity.ok("Booking cancelled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Cancellation failed. It may be already cancelled or not allowed.");
        }
    }
    @DeleteMapping("/delete/{rentalId}")
    public ResponseEntity<String> deleteRental(@PathVariable Long rentalId) {
        rentalService.deleteRental(rentalId);
        return ResponseEntity.ok("Rental with ID " + rentalId + " has been deleted successfully.");
    }

}

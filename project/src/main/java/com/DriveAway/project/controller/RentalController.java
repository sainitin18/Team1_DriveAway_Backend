package com.DriveAway.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DriveAway.project.dto.RentalDTO;
import com.DriveAway.project.service.RentalService;

import java.util.List;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/RentARide/bookings")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    /**
     * Get bookings for a user based on rental status.
     * @param userId ID of the user
     * @param status Rental status (e.g., pending, approved)
     * @return List of rental bookings for the user
     * The method getUserBookingsByStatus(Long, String) is undefined for the type RentalService
     * The method getNewBookingsForAdmin() is undefined for the type RentalService
     */
    @GetMapping("/user/{userId}/{status}")
    public List<RentalDTO> getUserBookingsByStatus(@PathVariable Long userId, @PathVariable String status) {
        return rentalService.getUserBookingsByStatus(userId, status);
    }

    /**
     * Get all new bookings for admin.
     * @return List of new rental requests
     */
    @GetMapping("/admin/new")
    public List<RentalDTO> getNewBookingsForAdmin() {
        return rentalService.getNewBookingsForAdmin();
    }

    /**
     * Get all approved bookings for admin.
     * @return List of approved bookings
     */
    @GetMapping("/admin/approved")
    public List<RentalDTO> getApprovedBookingsForAdmin() {
        return rentalService.getApprovedBookingsForAdmin();
    }

    /**
     * Get all ongoing rentals for admin.
     * @return List of ongoing rentals
     */
    @GetMapping("/admin/ongoing")
    public List<RentalDTO> getOngoingRentalsForAdmin() {
        return rentalService.getOngoingRentalsForAdmin();
    }

    /**
     * Get all completed rentals for admin.
     * @return List of completed rentals
     */
    @GetMapping("/admin/completed")
    public List<RentalDTO> getCompletedRentalsForAdmin() {
        return rentalService.getCompletedRentalsForAdmin();
    }

    /**
     * Get rental count of a specific car based on status.
     * @param carId Car ID
     * @param status Rental status
     * @return Count of rentals for the given car and status
     */
    @GetMapping("/rentalcount/{carId}/{status}")
    public int getRentalCountByCarIdAndStatus(@PathVariable Long carId, @PathVariable String status) {
        return rentalService.getRentalCountByCarIdAndStatus(carId, status);
    }

    /**
     * Check if a user has a pending booking for a specific car.
     * @param userId User ID
     * @param carId Car ID
     * @return True if user has a pending booking, otherwise false
     */
    @GetMapping("/pending/{userId}/{carId}")
    public boolean hasPendingBooking(@PathVariable Long userId, @PathVariable Long carId) {
        return rentalService.hasPendingBooking(userId, carId);
    }

    /**
     * Add a new rental booking (User must pay security deposit first).
     * @param rentalDTO Rental details
     * @return Created rental booking
     */
    @PostMapping
    public RentalDTO createBooking(@RequestBody RentalDTO rentalDTO) {
        return rentalService.createBooking(rentalDTO);
    }

    /**
     * Accept a rental request (Only admin can approve).
     * @param id Rental booking ID
     */
    
    @GetMapping("/admin/bookings")
    public ResponseEntity<List<RentalDTO>> getAllBookingsForAdmin() {
        return ResponseEntity.ok(rentalService.getAllBookingsForAdmin());
    }

    
    @PutMapping("/{id}/accept")
    public void acceptBooking(@PathVariable Long id) {
        rentalService.acceptBooking(id);
    }

    /**
     * Decline a rental request (Only admin can decline).
     * @param id Rental booking ID
     */
    @PutMapping("/{id}/decline")
    public void declineBooking(@PathVariable Long id) {
        rentalService.declineBooking(id);
    }

    /**
     * Mark a rental as completed.
     * @param id Rental booking ID
     */
    @PutMapping("/{id}/complete")
    public void completeRental(@PathVariable Long id) {
        rentalService.completeRental(id);
    }

    /**
     * Cancel a rental (User can cancel only if not yet approved).
     * @param id Rental booking ID
     */
    @PutMapping("/{id}/cancel")
    public void cancelBooking(@PathVariable Long id) {
        rentalService.cancelBooking(id);
    }
}

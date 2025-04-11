package com.DriveAway.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DriveAway.project.dto.RentalDTO;
import com.DriveAway.project.dto.RentalResponseDTO;
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
    
    @GetMapping("/count")
    public ResponseEntity<Integer> getRentalCountByCarAndStatus(
            @RequestParam Long carId,
            @RequestParam String status
    ) {
        int count = rentalService.getRentalCountByCarIdAndStatus(carId, status);
        return ResponseEntity.ok(count);
    }

    
    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseEntity<String> updateRentalStatus(
            @PathVariable Long id,
            @PathVariable String status) {
        rentalService.updateRentalStatus(id, status);
        return ResponseEntity.ok("Rental status updated to " + status.toUpperCase());
    }

}

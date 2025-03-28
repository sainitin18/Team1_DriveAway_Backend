package com.DriveAway.project.controller;

import com.DriveAway.project.model.Rental;
import com.DriveAway.project.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping
    public Rental createRental(@RequestBody Rental rental) {
        return rentalService.createRental(rental);
    }

    @GetMapping("/{rentalId}")
    public Rental getRentalById(@PathVariable Long rentalId) {
        return rentalService.getRentalById(rentalId);
    }

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/user/{userId}")
    public List<Rental> getRentalsByUserId(@PathVariable Long userId) {
        return rentalService.getRentalsByUserId(userId);
    }

    @GetMapping("/car/{carId}")
    public List<Rental> getRentalsByCarId(@PathVariable Long carId) {
        return rentalService.getRentalsByCarId(carId);
    }

    @PutMapping("/{rentalId}/status")
    public Rental updateRentalStatus(@PathVariable Long rentalId, @RequestParam String status) {
        return rentalService.updateRentalStatus(rentalId, status);
    }

    @DeleteMapping("/{rentalId}")
    public String deleteRental(@PathVariable Long rentalId) {
        rentalService.deleteRental(rentalId);
        return "Rental with ID " + rentalId + " has been deleted successfully.";
    }
}

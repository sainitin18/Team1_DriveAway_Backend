
package com.DriveAway.project.service;

import com.DriveAway.project.model.Rental;
import java.util.List;

public interface RentalService {
    
   
    Rental createRental(Rental rental);

    Rental getRentalById(Long rentalId);

    List<Rental> getAllRentals();

    List<Rental> getRentalsByUserId(Long userId);

 
    List<Rental> getRentalsByCarId(Long carId);

    Rental updateRentalStatus(Long rentalId, String status);

    void deleteRental(Long rentalId);
}

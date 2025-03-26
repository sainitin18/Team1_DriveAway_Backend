package com.DriveAway.project.service;
import com.DriveAway.project.model.Rental;
import com.DriveAway.project.repository.RentalRepository;
import com.DriveAway.project.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Override
    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Override
    public Rental getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found with ID: " + rentalId));
    }

    @Override
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    public List<Rental> getRentalsByUserId(Long userId) {
        return rentalRepository.findByUserUserId(userId);
    }

    @Override
    public List<Rental> getRentalsByCarId(Long carId) {
        return rentalRepository.findByVehicleCarId(carId);
    }

    @Override
    public Rental updateRentalStatus(Long rentalId, String status) {
        Optional<Rental> rentalOptional = rentalRepository.findById(rentalId);
        if (rentalOptional.isPresent()) {
            Rental rental = rentalOptional.get();
            rental.setRentalStatus(status);
            return rentalRepository.save(rental);
        } else {
            throw new RuntimeException("Rental not found with ID: " + rentalId);
        }
    }

    @Override
    public void deleteRental(Long rentalId) {
        rentalRepository.deleteById(rentalId);
    }
}

package com.DriveAway.project.service;
import com.DriveAway.project.dto.RentalDTO;
import com.DriveAway.project.model.Rental;
import com.DriveAway.project.model.User;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.RentalRepository;
import com.DriveAway.project.repository.UserRepository;
import com.DriveAway.project.repository.VehicleRepository;
import com.DriveAway.project.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public List<RentalDTO> getUserBookingsByStatus(Long userId, String status) {
        return rentalRepository.findByUserAndStatus(userId, status)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> getNewBookingsForAdmin() {
        return rentalRepository.findByRentalStatus("Pending")
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> getApprovedBookingsForAdmin() {
        return rentalRepository.findByRentalStatus("Approved")
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> getOngoingRentalsForAdmin() {
        return rentalRepository.findByRentalStatus("Ongoing")
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RentalDTO> getCompletedRentalsForAdmin() {
        return rentalRepository.findByRentalStatus("Completed")
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public int getRentalCountByCarIdAndStatus(Long carId, String status) {
//        return rentalRepository.countByCarIdAndRentalStatus(carId, status);
    	return 1;
    }

    @Override
    public boolean hasPendingBooking(Long userId, Long carId) {
//        return rentalRepository.existsByUserIdAndCarIdAndRentalStatus(userId, carId, "Pending");
    	return true;
    }

    @Override
    public RentalDTO createBooking(RentalDTO rentalDTO) {
        Optional<User> userOpt = userRepository.findById(rentalDTO.getUserId());
        Optional<Vehicle> carOpt = vehicleRepository.findById(rentalDTO.getCarId());

        if (userOpt.isPresent() && carOpt.isPresent()) {
            Rental rental = new Rental();
            rental.setUser(userOpt.get());
            rental.setCar(carOpt.get());
            rental.setRentalPeriod(rentalDTO.getRentalPeriod());
            rental.setRentalStatus("Pending");
            rental.setBookingDate(rentalDTO.getBookingDate());
            rental.setBookingTime(rentalDTO.getBookingTime());
            rental.setSecurityAmount(rentalDTO.getSecurityAmount());
            rental.setPaymentAmount(rentalDTO.getPaymentAmount());

            Rental savedRental = rentalRepository.save(rental);
            return convertToDTO(savedRental);
        }
        throw new RuntimeException("User or Car not found");
    }

    @Override
    public void acceptBooking(Long id) {
        rentalRepository.findById(id).ifPresent(rental -> {
            rental.setRentalStatus("Approved");
            rentalRepository.save(rental);
        });
    }

    @Override
    public void declineBooking(Long id) {
        rentalRepository.findById(id).ifPresent(rental -> {
            rental.setRentalStatus("Declined");
            rentalRepository.save(rental);
        });
    }

    @Override
    public void completeRental(Long id) {
        rentalRepository.findById(id).ifPresent(rental -> {
            rental.setRentalStatus("Completed");
            rentalRepository.save(rental);
        });
    }

    @Override
    public void cancelBooking(Long id) {
        rentalRepository.findById(id).ifPresent(rental -> {
            if ("Pending".equals(rental.getRentalStatus())) {
                rental.setRentalStatus("Cancelled");
                rentalRepository.save(rental);
            } else {
                throw new RuntimeException("Booking cannot be cancelled as it is already approved or ongoing.");
            }
        });
    }

    private RentalDTO convertToDTO(Rental rental) {
        RentalDTO dto = new RentalDTO();
        dto.setId(rental.getId());
        dto.setUserId(rental.getUser().getUserId());
        dto.setCarId(rental.getCar().getCarId());
        dto.setRentalPeriod(rental.getRentalPeriod());
        dto.setRentalStatus(rental.getRentalStatus());
        dto.setBookingDate(rental.getBookingDate());
        dto.setBookingTime(rental.getBookingTime());
        dto.setCreatedTime(rental.getCreatedTime());
        dto.setExpiryTime(rental.getExpiryTime());
        dto.setSecurityAmount(rental.getSecurityAmount());
        dto.setPaymentAmount(rental.getPaymentAmount());
        return dto;
    }
}



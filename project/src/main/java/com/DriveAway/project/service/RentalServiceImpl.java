package com.DriveAway.project.service;
import com.DriveAway.project.dto.RentalDTO;
import com.DriveAway.project.dto.RentalResponseDTO;
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
    public List<RentalResponseDTO> getRentalsByStatus(String status) {
        List<Rental> rentals;

        if ("All".equalsIgnoreCase(status)) {
            rentals = rentalRepository.findAll();
        } else {
            rentals = rentalRepository.findByRentalStatus(status);
        }

        return rentals.stream()
                .map(this::convertToRentalResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RentalDTO createBooking(RentalDTO rentalDTO) {
        Optional<User> userOpt = userRepository.findById(rentalDTO.getUserId());
        Optional<Vehicle> carOpt = vehicleRepository.findById(rentalDTO.getCarId());

        if (userOpt.isPresent() && carOpt.isPresent()) {
            User user = userOpt.get();
            Vehicle car = carOpt.get();

            // ✅ Update vehicle status
            car.setStatus("NOT AVAILABLE");
            vehicleRepository.save(car); // Save the updated vehicle status

            Rental rental = new Rental();
            rental.setUser(user);
            rental.setCar(car);
            rental.setRentalPeriod(rentalDTO.getRentalPeriod());
            rental.setRentalStatus("Pending");
            rental.setBookingDate(rentalDTO.getBookingDate());
            rental.setBookingTime(rentalDTO.getBookingTime());
            rental.setTotalPaymentAmount(rentalDTO.getTotalPaymentAmount());

            Rental savedRental = rentalRepository.save(rental);
            return convertToDTO(savedRental);
        }
        
        throw new RuntimeException("User or Car not found");
    }
    
    @Override
    public int getRentalCountByCarIdAndStatus(Long carId, String status) {
        return rentalRepository.countByCarAndStatus(carId, status.toUpperCase());
    }


    @Override
    public void updateRentalStatus(Long rentalId, String status) {
//    	System.out.println("From update rental service: "+status);
        rentalRepository.findById(rentalId).ifPresentOrElse(rental -> {
            String upperStatus = status.toUpperCase();
            String currentStatus = rental.getRentalStatus().toUpperCase();
            Vehicle vehicle = rental.getCar();

            switch (upperStatus) {
                case "USER CANCELLED":
                    if (!currentStatus.equals("PENDING")) {
                        throw new RuntimeException("Booking cannot be cancelled as it is already Approved or Ongoing.");
                    }
                    rental.setRentalStatus(upperStatus);
                    rentalRepository.save(rental);

                    vehicle.setStatus("AVAILABLE");
                    vehicleRepository.save(vehicle);
                    break;

                case "DECLINE CAR FOR RIDE":
                	rental.setRentalStatus(upperStatus);
                	rentalRepository.save(rental);
                	break;
                	
                case "FINISHED THE RIDE":
                    rental.setRentalStatus(upperStatus);
                    rentalRepository.save(rental);

                    vehicle.setStatus("AVAILABLE");
                    vehicleRepository.save(vehicle);
                    break;

                case "CAR IS IN RIDE":
                	rental.setRentalStatus(upperStatus);
                	rentalRepository.save(rental);
                	break;
                	
                case "ACCEPTED CAR FOR RIDE":
                	rental.setRentalStatus(upperStatus);
                	rentalRepository.save(rental);
                	break;
                	
                case "PENDING":
                    rental.setRentalStatus(upperStatus);
                    rentalRepository.save(rental);

                    vehicle.setStatus("NOT AVAILABLE");
                    vehicleRepository.save(vehicle);
                    break;

                default:
                    throw new RuntimeException("Unsupported rental status: " + upperStatus);
            }

        }, () -> {
            throw new RuntimeException("Rental with ID " + rentalId + " not found");
        });
    }



    private RentalDTO convertToDTO(Rental rental) {
        RentalDTO dto = new RentalDTO();
        dto.setRentalId(rental.getRentalId());
        dto.setUserId(rental.getUser().getUserId());
        dto.setCarId(rental.getCar().getCarId());
        dto.setRentalPeriod(rental.getRentalPeriod());
        dto.setRentalStatus(rental.getRentalStatus());
        dto.setBookingDate(rental.getBookingDate());
        dto.setBookingTime(rental.getBookingTime());
        dto.setCreatedTime(rental.getCreatedTime());
        dto.setExpiryTime(rental.getExpiryTime());
        dto.setTotalPaymentAmount(rental.getTotalPaymentAmount());
        return dto;
    }
    
    private RentalResponseDTO convertToRentalResponseDTO(Rental rental) {
        RentalResponseDTO dto = new RentalResponseDTO();

        dto.setRentalId(rental.getRentalId());
        dto.setUserName(rental.getUser().getUsername());

        String brand = rental.getCar().getBrand();
        String model = rental.getCar().getModel();
        dto.setCarModel(brand + " " + model);

        dto.setRentalStatus(rental.getRentalStatus());
        dto.setBookingDate(rental.getBookingDate());
        dto.setBookingTime(rental.getBookingTime());
        dto.setNumberOfDays(rental.	getRentalPeriod());
        dto.setSecurityAmount(rental.getCar().getSecurityAmount());
        dto.setCreatedTime(rental.getCreatedTime());
        dto.setExpiryTime(rental.getExpiryTime());
        dto.setTotalPaymentAmount(rental.getTotalPaymentAmount());
        return dto;
    }
}



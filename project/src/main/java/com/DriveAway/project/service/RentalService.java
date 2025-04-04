//package com.DriveAway.project.service;
//
//import com.DriveAway.project.dto.RentalDTO;
//
//import java.util.List;
//
//public interface RentalService {
//    
//    List<RentalDTO> getUserBookingsByStatus(Long userId, String status);
//
//    List<RentalDTO> getNewBookingsForAdmin();
//
//    List<RentalDTO> getApprovedBookingsForAdmin();
//
//    List<RentalDTO> getOngoingRentalsForAdmin();
//
//    List<RentalDTO> getCompletedRentalsForAdmin();
//
//    int getRentalCountByCarIdAndStatus(Long carId, String status);
//
//    boolean hasPendingBooking(Long userId, Long carId);
//
//    RentalDTO createBooking(RentalDTO rentalDTO);
//
//    void acceptBooking(Long id);
//
//    void declineBooking(Long id);
//
//    void completeRental(Long id);
//
//    void cancelBooking(Long id);
//}
package com.DriveAway.project.service;

import com.DriveAway.project.dto.RentalDTO;
import java.util.List;

public interface RentalService {
    List<RentalDTO> getUserBookingsByStatus(Long userId, String status);
    List<RentalDTO> getNewBookingsForAdmin();
    List<RentalDTO> getApprovedBookingsForAdmin();
    List<RentalDTO> getOngoingRentalsForAdmin();
    List<RentalDTO> getCompletedRentalsForAdmin();
    int getRentalCountByCarIdAndStatus(Long carId, String status);
    boolean hasPendingBooking(Long userId, Long carId);
    RentalDTO createBooking(RentalDTO rentalDTO);
    void acceptBooking(Long id);
    void declineBooking(Long id);
    void completeRental(Long id);
    void cancelBooking(Long id);
}
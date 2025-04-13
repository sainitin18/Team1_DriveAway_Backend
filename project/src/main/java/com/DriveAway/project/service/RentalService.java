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
import com.DriveAway.project.dto.RentalResponseDTO;
import com.DriveAway.project.dto.UserBookingDTO;

import java.util.List;

public interface RentalService {
	List<RentalResponseDTO> getRentalsByStatus(String status);
	public int getRentalCountByStatus(String status);
	RentalDTO createBooking(RentalDTO rentalDTO);
    void updateRentalStatus(Long rentalId, String status);
    public List<UserBookingDTO> getUserBookings(Long userId);
    boolean cancelBooking(Long rentalId);
}
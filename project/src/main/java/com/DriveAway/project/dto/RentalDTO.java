//package com.DriveAway.project.dto;
//
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//@Data
//public class RentalDTO {
//    private Long id;
//    private Long userId;
//    private Long carId;
//    private int rentalPeriod;
//    private String rentalStatus;
//    private LocalDate bookingDate;
//    private LocalTime bookingTime;
//    private LocalDateTime createdTime;
//    private LocalDateTime expiryTime;
//    private Integer securityAmount;
//    private Integer paymentAmount;
//}
//
package com.DriveAway.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private Long id;
    private Long userId;
    private Long carId;
    private int rentalPeriod;
    private String rentalStatus;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private LocalDateTime createdTime;
    private LocalDateTime expiryTime;
    private Integer securityAmount;
    private Integer paymentAmount;
}
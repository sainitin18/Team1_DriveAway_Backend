package com.DriveAway.project.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.Data;

@Data
public class UserBookingDTO {
    private Long rentalId;
    private String userName;
    private String carModel;
    private String rentalStatus;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private int numberOfDays;
    private Double securityAmount;
    private LocalDateTime createdTime;
    private LocalDateTime expiryTime;
    private Integer totalPaymentAmount;
    private List<String> imageUrls;
}

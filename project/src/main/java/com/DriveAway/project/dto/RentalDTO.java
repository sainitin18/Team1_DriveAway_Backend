package com.DriveAway.project.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RentalDTO {

    private Long rentalId; // Auto-generated

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Car ID is required")
    private Long carId;

    @NotNull(message = "Rental period is required")
    @Min(value = 1, message = "Rental period must be at least 1 day")
    private Integer rentalPeriod;

    private String rentalStatus = "Available"; // Default status

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    @NotNull(message = "Booking time is required")
    private LocalTime bookingTime;

    private LocalDate createdTime = LocalDate.now(); // Auto-generated

    private LocalDate expiryTime; // Auto-generated in logic

    private BigDecimal paymentAmount; // Calculated in business logic
}

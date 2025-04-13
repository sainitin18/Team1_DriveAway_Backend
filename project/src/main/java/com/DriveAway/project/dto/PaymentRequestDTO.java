package com.DriveAway.project.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long rentalId;
    private Double amount;
    private String currency = "INR"; // Changed default to INR
}
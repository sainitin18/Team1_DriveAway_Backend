package com.DriveAway.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long id;
    private Long rentalId;
    private Double amount;
    private String currency;
    private String paymentStatus;
    private String clientSecret;
    private String paymentIntentId;
    private String paymentMethodId;
} 
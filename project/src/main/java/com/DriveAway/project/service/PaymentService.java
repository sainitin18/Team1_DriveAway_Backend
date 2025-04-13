package com.DriveAway.project.service;

import com.DriveAway.project.dto.PaymentRequestDTO;
import com.DriveAway.project.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO createPaymentIntent(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO confirmPayment(String paymentIntentId);
    PaymentResponseDTO getPaymentByRentalId(Long rentalId);
} 
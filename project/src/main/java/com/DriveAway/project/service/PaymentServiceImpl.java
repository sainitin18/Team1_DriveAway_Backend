package com.DriveAway.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DriveAway.project.dto.PaymentRequestDTO;
import com.DriveAway.project.dto.PaymentResponseDTO;
import com.DriveAway.project.exception.ResourceNotFoundException;
import com.DriveAway.project.model.Payment;
import com.DriveAway.project.model.Rental;
import com.DriveAway.project.repository.PaymentRepository;
import com.DriveAway.project.repository.RentalRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.RefundCreateParams;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Override
    public PaymentResponseDTO createPaymentIntent(PaymentRequestDTO paymentRequestDTO) {
        try {
            // Find the rental
            Rental rental = rentalRepository.findById(paymentRequestDTO.getRentalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + paymentRequestDTO.getRentalId()));

            // Create a payment intent with automatic confirmation
            // Note: For INR, amount should be in paise (1 INR = 100 paise)
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (paymentRequestDTO.getAmount() * 100)) // Convert to paise
                    .setCurrency("INR") // Set currency to INR
                    .addPaymentMethodType("card")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Create a payment record
            Payment payment = new Payment();
            payment.setRental(rental);
            payment.setAmount(paymentRequestDTO.getAmount());
            payment.setCurrency("INR");
            payment.setPaymentStatus("PENDING");
            payment.setPaymentIntentId(paymentIntent.getId());
            
            payment = paymentRepository.save(payment);

            // Create response DTO
            PaymentResponseDTO responseDTO = new PaymentResponseDTO();
            responseDTO.setId(payment.getId());
            responseDTO.setRentalId(rental.getRentalId());
            responseDTO.setAmount(payment.getAmount());
            responseDTO.setCurrency(payment.getCurrency());
            responseDTO.setPaymentStatus(payment.getPaymentStatus());
            responseDTO.setClientSecret(paymentIntent.getClientSecret());
            responseDTO.setPaymentIntentId(paymentIntent.getId());

            return responseDTO;
        } catch (StripeException e) {
            throw new RuntimeException("Error creating payment intent: " + e.getMessage(), e);
        }
    }

    // ... rest of the service implementation remains the same ...

    @Override
    public PaymentResponseDTO confirmPayment(String paymentIntentId) {
        try {
            // Find the payment
            Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found with paymentIntentId: " + paymentIntentId));

            // Retrieve the PaymentIntent from Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            
            // Check if payment is successful
            if ("succeeded".equals(paymentIntent.getStatus())) {
                // Update payment status
                payment.setPaymentStatus("COMPLETED");
                payment = paymentRepository.save(payment);

                // Update rental status to CONFIRMED
                Rental rental = payment.getRental();
                rental.setRentalStatus("CONFIRMED");
                rentalRepository.save(rental);
            }

            // Create response DTO
            PaymentResponseDTO responseDTO = new PaymentResponseDTO();
            responseDTO.setId(payment.getId());
            responseDTO.setRentalId(payment.getRental().getRentalId());
            responseDTO.setAmount(payment.getAmount());
            responseDTO.setCurrency(payment.getCurrency());
            responseDTO.setPaymentStatus(payment.getPaymentStatus());
            responseDTO.setPaymentIntentId(payment.getPaymentIntentId());

            return responseDTO;
        } catch (StripeException e) {
            throw new RuntimeException("Error confirming payment: " + e.getMessage(), e);
        }
    }
    @Override
    public PaymentResponseDTO getPaymentByRentalId(Long rentalId) {
        Payment payment = paymentRepository.findByRentalRentalId(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for rental id: " + rentalId));

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setId(payment.getId());
        responseDTO.setRentalId(payment.getRental().getRentalId());
        responseDTO.setAmount(payment.getAmount());
        responseDTO.setCurrency(payment.getCurrency());
        responseDTO.setPaymentStatus(payment.getPaymentStatus());
        responseDTO.setPaymentIntentId(payment.getPaymentIntentId());

        return responseDTO;
    }
}


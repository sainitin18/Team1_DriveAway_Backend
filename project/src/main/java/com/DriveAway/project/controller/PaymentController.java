package com.DriveAway.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DriveAway.project.dto.PaymentRequestDTO;
import com.DriveAway.project.dto.PaymentResponseDTO;
import com.DriveAway.project.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/RentARide/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Value("${stripe.public.key}")
    private String stripePublicKey;
    
    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getStripeConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("publicKey", stripePublicKey);
        return ResponseEntity.ok(config);
    }
    
    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentResponseDTO> createPaymentIntent(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentResponseDTO paymentResponse = paymentService.createPaymentIntent(paymentRequestDTO);
        return ResponseEntity.ok(paymentResponse);
    }
    
    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<PaymentResponseDTO> confirmPayment(@PathVariable String paymentIntentId) {
        PaymentResponseDTO paymentResponse = paymentService.confirmPayment(paymentIntentId);
        return ResponseEntity.ok(paymentResponse);
    }
    
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByRentalId(@PathVariable Long rentalId) {
        PaymentResponseDTO paymentResponse = paymentService.getPaymentByRentalId(rentalId);
        return ResponseEntity.ok(paymentResponse);
    }
    
} 
package com.DriveAway.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DriveAway.project.model.Payment;
import com.DriveAway.project.model.Rental;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
    Optional<Payment> findByRental(Rental rental);
    Optional<Payment> findByRentalRentalId(Long rentalId);
} 
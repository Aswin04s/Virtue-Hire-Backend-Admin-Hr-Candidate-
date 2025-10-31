package com.virtuehire.repository;

import com.virtuehire.model.Payment;
import com.virtuehire.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByHrId(Long hrId);
    List<Payment> findByHrIdAndStatus(Long hrId, PaymentStatus status);
    Optional<Payment> findByPaymentGatewayId(String paymentGatewayId);
    List<Payment> findByStatus(PaymentStatus status);

    // For admin dashboard
    long countByStatus(PaymentStatus status);
}
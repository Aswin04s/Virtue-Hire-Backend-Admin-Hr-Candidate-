package com.virtuehire.service;

import com.virtuehire.model.*;
import com.virtuehire.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final HrService hrService;

    // Mock payment gateway simulation
    private final Map<String, Payment> pendingPayments = new ConcurrentHashMap<>();
    private final Random random = new Random();

    // Payment configuration from properties
    @Value("${payment.success.rate:0.7}")
    private double paymentSuccessRate;

    @Value("${payment.currency:INR}")
    private String defaultCurrency;

    @Value("${plan.price.monthly.unlimited:2999.00}")
    private Double monthlyUnlimitedPrice;

    @Value("${plan.price.ten.candidates:1999.00}")
    private Double tenCandidatesPrice;

    @Value("${plan.price.single.candidate:299.00}")
    private Double singleCandidatePrice;

    public PaymentService(PaymentRepository paymentRepository, HrService hrService) {
        this.paymentRepository = paymentRepository;
        this.hrService = hrService;
    }

    /**
     * Get plan price by plan type
     */
    public Double getPlanPrice(String planType) {
        switch (planType) {
            case "MONTHLY_UNLIMITED":
                return monthlyUnlimitedPrice;
            case "TEN_CANDIDATES":
                return tenCandidatesPrice;
            case "SINGLE_CANDIDATE":
                return singleCandidatePrice;
            default:
                return 0.0;
        }
    }

    /**
     * Initiate a plan payment
     */
    public Payment initiatePlanPayment(Hr hr, String planType) {
        Double amount = getPlanPrice(planType);
        String description = getPlanDescription(planType);

        Payment payment = new Payment();
        payment.setHr(hr);
        payment.setAmount(amount);
        payment.setCurrency(defaultCurrency);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPlanType(planType);
        payment.setDescription(description);
        payment.setPaymentMethod("MOCK");

        // Generate mock payment gateway ID
        String gatewayId = "TXN_" + System.currentTimeMillis() + "_" + random.nextInt(10000);
        payment.setPaymentGatewayId(gatewayId);

        // Save payment
        payment = paymentRepository.save(payment);

        // Store in pending payments
        pendingPayments.put(gatewayId, payment);

        return payment;
    }

    /**
     * Process plan payment (mock simulation)
     */
    public Payment processPlanPayment(String paymentGatewayId) {
        Payment payment = pendingPayments.get(paymentGatewayId);

        if (payment == null) {
            payment = paymentRepository.findByPaymentGatewayId(paymentGatewayId).orElse(null);
        }

        if (payment == null || payment.getStatus() != PaymentStatus.PENDING) {
            return payment;
        }

        // Mock payment processing (70% success rate by default)
        boolean success = random.nextDouble() < paymentSuccessRate;

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setCompletedAt(LocalDateTime.now());

            // Update HR plan based on payment type
            Hr hr = payment.getHr();
            String planType = payment.getPlanType();

            switch (planType) {
                case "MONTHLY_UNLIMITED":
                    hr.setPlanType("MONTHLY_UNLIMITED");
                    hr.setPlanExpiryDate(LocalDateTime.now().plusDays(30));
                    hr.setRemainingViews(null); // unlimited
                    break;

                case "TEN_CANDIDATES":
                    hr.setPlanType("TEN_CANDIDATES");
                    hr.setPlanExpiryDate(null); // no expiry
                    // Add 10 views to existing views (or set to 10 if null)
                    int currentViews = hr.getRemainingViews() != null ? hr.getRemainingViews() : 0;
                    hr.setRemainingViews(currentViews + 10);
                    break;

                case "SINGLE_CANDIDATE":
                    hr.setPlanType("SINGLE_CANDIDATE");
                    hr.setPlanExpiryDate(null); // no expiry
                    // Add 1 view to existing views (or set to 1 if null)
                    int singleViews = hr.getRemainingViews() != null ? hr.getRemainingViews() : 0;
                    hr.setRemainingViews(singleViews + 1);
                    break;
            }

            hrService.save(hr);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setCompletedAt(LocalDateTime.now());
        }

        payment = paymentRepository.save(payment);
        pendingPayments.remove(paymentGatewayId);

        return payment;
    }

    /**
     * Get payment by gateway ID
     */
    public Optional<Payment> getPaymentByGatewayId(String gatewayId) {
        return paymentRepository.findByPaymentGatewayId(gatewayId);
    }

    /**
     * Get all payments for an HR
     */
    public List<Payment> getPaymentsByHr(Long hrId) {
        return paymentRepository.findByHrId(hrId);
    }

    /**
     * Get plan description
     */
    private String getPlanDescription(String planType) {
        switch (planType) {
            case "MONTHLY_UNLIMITED":
                return "Monthly Unlimited - Unlimited candidate views for 30 days";
            case "TEN_CANDIDATES":
                return "10 Candidates Plan - View 10 candidate profiles (no expiry)";
            case "SINGLE_CANDIDATE":
                return "Single Candidate Plan - View 1 candidate profile";
            default:
                return "Unknown Plan";
        }
    }

    /**
     * Get payment statistics (for admin dashboard)
     */
    public Map<String, Object> getPaymentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPayments", paymentRepository.count());
        stats.put("successfulPayments", paymentRepository.countByStatus(PaymentStatus.SUCCESS));
        stats.put("pendingPayments", paymentRepository.countByStatus(PaymentStatus.PENDING));
        stats.put("failedPayments", paymentRepository.countByStatus(PaymentStatus.FAILED));
        return stats;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }
}
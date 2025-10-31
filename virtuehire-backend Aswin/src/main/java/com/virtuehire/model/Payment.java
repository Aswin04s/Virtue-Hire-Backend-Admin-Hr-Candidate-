// Payment.java
package com.virtuehire.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hr_id", nullable = false)
    private Hr hr;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    private Double amount;
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String paymentGatewayId; // Mock transaction ID
    private String paymentMethod = "MOCK"; // MOCK, CARD, UPI, etc.

    @Column(length = 1000)
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime completedAt;

    // Plan type for subscription
    private String planType; // BASIC, PREMIUM, ENTERPRISE

    // Constructors
    public Payment() {}

    public Payment(Hr hr, Double amount, String description, String planType) {
        this.hr = hr;
        this.amount = amount;
        this.description = description;
        this.planType = planType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Hr getHr() { return hr; }
    public void setHr(Hr hr) { this.hr = hr; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public String getPaymentGatewayId() { return paymentGatewayId; }
    public void setPaymentGatewayId(String paymentGatewayId) { this.paymentGatewayId = paymentGatewayId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }
}
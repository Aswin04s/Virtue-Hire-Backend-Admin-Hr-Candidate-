package com.virtuehire.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Hr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;

    @Transient
    private String confirmPassword;

    private String companyName;
    private String jobTitle;
    private String companyWebsite;
    private String industry;
    private String city;
    private String state;
    private String idProofPath; // store file path in uploads/

    // ===== UPDATED PLAN FIELDS =====
    // Plan types: MONTHLY_UNLIMITED, TEN_CANDIDATES, SINGLE_CANDIDATE, or null (no plan)
    private String planType = null;

    // For MONTHLY_UNLIMITED: tracks expiry date (30 days from purchase)
    // For other plans: null (no expiry)
    private LocalDateTime planExpiryDate;

    // For TEN_CANDIDATES and SINGLE_CANDIDATE: tracks remaining views
    // For MONTHLY_UNLIMITED: null or -1 (unlimited)
    // When remainingViews reaches 0, planType is set to null
    private Integer remainingViews = 0;

    private Boolean verified = false;

    // Constructors
    public Hr() {
        this.verified = false;
    }

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdProofPath() {
        return idProofPath;
    }

    public void setIdProofPath(String idProofPath) {
        this.idProofPath = idProofPath;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public LocalDateTime getPlanExpiryDate() {
        return planExpiryDate;
    }

    public void setPlanExpiryDate(LocalDateTime planExpiryDate) {
        this.planExpiryDate = planExpiryDate;
    }

    public Integer getRemainingViews() {
        return remainingViews;
    }

    public void setRemainingViews(Integer remainingViews) {
        this.remainingViews = remainingViews;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
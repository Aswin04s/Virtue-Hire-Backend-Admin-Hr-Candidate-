package com.virtuehire.service;

import com.virtuehire.model.Hr;
import com.virtuehire.repository.HrRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HrService {

    private final HrRepository repo;

    public HrService(HrRepository repo) {
        this.repo = repo;
    }

    public Hr save(Hr hr) {
        return repo.save(hr);
    }

    public List<Hr> findAll() {
        return repo.findAll();
    }

    public Optional<Hr> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<Hr> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public Hr login(String email, String password) {
        Optional<Hr> optionalHr = repo.findByEmail(email);
        if (optionalHr.isPresent()) {
            Hr hr = optionalHr.get();
            if (hr.getPassword().equals(password)) {
                return hr;
            }
        }
        return null;
    }

    /**
     * Check if HR can view candidate details based on their plan
     */
    public boolean canViewCandidateDetails(Hr hr) {
        if (hr == null || !hr.getVerified()) {
            return false;
        }

        // Check if HR has MONTHLY_UNLIMITED plan that's still active
        if ("MONTHLY_UNLIMITED".equals(hr.getPlanType())) {
            if (hr.getPlanExpiryDate() != null && hr.getPlanExpiryDate().isAfter(LocalDateTime.now())) {
                return true;
            } else {
                // Plan expired, reset plan type
                hr.setPlanType(null);
                hr.setPlanExpiryDate(null);
                repo.save(hr);
                return false;
            }
        }

        // Check if HR has remaining views (TEN_CANDIDATES or SINGLE_CANDIDATE)
        if (hr.getRemainingViews() != null && hr.getRemainingViews() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Consume one view from HR's plan
     */
    public void consumeView(Hr hr) {
        if (hr == null) {
            return;
        }

        // If MONTHLY_UNLIMITED, no need to decrement (unlimited views)
        if ("MONTHLY_UNLIMITED".equals(hr.getPlanType())) {
            return;
        }

        // For TEN_CANDIDATES or SINGLE_CANDIDATE, decrement remaining views
        if (hr.getRemainingViews() != null && hr.getRemainingViews() > 0) {
            hr.setRemainingViews(hr.getRemainingViews() - 1);

            // If no views remaining, reset plan type
            if (hr.getRemainingViews() == 0) {
                hr.setPlanType(null);
            }

            repo.save(hr);
        }
    }

    /**
     * Get a user-friendly plan display name
     */
    public String getPlanDisplayName(Hr hr) {
        if (hr == null || hr.getPlanType() == null) {
            return "No Active Plan";
        }

        switch (hr.getPlanType()) {
            case "MONTHLY_UNLIMITED":
                if (hr.getPlanExpiryDate() != null && hr.getPlanExpiryDate().isAfter(LocalDateTime.now())) {
                    return "Monthly Unlimited (Expires: " + hr.getPlanExpiryDate().toLocalDate() + ")";
                } else {
                    return "No Active Plan (Expired)";
                }
            case "TEN_CANDIDATES":
                int views = hr.getRemainingViews() != null ? hr.getRemainingViews() : 0;
                return "10 Candidates Plan (" + views + " views remaining)";
            case "SINGLE_CANDIDATE":
                int singleViews = hr.getRemainingViews() != null ? hr.getRemainingViews() : 0;
                return "Single Candidate Plan (" + singleViews + " view remaining)";
            default:
                return "Unknown Plan";
        }
    }

    /**
     * Get verification status message for HR
     */
    public String getVerificationStatusMessage(Hr hr) {
        if (hr == null) {
            return "HR not found";
        }

        if (hr.getVerified()) {
            return "✓ Account Verified - You can access all features";
        } else {
            return "⏳ Waiting for Admin Approval - Please wait while we verify your account";
        }
    }
}
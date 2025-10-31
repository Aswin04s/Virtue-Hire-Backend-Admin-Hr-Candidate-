package com.virtuehire.controller;

import com.virtuehire.model.Hr;
import com.virtuehire.model.Payment;
import com.virtuehire.model.PaymentStatus;
import com.virtuehire.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ===== UPDATED: Show payment plans page =====
    @GetMapping("/plans")
    public String showPlans(HttpSession session, Model model) {
        Hr hr = (Hr) session.getAttribute("hr");
        if (hr == null) {
            return "redirect:/hrs/login";
        }

        // Get prices from service
        Double monthlyPrice = paymentService.getPlanPrice("MONTHLY_UNLIMITED");
        Double tenPrice = paymentService.getPlanPrice("TEN_CANDIDATES");
        Double singlePrice = paymentService.getPlanPrice("SINGLE_CANDIDATE");

        model.addAttribute("hr", hr);
        model.addAttribute("monthlyPrice", monthlyPrice);
        model.addAttribute("tenPrice", tenPrice);
        model.addAttribute("singlePrice", singlePrice);

        return "payment-plans";
    }

    // ===== UPDATED: Process payment =====
    @PostMapping("/process-payment")
    public String processPayment(@RequestParam String planType,
                                 HttpSession session,
                                 Model model) {
        Hr hr = (Hr) session.getAttribute("hr");
        if (hr == null) {
            return "redirect:/hrs/login";
        }

        // Initiate payment
        Payment payment = paymentService.initiatePlanPayment(hr, planType);

        // Process payment (mock gateway)
        payment = paymentService.processPlanPayment(payment.getPaymentGatewayId());

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            // Check if there's a pending candidate view
            Long candidateId = (Long) session.getAttribute("pendingCandidateId");
            session.removeAttribute("pendingCandidateId");

            // Refresh HR in session
            hr = payment.getHr();
            session.setAttribute("hr", hr);

            if (candidateId != null) {
                // Redirect back to the candidate they wanted to view
                return "redirect:/hrs/viewCandidateDetails/" + candidateId;
            }

            model.addAttribute("message", "Payment successful! Your plan has been activated.");
            return "redirect:/hrs/dashboard";
        } else {
            model.addAttribute("error", "Payment failed. Please try again.");
            return "redirect:/payments/plans";
        }
    }

    // View payment history
    @GetMapping("/history")
    public String paymentHistory(HttpSession session, Model model) {
        Hr hr = (Hr) session.getAttribute("hr");
        if (hr == null) {
            return "redirect:/hrs/login";
        }

        List<Payment> payments = paymentService.getPaymentsByHr(hr.getId());
        model.addAttribute("payments", payments);
        return "payment-history";
    }

    // Mock payment status check
    @GetMapping("/status/{gatewayId}")
    @ResponseBody
    public Map<String, Object> checkPaymentStatus(@PathVariable String gatewayId) {
        Optional<Payment> paymentOpt = paymentService.getPaymentByGatewayId(gatewayId);
        Map<String, Object> response = new HashMap<>();

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            response.put("status", payment.getStatus().toString());
            response.put("amount", payment.getAmount());
            response.put("planType", payment.getPlanType());
        } else {
            response.put("status", "NOT_FOUND");
        }

        return response;
    }
}

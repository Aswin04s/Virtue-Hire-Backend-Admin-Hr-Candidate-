package com.virtuehire.controller;

import com.virtuehire.model.*;
import com.virtuehire.service.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final HrService hrService;
    private final PaymentService paymentService;
    private final CandidateService candidateService;
    private final QuestionService questionService;
    private final AssessmentResultService assessmentResultService;

    public AdminController(HrService hrService, PaymentService paymentService,
                           CandidateService candidateService, QuestionService questionService,
                           AssessmentResultService assessmentResultService) {
        this.hrService = hrService;
        this.paymentService = paymentService;
        this.candidateService = candidateService;
        this.questionService = questionService;
        this.assessmentResultService = assessmentResultService;
    }

    // Enhanced Admin dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Hr> allHrs = hrService.findAll();
        List<Candidate> allCandidates = candidateService.findAll();
        List<Payment> allPayments = paymentService.getAllPayments();

        // HR Statistics
        long totalHrs = allHrs.size();
        long verifiedHrs = allHrs.stream().filter(Hr::getVerified).count();
        long unverifiedHrs = totalHrs - verifiedHrs;

        // Candidate Statistics
        long totalCandidates = allCandidates.size();
        long candidatesWithTest = allCandidates.stream()
                .filter(c -> c.getAssessmentTaken() != null && c.getAssessmentTaken())
                .count();

        // Payment Statistics
        Map<String, Object> paymentStats = paymentService.getPaymentStatistics();

        // FIXED: Correct total revenue calculation
        double totalRevenue = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .mapToDouble(Payment::getAmount)
                .sum();

        // HRs who collected candidate details
        List<Payment> successfulPayments = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .toList();

        model.addAttribute("hrs", allHrs);
        model.addAttribute("candidates", allCandidates);
        model.addAttribute("payments", successfulPayments);
        model.addAttribute("totalHrs", totalHrs);
        model.addAttribute("verifiedHrs", verifiedHrs);
        model.addAttribute("unverifiedHrs", unverifiedHrs);
        model.addAttribute("totalCandidates", totalCandidates);
        model.addAttribute("candidatesWithTest", candidatesWithTest);
        model.addAttribute("paymentStats", paymentStats);
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin-dashboard";
    }

    // Show all HRs for verification
    @GetMapping("/hrs")
    public String showAllHrs(
            @RequestParam(required = false, defaultValue = "all") String filter,
            Model model) {

        List<Hr> hrs;

        if ("verified".equals(filter)) {
            hrs = hrService.findAll().stream()
                    .filter(Hr::getVerified)
                    .toList();
        } else if ("unverified".equals(filter)) {
            hrs = hrService.findAll().stream()
                    .filter(hr -> !hr.getVerified())
                    .toList();
        } else {
            hrs = hrService.findAll();
        }

        model.addAttribute("hrs", hrs);
        model.addAttribute("currentFilter", filter);
        return "hr-list";
    }

    // Verify HR
    @PostMapping("/verify/{id}")
    public String verifyHr(@PathVariable Long id, Model model) {
        Hr hr = hrService.findById(id).orElse(null);
        if (hr != null) {
            hr.setVerified(true);
            hrService.save(hr);
            model.addAttribute("message", "HR verified successfully!");
        } else {
            model.addAttribute("error", "HR not found!");
        }
        return "redirect:/admin/hrs";
    }

    // Unverify HR with reason
    @PostMapping("/unverify/{id}")
    public String unverifyHr(@PathVariable Long id,
                             @RequestParam(required = false, defaultValue = "Invalid ID proof or details") String reason,
                             Model model) {
        Hr hr = hrService.findById(id).orElse(null);
        if (hr != null) {
            hr.setVerified(false);
            hrService.save(hr);
            model.addAttribute("message", "HR unverified! Reason: " + reason);
        } else {
            model.addAttribute("error", "HR not found!");
        }
        return "redirect:/admin/hrs";
    }

    // Question Management
    @GetMapping("/questions")
    public String questionManagement(Model model) {
        // FIXED: Use existing method instead of getAllQuestions()
        List<Question> questions = questionService.getAllQuestionsFromRepository();
        model.addAttribute("questions", questions);
        model.addAttribute("newQuestion", new Question());
        return "question-management";
    }

    // Add new question
    @PostMapping("/questions/add")
    public String addQuestion(@ModelAttribute Question question,
                              @RequestParam List<String> options,
                              Model model) {
        question.setOptions(options);
        // FIXED: Use repository directly for saving
        questionService.saveQuestionViaRepository(question);
        model.addAttribute("message", "Question added successfully!");
        return "redirect:/admin/questions";
    }

    // Edit question form
    @GetMapping("/questions/edit/{id}")
    public String editQuestionForm(@PathVariable Long id, Model model) {
        // FIXED: Use repository method
        Question question = questionService.getQuestionByIdFromRepository(id);
        if (question != null) {
            model.addAttribute("question", question);
            return "question-edit";
        }
        model.addAttribute("error", "Question not found!");
        return "redirect:/admin/questions";
    }

    // Update question
    @PostMapping("/questions/update/{id}")
    public String updateQuestion(@PathVariable Long id,
                                 @ModelAttribute Question question,
                                 @RequestParam List<String> options,
                                 Model model) {
        // FIXED: Set ID using the path variable
        Question existingQuestion = questionService.getQuestionByIdFromRepository(id);
        if (existingQuestion != null) {
            existingQuestion.setLevel(question.getLevel());
            existingQuestion.setText(question.getText());
            existingQuestion.setOptions(options);
            existingQuestion.setCorrectAnswer(question.getCorrectAnswer());
            questionService.saveQuestionViaRepository(existingQuestion);
            model.addAttribute("message", "Question updated successfully!");
        } else {
            model.addAttribute("error", "Question not found!");
        }
        return "redirect:/admin/questions";
    }

    // Delete question
    @PostMapping("/questions/delete/{id}")
    public String deleteQuestion(@PathVariable Long id, Model model) {
        // FIXED: Use repository method
        questionService.deleteQuestionViaRepository(id);
        model.addAttribute("message", "Question deleted successfully!");
        return "redirect:/admin/questions";
    }

    // View candidate details
    @GetMapping("/candidates/{id}")
    public String viewCandidateDetails(@PathVariable Long id, Model model) {
        Candidate candidate = candidateService.findById(id).orElse(null);
        if (candidate != null) {
            List<AssessmentResult> results = assessmentResultService.getCandidateResults(id);
            model.addAttribute("candidate", candidate);
            model.addAttribute("results", results);
            return "admin-candidate-details";
        }
        model.addAttribute("error", "Candidate not found!");
        return "redirect:/admin/dashboard";
    }

    // Show all candidates for admin
    @GetMapping("/candidates")
    public String showAllCandidates(Model model) {
        List<Candidate> candidates = candidateService.findAll();
        model.addAttribute("candidates", candidates);
        return "admin-candidates-list";
    }

    // View HR details
    @GetMapping("/hrs/{id}")
    public String viewHrDetails(@PathVariable Long id, Model model) {
        Hr hr = hrService.findById(id).orElse(null);
        if (hr != null) {
            List<Payment> payments = paymentService.getPaymentsByHr(id);
            model.addAttribute("hr", hr);
            model.addAttribute("payments", payments);
            return "admin-hr-details";
        }
        model.addAttribute("error", "HR not found!");
        return "redirect:/admin/dashboard";
    }

    // Add this method to AdminController.java for resume downloads
    @GetMapping("/download/resume/{candidateId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long candidateId) {
        Candidate candidate = candidateService.findById(candidateId).orElse(null);
        if (candidate == null || candidate.getResumePath() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Use the correct upload directory
            Path uploadDir = Paths.get("C:/Users/aswin/Desktop/Virtue Hire/virtuehire-payment/virtuehire-backend Aswin/uploads");
            Path filePath = uploadDir.resolve(candidate.getResumePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // Debug information
                System.out.println("File not found at: " + filePath.toString());
                System.out.println("File exists: " + Files.exists(filePath));
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    //show all payments
    @GetMapping("/payments")
    public String showAllPayments(Model model) {
        List<Payment> allPayments = paymentService.getAllPayments();

        // Calculate statistics
        double totalRevenue = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .mapToDouble(Payment::getAmount)
                .sum();

        long successfulPayments = allPayments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                .count();

        model.addAttribute("payments", allPayments);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("successfulPayments", successfulPayments);
        model.addAttribute("totalPayments", allPayments.size());

        return "admin-payments-list";
    }

    // View payment details
    @GetMapping("/payments/{id}")
    public String viewPaymentDetails(@PathVariable Long id, Model model) {
        Payment payment = paymentService.getPaymentById(id);
        if (payment != null) {
            model.addAttribute("payment", payment);
            return "admin-payment-details";
        }
        model.addAttribute("error", "Payment not found!");
        return "redirect:/admin/payments";
    }
}
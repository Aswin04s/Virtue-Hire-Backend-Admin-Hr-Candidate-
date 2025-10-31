package com.virtuehire.controller;

import com.virtuehire.model.Candidate;
import com.virtuehire.model.Hr;
import com.virtuehire.service.CandidateService;
import com.virtuehire.service.HrService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/hrs")
public class HrController {

    private final HrService hrService;
    private final CandidateService candidateService;

    public HrController(HrService hrService, CandidateService candidateService) {
        this.hrService = hrService;
        this.candidateService = candidateService;
    }

    // Show HR registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("hr", new Hr());
        return "hr-form";
    }

    // Process HR registration
    @PostMapping("/register")
    public String registerHr(@ModelAttribute Hr hr,
                             @RequestParam(value = "idProof", required = false) MultipartFile idProofFile,
                             Model model) {

        // Validate password match
        if (!hr.getPassword().equals(hr.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "hr-form";
        }

        // Check if email already exists
        if (hrService.findByEmail(hr.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered!");
            return "hr-form";
        }

        // Handle ID proof upload - make it required but handle the validation manually
        if (idProofFile == null || idProofFile.isEmpty()) {
            model.addAttribute("error", "ID proof is required for verification!");
            return "hr-form";
        }

        try {
            String uploadsDir = "C:/Users/aswin/Desktop/Virtue Hire/virtuehire-payment/virtuehire-backend Aswin/uploads/";
            File directory = new File(uploadsDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + idProofFile.getOriginalFilename();
            Path path = Paths.get(uploadsDir + fileName);
            Files.write(path, idProofFile.getBytes());
            hr.setIdProofPath(fileName); // Store just the filename, not full path
        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload ID proof: " + e.getMessage());
            return "hr-form";
        }

        hrService.save(hr);
        model.addAttribute("message", "Registration successful! Wait for admin verification.");
        return "hr-login";
    }

    // Show HR login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "hr-login";
    }

    // Process HR login
    @PostMapping("/login")
    public String loginHr(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        Hr hr = hrService.login(email, password);

        if (hr == null) {
            model.addAttribute("error", "Invalid email or password!");
            return "hr-login";
        }

        // Check verification status
        if (!hr.getVerified()) {
            model.addAttribute("error", "Your account is not verified yet! Please wait for admin approval.");
            return "hr-login";
        }

        session.setAttribute("hr", hr);
        return "redirect:/hrs/dashboard";
    }

    // HR Dashboard
    @GetMapping("/dashboard")
    public String hrDashboard(HttpSession session, Model model) {
        Hr hr = (Hr) session.getAttribute("hr");
        if (hr == null) {
            return "redirect:/hrs/login";
        }

        // Refresh HR data from database
        hr = hrService.findById(hr.getId()).orElse(null);
        session.setAttribute("hr", hr);

        model.addAttribute("hr", hr);
        model.addAttribute("planDisplay", hrService.getPlanDisplayName(hr));
        return "hr-dashboard";
    }

    // Browse all candidates
    @GetMapping("/candidates")
    public String browseCandidates(HttpSession session, Model model) {
        Hr hr = (Hr) session.getAttribute("hr");
        if (hr == null) {
            return "redirect:/hrs/login";
        }

        List<Candidate> candidates = candidateService.findAll();
        model.addAttribute("candidates", candidates);
        model.addAttribute("hr", hr);
        return "candidate-list";
    }

    // View Candidate Details (redirects to payment if no plan)
    @GetMapping("/viewCandidateDetails/{candidateId}")
    public String viewCandidateDetails(@PathVariable Long candidateId,
                                       HttpSession session,
                                       Model model) {
        Hr hr = (Hr) session.getAttribute("hr");
        if (hr == null) {
            return "redirect:/hrs/login";
        }

        // Refresh HR from database to get latest plan info
        hr = hrService.findById(hr.getId()).orElse(null);
        if (hr == null) {
            return "redirect:/hrs/login";
        }

        // Check if HR can view candidate details
        if (!hrService.canViewCandidateDetails(hr)) {
            // Store candidate ID in session for post-payment redirect
            session.setAttribute("pendingCandidateId", candidateId);
            model.addAttribute("message", "You need an active plan to view candidate details. Please purchase a plan.");
            return "redirect:/payments/plans";
        }

        // Consume one view (if applicable)
        hrService.consumeView(hr);

        // Refresh session
        hr = hrService.findById(hr.getId()).orElse(null);
        session.setAttribute("hr", hr);

        // Load full candidate details
        Candidate candidate = candidateService.findById(candidateId).orElse(null);
        if (candidate == null) {
            model.addAttribute("error", "Candidate not found!");
            return "redirect:/hrs/candidates";
        }

        model.addAttribute("candidate", candidate);
        model.addAttribute("canView", true);
        return "candidate-profile-full";
    }

    // Download resume
    @GetMapping("/download/resume/{candidateId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long candidateId, HttpSession session) {
        Hr hr = (Hr) session.getAttribute("hr");
        if (hr == null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if HR has access
        if (!hrService.canViewCandidateDetails(hr)) {
            return ResponseEntity.badRequest().build();
        }

        Candidate candidate = candidateService.findById(candidateId).orElse(null);
        if (candidate == null || candidate.getResumePath() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path filePath = Paths.get(candidate.getResumePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Consume one view for resume download
                hrService.consumeView(hr);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Search/filter candidates
    @GetMapping("/searchCandidates")
    public String searchCandidates(
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) Integer minScore,
            HttpSession session,
            Model model) {

        Hr hr = (Hr) session.getAttribute("hr");
        if (hr == null) {
            return "redirect:/hrs/login";
        }

        // Get filtered candidates
        List<Candidate> candidates = candidateService.searchCandidates(skills, experienceLevel, minScore);

        model.addAttribute("candidates", candidates);
        model.addAttribute("hr", hr);

        // Preserve search params for form
        model.addAttribute("searchSkills", skills);
        model.addAttribute("experienceLevel", experienceLevel);
        model.addAttribute("minScore", minScore);

        return "candidate-list";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/hrs/login";
    }

    @GetMapping("/file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            String uploadsDir = "C:/Users/aswin/Desktop/Virtue Hire/virtuehire-payment/virtuehire-backend Aswin/uploads/";
            Path filePath = Paths.get(uploadsDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Determine content type
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType)) // FIXED: Using MediaType class
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
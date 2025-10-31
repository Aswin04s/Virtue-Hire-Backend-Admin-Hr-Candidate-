package com.virtuehire.controller;

import com.virtuehire.model.AssessmentResult;
import com.virtuehire.model.Candidate;
import com.virtuehire.service.AssessmentResultService;
import com.virtuehire.service.CandidateService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;
    private final AssessmentResultService assessmentResultService;

    private final Path uploadDir = Paths.get("C:/Users/aswin/Desktop/VirtueHire-backend Aswin/uploads");

    public CandidateController(CandidateService candidateService,
                               AssessmentResultService assessmentResultService) {
        this.candidateService = candidateService;
        this.assessmentResultService = assessmentResultService;
    }

    // Show registration form
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        return "candidate-form";
    }

    // Handle registration + resume + profile picture upload
    @PostMapping("/register")
    public String register(@ModelAttribute Candidate candidate,
                           @RequestParam("resumeFile") MultipartFile resumeFile,
                           @RequestParam("profilePicFile") MultipartFile profilePicFile,
                           Model model) throws IOException {

        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

        // Save resume
        if (!resumeFile.isEmpty()) {
            String resumeFileName = System.currentTimeMillis() + "_" + resumeFile.getOriginalFilename();
            Path resumePath = uploadDir.resolve(resumeFileName);
            resumeFile.transferTo(resumePath.toFile());
            candidate.setResumePath(resumeFileName);
        }

        // Save profile picture
        if (!profilePicFile.isEmpty()) {
            String profileFileName = System.currentTimeMillis() + "_" + profilePicFile.getOriginalFilename();
            Path profilePath = uploadDir.resolve(profileFileName);
            profilePicFile.transferTo(profilePath.toFile());
            candidate.setProfilePic(profileFileName);
        }

        candidateService.save(candidate);

        model.addAttribute("message", "Candidate registered successfully!");
        model.addAttribute("candidate", new Candidate()); // reset form
        return "candidate-form";
    }

    // Serve uploaded files (resume or profile pic)
    @GetMapping("/file/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Path path = uploadDir.resolve(filename).normalize();
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();

        String contentType = Files.probeContentType(path);
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    // Login page
    @GetMapping("/login")
    public String loginPage() {
        return "candidate-login";
    }

    // Handle login
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Candidate candidate = candidateService.login(email, password);
        if (candidate != null) {
            // Store candidate in session
            session.setAttribute("candidate", candidate);

            model.addAttribute("name", candidate.getFullName());
            model.addAttribute("badge", candidate.getBadge());
            model.addAttribute("profilePic", candidate.getProfilePic());

            // Load assessment results
            List<AssessmentResult> results = assessmentResultService.getCandidateResults(candidate.getId());
            model.addAttribute("results", results);

            // Level-wise marks
            model.addAttribute("levelMarks", results.stream()
                    .collect(java.util.stream.Collectors.toMap(AssessmentResult::getLevel, AssessmentResult::getScore)));

            // Attempted levels
            List<Integer> attemptedLevels = results.stream()
                    .map(AssessmentResult::getLevel)
                    .toList();
            model.addAttribute("attemptedLevels", attemptedLevels);

            // ✅ Assign badge if all 3 levels completed and passed
            boolean allPassed = attemptedLevels.contains(1) && attemptedLevels.contains(2) && attemptedLevels.contains(3)
                                && assessmentResultService.hasPassed(candidate, 1)
                                && assessmentResultService.hasPassed(candidate, 2)
                                && assessmentResultService.hasPassed(candidate, 3);

            if (allPassed && (candidate.getBadge() == null || candidate.getBadge().isEmpty())) {
                candidate.setBadge("Java Expert"); // set desired badge
                candidateService.save(candidate);   // save updated candidate
                model.addAttribute("badge", candidate.getBadge());
            }

            return "candidate-welcome";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "candidate-login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        session.invalidate(); // remove candidate from session
        model.addAttribute("message", "You have logged out successfully!");
        return "candidate-login";
    }
}

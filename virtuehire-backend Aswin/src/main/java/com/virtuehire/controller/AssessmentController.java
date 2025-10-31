package com.virtuehire.controller;

import com.virtuehire.model.Candidate;
import com.virtuehire.model.Question;
import com.virtuehire.service.AssessmentResultService;
import com.virtuehire.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/assessment")
public class AssessmentController {

    private final QuestionService questionService;
    private final AssessmentResultService resultService;

    private static final int MAX_LEVELS = 5; // adjust to your total levels

    public AssessmentController(QuestionService questionService,
                                AssessmentResultService resultService) {
        this.questionService = questionService;
        this.resultService = resultService;
    }

    // Assessment home page
    @GetMapping
    public String assessmentHome(HttpSession session, Model model) {
        Candidate candidate = (Candidate) session.getAttribute("candidate");
        if (candidate == null) return "redirect:/login";

        // Load all results from DB
        Map<Integer, Boolean> levelResults = resultService.getLevelResults(candidate);
        session.setAttribute("levelResults", levelResults);

        // Add to model for Thymeleaf
        model.addAttribute("levelResults", levelResults);

        // Calculate next available level
        int nextLevel = 1;
        for (int lvl = 1; lvl <= MAX_LEVELS; lvl++) {
            Boolean passed = levelResults.get(lvl);
            if (passed == null || !passed) {
                nextLevel = lvl;
                if (passed != null && !passed) {
                    model.addAttribute("error", "You failed Level " + lvl + ". Cannot continue.");
                }
                break;
            }
            nextLevel = lvl + 1;
        }

        session.setAttribute("currentLevel", nextLevel);
        model.addAttribute("currentLevel", nextLevel);

        return "assessment-home";
    }

    // Start current level
    @GetMapping("/start")
    public String startAssessment(HttpSession session, Model model) {
        Candidate candidate = (Candidate) session.getAttribute("candidate");
        if (candidate == null) return "redirect:/login";

        Integer currentLevel = (Integer) session.getAttribute("currentLevel");
        if (currentLevel == null) currentLevel = 1;

        // Prevent retake
        if (resultService.hasAttempted(candidate, currentLevel)) {
            model.addAttribute("error", "You have already attempted Level " + currentLevel + ".");
            return "assessment-home";
        }

        // Ensure previous level is passed
        if (currentLevel > 1 && !resultService.hasPassed(candidate, currentLevel - 1)) {
            model.addAttribute("error", "You must pass Level " + (currentLevel - 1) + " before attempting this level.");
            return "assessment-home";
        }

        return "redirect:/assessment/level/" + currentLevel;
    }

    // Show questions for a level
    @GetMapping("/level/{level}")
    public String getLevel(@PathVariable int level, HttpSession session, Model model) {
        Candidate candidate = (Candidate) session.getAttribute("candidate");
        if (candidate == null) return "redirect:/login";

        // Prevent retake
        if (resultService.hasAttempted(candidate, level)) {
            model.addAttribute("error", "You have already attempted Level " + level + ".");
            return "assessment-home";
        }

        // Ensure previous level passed
        if (level > 1 && !resultService.hasPassed(candidate, level - 1)) {
            model.addAttribute("error", "You must pass Level " + (level - 1) + " before accessing Level " + level + ".");
            return "assessment-home";
        }

        List<Question> questions = questionService.getQuestionsByLevel(level);
        if (questions.isEmpty()) return "assessment-complete";

        model.addAttribute("questions", questions);
        model.addAttribute("level", level);
        return "assessment-level";
    }

    // Submit answers for a level
    @PostMapping("/submit/{level}")
    public String submitLevel(@PathVariable int level,
                              @RequestParam Map<String, String> answers,
                              HttpSession session,
                              Model model) {

        Candidate candidate = (Candidate) session.getAttribute("candidate");
        if (candidate == null) return "redirect:/login";

        // Prevent double submit
        if (resultService.hasAttempted(candidate, level)) {
            model.addAttribute("error", "You have already submitted Level " + level + ".");
            return "assessment-home";
        }

        // Evaluate answers (score as percentage)
        Map<String, Object> result = questionService.evaluateWithScore(level, answers);
        int score = (Integer) result.get("score");
        int total = (Integer) result.get("total");
        boolean passed = (Boolean) result.get("passed");

        // Save result in DB
        resultService.saveResult(candidate, level, score);

        // Update session results from DB
        Map<Integer, Boolean> updatedResults = resultService.getLevelResults(candidate);
        session.setAttribute("levelResults", updatedResults);
        model.addAttribute("levelResults", updatedResults);

        // Update current level
        int nextLevel = level + 1;
        if (!passed) nextLevel = level; // failed level remains current
        session.setAttribute("currentLevel", nextLevel);
        model.addAttribute("currentLevel", nextLevel);

        if (!passed) {
            model.addAttribute("error", "You failed this level. Cannot continue.");
        }

        model.addAttribute("score", score);
        model.addAttribute("total", total);
        model.addAttribute("passed", passed);
        model.addAttribute("level", level);

        return "assessment-result";
    }
}

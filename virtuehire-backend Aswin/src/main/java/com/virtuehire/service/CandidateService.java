package com.virtuehire.service;

import com.virtuehire.model.Candidate;
import com.virtuehire.repository.AssessmentResultRepository;
import com.virtuehire.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    private final CandidateRepository repo;
    private final AssessmentResultRepository assessmentResultRepo;


    public CandidateService(CandidateRepository repo, AssessmentResultRepository assessmentResultRepo) {
        this.repo = repo;
        this.assessmentResultRepo = assessmentResultRepo;
    }

    public Optional<Candidate> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    // Save candidate
    public Candidate save(Candidate c) {
        return repo.save(c);
    }

    // Get all candidates
    public List<Candidate> findAll() {
        return repo.findAll();
    }

    // Login using email + password
    public Candidate login(String email, String password) {
        Optional<Candidate> optionalCandidate = repo.findByEmail(email);
        if (optionalCandidate.isPresent()) {
            Candidate candidate = optionalCandidate.get();
            // Check if candidate is approved AND password matches
            if (candidate.getPassword().equals(password) && candidate.getApproved()) {
                return candidate;
            }
        }
        return null;
    }

    // Get filtered candidates for HR dashboard
    public List<Candidate> getFilteredCandidates(String language, Integer minScore, Integer maxScore,
                                                 String experienceLevel, String sortBy, String sortDirection) {
        List<Candidate> allCandidates = repo.findAll();

        return allCandidates.stream()
                .filter(candidate -> language == null || language.trim().isEmpty() ||
                        (candidate.getSkills() != null &&
                                candidate.getSkills().toLowerCase().contains(language.toLowerCase())))
                .filter(candidate -> minScore == null ||
                        (candidate.getScore() != null &&
                                candidate.getScore() >= minScore))
                .filter(candidate -> maxScore == null ||
                        (candidate.getScore() != null &&
                                candidate.getScore() <= maxScore))
                .filter(candidate -> experienceLevel == null || experienceLevel.trim().isEmpty() ||
                        (candidate.getExperienceLevel() != null &&
                                candidate.getExperienceLevel().equalsIgnoreCase(experienceLevel)))
                .collect(Collectors.toList());
    }

    // Get candidate score from assessment results
    private Integer getCandidateScore(Candidate candidate) {
        // You might want to get the latest assessment score
        // For now, using experience as a placeholder for score
        return candidate.getScore(); // Replace with actual score logic
    }

    // Find candidates by approval status
    public List<Candidate> findByApprovedFalse() {
        return repo.findAll().stream()
                .filter(c -> !c.getApproved())
                .collect(Collectors.toList());
    }

    public List<Candidate> findByApprovedTrue() {
        return repo.findAll().stream()
                .filter(c -> c.getApproved())
                .collect(Collectors.toList());
    }

    // Create comparator for sorting
    private Comparator<Candidate> createCandidateComparator(String sortBy, String sortDirection) {
        String sortField = sortBy != null ? sortBy : "experience";
        String direction = sortDirection != null ? sortDirection : "DESC";

        Comparator<Candidate> comparator;
        switch (sortField.toLowerCase()) {
            case "score":
                comparator = Comparator.comparing(this::getCandidateScore,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "experience":
                comparator = Comparator.comparing(Candidate::getExperience,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "name":
                comparator = Comparator.comparing(Candidate::getFullName,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
                break;
            default:
                comparator = Comparator.comparing(this::getCandidateScore,
                        Comparator.nullsLast(Comparator.naturalOrder()));
        }

        return "DESC".equalsIgnoreCase(direction) ? comparator.reversed() : comparator;
    }

    // Get candidate by ID
    public Optional<Candidate> findById(Long id) {
        return repo.findById(id);
    }

    /**
     * Simplified search with 3 filters only
     */
    public List<Candidate> searchCandidates(String skills, String experienceLevel, Integer minScore) {
        List<Candidate> allCandidates = repo.findAll();

        return allCandidates.stream()
                .filter(candidate -> {
                    // Filter by skills
                    if (skills != null && !skills.trim().isEmpty()) {
                        String candidateSkills = candidate.getSkills() != null ?
                                candidate.getSkills().toLowerCase() : "";
                        String searchSkills = skills.toLowerCase();

                        String[] keywords = searchSkills.split(",");
                        boolean skillMatch = false;
                        for (String keyword : keywords) {
                            if (candidateSkills.contains(keyword.trim())) {
                                skillMatch = true;
                                break;
                            }
                        }
                        if (!skillMatch) {
                            return false;
                        }
                    }

                    // Filter by experience level
                    if (experienceLevel != null && !experienceLevel.equals("All")) {
                        Integer exp = candidate.getExperience() != null ? candidate.getExperience() : 0;

                        if (experienceLevel.equals("Fresher")) {
                            if (exp > 1) {
                                return false;
                            }
                        } else if (experienceLevel.equals("Experienced")) {
                            if (exp < 2) {
                                return false;
                            }
                        }
                    }

                    // Filter by minimum score - USE getScore()
                    if (minScore != null && candidate.getScore() != null) {
                        if (candidate.getScore() < minScore) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }



    // Get dashboard statistics
    public Map<String, Object> getDashboardStats() {
        List<Candidate> allCandidates = repo.findAll();

        long totalCandidates = allCandidates.size();
        long experiencedCandidates = allCandidates.stream()
                .filter(c -> c.getExperience() != null && c.getExperience() > 0)
                .count();

        // Calculate unique skills count
        long uniqueSkills = allCandidates.stream()
                .filter(c -> c.getSkills() != null && !c.getSkills().isEmpty())
                .map(c -> c.getSkills().split(","))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .distinct()
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCandidates", totalCandidates);
        stats.put("experiencedCandidates", experiencedCandidates);
        stats.put("uniqueSkills", uniqueSkills);

        return stats;
    }

}


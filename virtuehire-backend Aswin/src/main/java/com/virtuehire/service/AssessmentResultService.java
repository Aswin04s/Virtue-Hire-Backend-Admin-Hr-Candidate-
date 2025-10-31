package com.virtuehire.service;

import com.virtuehire.model.AssessmentResult;
import com.virtuehire.model.Candidate;
import com.virtuehire.repository.AssessmentResultRepository;
import com.virtuehire.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AssessmentResultService {

    @Autowired
    private AssessmentResultRepository resultRepo;

    @Autowired
    private CandidateRepository candidateRepo;

    public static final int PASS_MARKS = 50;

    public boolean hasAttempted(Long candidateId, int level) {
        return resultRepo.findByCandidateIdAndLevel(candidateId, level).isPresent();
    }

    public boolean hasAttempted(Candidate candidate, int level) {
        return hasAttempted(candidate.getId(), level);
    }

    public void saveResult(Candidate candidate, int level, int score) {
        if (hasAttempted(candidate, level)) return;
        AssessmentResult result = new AssessmentResult();
        result.setCandidate(candidate);
        result.setLevel(level);
        result.setScore(score);
        resultRepo.save(result);

        if (level == 3) {
            calculateCumulativeAndAssignBadge(candidate);
        }
    }

    public List<AssessmentResult> getCandidateResults(Long candidateId) {
        return resultRepo.findByCandidateId(candidateId);
    }

    public Map<Integer, Boolean> getLevelResults(Candidate candidate) {
        List<AssessmentResult> results = getCandidateResults(candidate.getId());
        Map<Integer, Boolean> map = new HashMap<>();
        for (AssessmentResult r : results) {
            map.put(r.getLevel(), r.getScore() >= PASS_MARKS);
        }
        return map;
    }

    public boolean hasPassed(Candidate candidate, int level) {
        Optional<AssessmentResult> resultOpt = resultRepo.findByCandidateIdAndLevel(candidate.getId(), level);
        return resultOpt.isPresent() && resultOpt.get().getScore() >= PASS_MARKS;
    }

    public boolean hasFailedPreviousLevels(Candidate candidate, int currentLevel) {
        List<AssessmentResult> results = getCandidateResults(candidate.getId());
        for (AssessmentResult r : results) {
            if (r.getLevel() < currentLevel && r.getScore() < PASS_MARKS) {
                return true;
            }
        }
        return false;
    }

    public double calculateCumulativeAndAssignBadge(Candidate candidate) {
        List<AssessmentResult> results = getCandidateResults(candidate.getId());

        if (results.isEmpty()) {
            return 0.0;
        }

        int totalScore = 0;
        int totalPossible = results.size() * 100;
        boolean reachedLevel3 = false;
        boolean passedLevel3 = false;

        for (AssessmentResult r : results) {
            totalScore += r.getScore();

            if (r.getLevel() == 3) {
                reachedLevel3 = true;
                if (r.getScore() >= PASS_MARKS) {
                    passedLevel3 = true;
                }
            }
        }

        double cumulativePercentage = (totalScore * 100.0) / totalPossible;

        if (reachedLevel3 && passedLevel3 && cumulativePercentage >= 95) {
            candidate.setBadge("Java Expert");
        } else {
            candidate.setBadge(null);
        }

        candidateRepo.save(candidate);
        return cumulativePercentage;
    }
}
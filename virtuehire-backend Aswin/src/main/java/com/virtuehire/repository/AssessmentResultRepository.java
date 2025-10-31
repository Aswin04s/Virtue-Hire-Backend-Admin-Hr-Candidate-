package com.virtuehire.repository;

import com.virtuehire.model.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    Optional<AssessmentResult> findByCandidateIdAndLevel(Long candidateId, int level);
    List<AssessmentResult> findByCandidateId(Long candidateId);
}

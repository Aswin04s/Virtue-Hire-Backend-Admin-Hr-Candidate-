package com.virtuehire.repository;

import com.virtuehire.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    // Find candidate by email (needed for login)
    Optional<Candidate> findByEmail(String email);

    // Optional: combined email+password lookup
    Optional<Candidate> findByEmailAndPassword(String email, String password);
}

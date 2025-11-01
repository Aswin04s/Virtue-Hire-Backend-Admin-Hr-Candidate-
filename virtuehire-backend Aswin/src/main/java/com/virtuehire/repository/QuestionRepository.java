package com.virtuehire.repository;

import com.virtuehire.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySubjectAndLevel(String subject, int level);
    List<Question> findBySubject(String subject);


    List<Question> findByLevel(int level);

    // Get all unique subjects
    @Query("SELECT DISTINCT q.subject FROM Question q")
    List<String> findDistinctSubject();
}
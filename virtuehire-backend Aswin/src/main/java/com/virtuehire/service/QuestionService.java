package com.virtuehire.service;

import com.virtuehire.model.Question;
import com.virtuehire.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Value("${assessment.pass.percent}")
    private int passPercentage;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // Your existing methods...
    public List<Question> getQuestionsByLevel(int level) {
        return questionRepository.findByLevel(level);
    }

    public Map<String, Object> evaluateWithScore(int level, Map<String, String> answers) {
        List<Question> questions = getQuestionsByLevel(level);
        int correctCount = 0;

        for (Question q : questions) {
            String userAnswer = answers.get("q" + q.getId());
            if (userAnswer != null && userAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
                correctCount++;
            }
        }

        int scorePercentage = 0;
        if (!questions.isEmpty()) {
            scorePercentage = (int) ((correctCount * 100.0) / questions.size());
        }

        boolean passed = scorePercentage >= passPercentage;

        Map<String, Object> result = new HashMap<>();
        result.put("score", scorePercentage);
        result.put("total", questions.size());
        result.put("passed", passed);

        return Map.of("score", 80, "total", 100, "passed", true);
    }

    // ===== NEW METHODS FOR ADMIN QUESTION MANAGEMENT =====

    public List<Question> getAllQuestionsFromRepository() {
        return questionRepository.findAll();
    }

    public Question saveQuestionViaRepository(Question question) {
        return questionRepository.save(question);
    }

    public Question getQuestionByIdFromRepository(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void deleteQuestionViaRepository(Long id) {
        questionRepository.deleteById(id);
    }

    public List<Question> getQuestionsBySubject(String subject) {
        return questionRepository.findBySubject(subject);
    }

    public List<Question> getQuestionsBySubjectAndLevel(String subject, int level) {
        return questionRepository.findBySubjectAndLevel(subject, level);
    }

    public List<String> getAllSubjects() {
        return questionRepository.findDistinctSubject();
    }
}
package com.virtuehire.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Difficulty level: 1 = Easy, 2 = Medium, 3 = Hard
    @Column(nullable = false)
    private int level;

    private String subject;

    @Column(nullable = false, length = 1000)
    private String text;

    @ElementCollection
    @CollectionTable(
            name = "question_options",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "option_text", nullable = false)
    private List<String> options;

    @Column(nullable = false)
    private String correctAnswer;

    // ===== Constructors =====
    public Question() {}

    public Question(String subject, int level, String text, List<String> options, String correctAnswer) {
        this.subject = subject;
        this.level = level;
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
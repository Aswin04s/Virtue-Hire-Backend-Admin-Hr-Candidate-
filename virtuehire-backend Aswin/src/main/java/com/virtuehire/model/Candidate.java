package com.virtuehire.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    private String alternatePhoneNumber;

    @NotBlank
    private String password;

    @Transient
    private String confirmPassword;

    private String gender;
    private String city;
    private String state;
    private String highestEducation;
    private String collegeUniversity;

    @PositiveOrZero
    private Integer yearOfGraduation;

    private String skills;
    private Integer experience; // in years

    private String resumePath; // store file path in uploads/

    // 🔹 New fields
    private String badge;       // e.g., "Java Expert"
    private String profilePic;  // store path of uploaded profile image

    private String experienceLevel; // Fresher/Experienced
    private String languagePreference; // Java, Python, etc
    private Boolean assessmentTaken = false;
    private Integer score;
    private String levelAttempted; // Basic/Intermediate/Advanced

    // Getters and setters for new fields
    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }

    public String getLanguagePreference() { return languagePreference; }
    public void setLanguagePreference(String languagePreference) { this.languagePreference = languagePreference; }

    public Boolean getAssessmentTaken() { return assessmentTaken; }
    public void setAssessmentTaken(Boolean assessmentTaken) { this.assessmentTaken = assessmentTaken; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getLevelAttempted() { return levelAttempted; }
    public void setLevelAttempted(String levelAttempted) { this.levelAttempted = levelAttempted; }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAlternatePhoneNumber() { return alternatePhoneNumber; }
    public void setAlternatePhoneNumber(String alternatePhoneNumber) { this.alternatePhoneNumber = alternatePhoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getHighestEducation() { return highestEducation; }
    public void setHighestEducation(String highestEducation) { this.highestEducation = highestEducation; }

    public String getCollegeUniversity() { return collegeUniversity; }
    public void setCollegeUniversity(String collegeUniversity) { this.collegeUniversity = collegeUniversity; }

    public Integer getYearOfGraduation() { return yearOfGraduation; }
    public void setYearOfGraduation(Integer yearOfGraduation) { this.yearOfGraduation = yearOfGraduation; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
}

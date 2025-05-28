package com.example.personalizedlearningexperienceapp.entity;

public class UserStats {
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;

    public UserStats(int totalQuestions, int correctAnswers, int incorrectAnswers) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
    }

    // Getters and setters
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public int getIncorrectAnswers() { return incorrectAnswers; }
    public void setIncorrectAnswers(int incorrectAnswers) { this.incorrectAnswers = incorrectAnswers; }
}

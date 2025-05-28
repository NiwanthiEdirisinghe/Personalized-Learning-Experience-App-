package com.example.personalizedlearningexperienceapp.entity;

import java.util.ArrayList;
import java.util.List;

public class QuizHistory {
    private String questionTitle;
    private int score;
    private String date;
    private boolean isCompleted;
    private List<QuestionResult> questionResults;

    public QuizHistory(String questionTitle, int score, String date, boolean isCompleted) {
        this.questionTitle = questionTitle;
        this.score = score;
        this.date = date;
        this.isCompleted = isCompleted;
        this.questionResults = new ArrayList<>();
    }

    public QuizHistory(String questionTitle, int score, String date, boolean isCompleted,
                       List<QuestionResult> questionResults) {
        this.questionTitle = questionTitle;
        this.score = score;
        this.date = date;
        this.isCompleted = isCompleted;
        this.questionResults = questionResults != null ? questionResults : new ArrayList<>();
    }

    public String getQuestionTitle() { return questionTitle; }
    public void setQuestionTitle(String questionTitle) { this.questionTitle = questionTitle; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public List<QuestionResult> getQuestionResults() { return questionResults; }
    public void setQuestionResults(List<QuestionResult> questionResults) {
        this.questionResults = questionResults != null ? questionResults : new ArrayList<>();
    }

    public void addQuestionResult(QuestionResult questionResult) {
        if (questionResults == null) {
            questionResults = new ArrayList<>();
        }
        questionResults.add(questionResult);
    }

    public void addAllQuestionResults(List<QuestionResult> results) {
        if (questionResults == null) {
            questionResults = new ArrayList<>();
        }
        questionResults.addAll(results);
    }

    public int getTotalQuestions() {
        return questionResults != null ? questionResults.size() : 3;
    }

    public String getFormattedScore() {
        return score + "/" + getTotalQuestions();
    }

    public int getPercentageScore() {
        int total = getTotalQuestions();
        return total > 0 ? (int) ((score * 100.0) / total) : 0;
    }

    public String getFormattedPercentage() {
        return getPercentageScore() + "%";
    }

    public boolean isPassed() {
        return getPercentageScore() >= 60;
    }

    public String getResultStatus() {
        int percentage = getPercentageScore();
        if (percentage >= 90) return "Excellent";
        if (percentage >= 75) return "Good";
        if (percentage >= 60) return "Passed";
        return "Failed";
    }

    public String getAnswerBreakdown() {
        if (questionResults == null || questionResults.isEmpty()) {
            int total = 3;
            return String.format("%d Correct, %d Wrong", score, total - score);
        }

        int correct = 0;
        int wrong = 0;

        for (QuestionResult result : questionResults) {
            if (result.isCorrect()) {
                correct++;
            } else {
                wrong++;
            }
        }

        return String.format("%d Correct, %d Wrong", correct, wrong);
    }

    public String getQuestionBreakdown() {
        if (questionResults == null || questionResults.isEmpty()) {
            return "No detailed results available";
        }

        StringBuilder breakdown = new StringBuilder();

        for (int i = 0; i < questionResults.size(); i++) {
            QuestionResult result = questionResults.get(i);
            breakdown.append("Q").append(i + 1).append(": ")
                    .append(result.isCorrect() ? "✅" : "❌");

            if (i < questionResults.size() - 1) {
                breakdown.append(" ");
            }
        }

        return breakdown.toString();
    }

    public boolean hasDetailedResults() {
        return questionResults != null && !questionResults.isEmpty();
    }

    @Override
    public String toString() {
        return "QuizHistory{" +
                "questionTitle='" + questionTitle + '\'' +
                ", score=" + score +
                ", date='" + date + '\'' +
                ", isCompleted=" + isCompleted +
                ", hasDetailedResults=" + hasDetailedResults() +
                '}';
    }
}

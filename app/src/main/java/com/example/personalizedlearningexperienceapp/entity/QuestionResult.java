package com.example.personalizedlearningexperienceapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class QuestionResult implements Serializable {
    private int questionNumber;
    private String questionText;
    private String userAnswer;
    private String correctAnswer;
    private boolean isCorrect;

    public QuestionResult(int questionNumber, String questionText, String userAnswer, String correctAnswer) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer.trim());
    }
    public QuestionResult(int questionNumber, String questionText, String userAnswer,
                          String correctAnswer, boolean isCorrect) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
    }

    public int getQuestionNumber() { return questionNumber; }
    public void setQuestionNumber(int questionNumber) { this.questionNumber = questionNumber; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
        this.isCorrect = userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer.trim());
    }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }

    public String getResultStatus() {
        return isCorrect ? "Correct" : "Wrong";
    }
    public String getFormattedQuestion() {
        return "Q" + questionNumber + ": " + questionText;
    }

    public String getAnswerComparison() {
        if (isCorrect) {
            return "Your answer: " + userAnswer;
        } else {
            return "Your answer: " + userAnswer + "\nCorrect answer: " + correctAnswer;
        }
    }

    public int getResultColor() {
        return isCorrect ? android.R.color.holo_green_light : android.R.color.holo_red_light;
    }

    @Override
    public String toString() {
        return "Q" + questionNumber + ": " + (isCorrect ? "✅" : "❌") + " " + questionText;
    }
}

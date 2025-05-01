package com.example.personalizedlearningexperienceapp;

import java.io.Serializable;
import java.util.List;

public class Quiz implements Serializable {
    private String topic;
    private String description;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    public Quiz() {
    }

    public Quiz(String topic, String description, List<Question> questions) {
        this.topic = topic;
        this.description = description;
        this.questions = questions;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public void nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
        }
    }

    public Question getCurrentQuestion() {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean hasNextQuestion() {
        return questions != null && currentQuestionIndex < questions.size() - 1;
    }

    public int getTotalQuestions() {
        return questions != null ? questions.size() : 0;
    }

    public int getCorrectAnswersCount() {
        int count = 0;
        if (questions != null) {
            for (Question question : questions) {
                if (question.isCorrect()) {
                    count++;
                }
            }
        }
        return count;
    }
}

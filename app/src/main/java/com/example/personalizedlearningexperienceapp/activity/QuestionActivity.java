package com.example.personalizedlearningexperienceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.entity.QuestionResult;
import com.example.personalizedlearningexperienceapp.util.ApiClient;
import com.example.personalizedlearningexperienceapp.util.DBHelper;
import com.example.personalizedlearningexperienceapp.adapter.QuestionAdapter;
import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.entity.Question;
import com.example.personalizedlearningexperienceapp.entity.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private TextView textViewQuizTitle, textViewQuizDescription;
    private RecyclerView recyclerViewQuestions;
    private Button buttonSubmitAnswer;
    private LinearLayout loadingContainer;

    private Quiz currentQuiz;
    private long userId;
    private String userName;
    private String taskTopic;

    private DBHelper dbHelper;
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        textViewQuizTitle = findViewById(R.id.textViewQuizTitle);
        textViewQuizDescription = findViewById(R.id.textViewQuizDescription);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        buttonSubmitAnswer = findViewById(R.id.buttonSubmitAnswer);
        loadingContainer = findViewById(R.id.loadingContainer);

        String taskTitle = getIntent().getStringExtra("TASK_TITLE");
        String taskDescription = getIntent().getStringExtra("TASK_DESCRIPTION");
        taskTopic = getIntent().getStringExtra("TASK_TOPIC");
        userId = getIntent().getLongExtra("USER_ID", -1);
        userName = getIntent().getStringExtra("USERNAME");

        dbHelper = new DBHelper(this);

        textViewQuizTitle.setText(taskTitle);
        textViewQuizDescription.setText(taskDescription);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));

        showLoading(true);
        buttonSubmitAnswer.setEnabled(false);

        loadQuiz(taskTopic);
        buttonSubmitAnswer.setOnClickListener(view -> handleSubmit());
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            loadingContainer.setVisibility(View.VISIBLE);
            recyclerViewQuestions.setVisibility(View.GONE);
            buttonSubmitAnswer.setEnabled(false);
        } else {
            loadingContainer.setVisibility(View.GONE);
            recyclerViewQuestions.setVisibility(View.VISIBLE);
            buttonSubmitAnswer.setEnabled(true);
        }
    }

    private void loadQuiz(String topic) {
        ApiClient.getQuiz(topic, new ApiClient.QuizCallback() {
            @Override
            public void onQuizReceived(Quiz quiz) {
                currentQuiz = quiz;
                setupRecyclerView(quiz.getQuestions());
                showLoading(false);
            }

            @Override
            public void onError(String errorMessage) {
                currentQuiz = ApiClient.getDummyQuiz(topic);
                setupRecyclerView(currentQuiz.getQuestions());
                showLoading(false);
                Toast.makeText(QuestionActivity.this, "Using offline quiz: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<Question> questions) {
        adapter = new QuestionAdapter(questions);
        recyclerViewQuestions.setAdapter(adapter);
    }

    private void handleSubmit() {
        if (!areAllQuestionsAnswered()) {
            Toast.makeText(this, "Please answer all questions before submitting", Toast.LENGTH_SHORT).show();
            return;
        }

        int correctAnswers = currentQuiz.getCorrectAnswersCount();
        int totalQuestions = currentQuiz.getQuestions().size();

        List<QuestionResult> questionResults = createQuestionResults();

        dbHelper.saveQuizResultWithQuestions(userId, taskTopic, correctAnswers, totalQuestions, questionResults);

        Intent intent = new Intent(QuestionActivity.this, ResultsActivity.class);
        intent.putExtra("SCORE", correctAnswers);
        intent.putExtra("QUIZ", currentQuiz);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("TOTAL", totalQuestions);
        startActivity(intent);
        finish();
    }

    private List<QuestionResult> createQuestionResults() {
        List<QuestionResult> questionResults = new ArrayList<>();

        if (currentQuiz == null || currentQuiz.getQuestions() == null) {
            return questionResults;
        }

        List<Question> questions = currentQuiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            String userAnswerText = getUserAnswerText(question);
            String correctAnswerText = getCorrectAnswerText(question);

            QuestionResult result = new QuestionResult(
                    i + 1,
                    question.getQuestionText(),
                    userAnswerText,
                    correctAnswerText
            );

            questionResults.add(result);
        }

        return questionResults;
    }

    private String getUserAnswerText(Question question) {
        String userAnswer = question.getUserAnswer();
        List<String> options = question.getOptions();

        if (userAnswer == null || options == null || options.isEmpty()) {
            return "No answer";
        }

        switch (userAnswer) {
            case "A": return options.size() > 0 ? options.get(0) : "No answer";
            case "B": return options.size() > 1 ? options.get(1) : "No answer";
            case "C": return options.size() > 2 ? options.get(2) : "No answer";
            case "D": return options.size() > 3 ? options.get(3) : "No answer";
            default: return "No answer";
        }
    }

    private String getCorrectAnswerText(Question question) {
        String correctAnswer = question.getCorrectAnswer();
        List<String> options = question.getOptions();

        if (correctAnswer == null || options == null || options.isEmpty()) {
            return "Unknown";
        }

        switch (correctAnswer) {
            case "A": return options.size() > 0 ? options.get(0) : "Unknown";
            case "B": return options.size() > 1 ? options.get(1) : "Unknown";
            case "C": return options.size() > 2 ? options.get(2) : "Unknown";
            case "D": return options.size() > 3 ? options.get(3) : "Unknown";
            default: return "Unknown";
        }
    }

    private boolean areAllQuestionsAnswered() {
        if (currentQuiz == null || currentQuiz.getQuestions() == null) {
            return false;
        }

        for (Question question : currentQuiz.getQuestions()) {
            if (question.getUserAnswer() == null || question.getUserAnswer().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
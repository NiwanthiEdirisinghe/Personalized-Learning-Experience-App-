package com.example.personalizedlearningexperienceapp;

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
        int correctAnswers = currentQuiz.getCorrectAnswersCount();
        dbHelper.saveQuizResult(userId, taskTopic, correctAnswers);

        Intent intent = new Intent(QuestionActivity.this, ResultsActivity.class);
        intent.putExtra("SCORE", correctAnswers);
        intent.putExtra("QUIZ", currentQuiz);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("USERNAME", userName);
        intent.putExtra("TOTAL", currentQuiz.getQuestions().size());
        startActivity(intent);
        finish();
    }
}
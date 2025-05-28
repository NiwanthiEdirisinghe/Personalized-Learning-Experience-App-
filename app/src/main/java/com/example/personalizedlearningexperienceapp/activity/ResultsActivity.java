package com.example.personalizedlearningexperienceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.adapter.ResultsAdapter;
import com.example.personalizedlearningexperienceapp.entity.Quiz;

public class ResultsActivity extends AppCompatActivity {

    private TextView textViewScore, textViewPercentage;
    private RecyclerView recyclerViewResults;
    private Button buttonContinue, buttonViewProfile, buttonViewHistory;

    private Quiz quiz;
    private long userId;
    private String userName;
    private int score;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        initializeViews();
        getIntentData();
        setupViews();
        setupClickListeners();
    }

    private void initializeViews() {
        textViewScore = findViewById(R.id.textViewScore);
        textViewPercentage = findViewById(R.id.textViewPercentage);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);
        buttonContinue = findViewById(R.id.buttonContinue);
        buttonViewProfile = findViewById(R.id.buttonViewProfile);
        buttonViewHistory = findViewById(R.id.buttonViewHistory);
    }

    private void getIntentData() {
        score = getIntent().getIntExtra("SCORE", 0);
        total = getIntent().getIntExtra("TOTAL", 0);
        quiz = (Quiz) getIntent().getSerializableExtra("QUIZ");
        userId = getIntent().getLongExtra("USER_ID", -1);
        userName = getIntent().getStringExtra("USERNAME");
    }

    private void setupViews() {
        textViewScore.setText("Your Score: " + score + " / " + total);

        int percentage = total > 0 ? (int) ((score * 100.0) / total) : 0;
        textViewPercentage.setText("Percentage: " + percentage + "%");

        if (quiz != null && quiz.getQuestions() != null) {
            recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
            ResultsAdapter adapter = new ResultsAdapter(quiz.getQuestions());
            recyclerViewResults.setAdapter(adapter);
        }
    }

    private void setupClickListeners() {
        buttonContinue.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, DashboardActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USERNAME", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        buttonViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USERNAME", userName);
            startActivity(intent);
        });

        buttonViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, HistoryActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USERNAME", userName);
            startActivity(intent);
        });
    }
}
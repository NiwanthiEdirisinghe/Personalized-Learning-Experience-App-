package com.example.personalizedlearningexperienceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ResultsActivity extends AppCompatActivity {

    private TextView textViewScore;
    private RecyclerView recyclerViewResults;
    private Button buttonContinue;

    private Quiz quiz;
    private long userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        textViewScore = findViewById(R.id.textViewScore);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);
        buttonContinue = findViewById(R.id.buttonContinue);

        int score = getIntent().getIntExtra("SCORE", 0);
        int total = getIntent().getIntExtra("TOTAL", 0);
        quiz = (Quiz) getIntent().getSerializableExtra("QUIZ");
        userId = getIntent().getLongExtra("USER_ID", -1);
        userName = getIntent().getStringExtra("USERNAME");

        textViewScore.setText("Your Score: " + score + " / " + total);

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        ResultsAdapter adapter = new ResultsAdapter(quiz.getQuestions());
        recyclerViewResults.setAdapter(adapter);


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, DashboardActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("USERNAME", userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
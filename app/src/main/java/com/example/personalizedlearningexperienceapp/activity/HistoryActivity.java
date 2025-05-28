package com.example.personalizedlearningexperienceapp.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.entity.QuestionResult;
import com.example.personalizedlearningexperienceapp.util.DBHelper;
import com.example.personalizedlearningexperienceapp.adapter.HistoryAdapter;
import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.entity.QuizHistory;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private ImageView backButton;
    private TextView historyTitle;

    private long userId;
    private DBHelper dbHelper;
    private List<QuizHistory> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initializeViews();
        getUserData();
        setupRecyclerView();
        loadHistory();
        setupClickListeners();
    }

    private void initializeViews() {
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        backButton = findViewById(R.id.backButton);
        historyTitle = findViewById(R.id.historyTitle);

        dbHelper = new DBHelper(this);
        historyList = new ArrayList<>();
    }

    private void getUserData() {
        userId = getIntent().getLongExtra("USER_ID", -1);
    }

    private void setupRecyclerView() {
        historyAdapter = new HistoryAdapter(historyList);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }

    private void loadHistory() {
        historyList.clear();

        List<QuizHistory> detailedHistory = dbHelper.getQuizHistoryList(userId);

        if (detailedHistory != null && !detailedHistory.isEmpty()) {
            historyList.addAll(detailedHistory);
        } else {
            // Add some dummy data if no history exists for testing
            QuizHistory dummyQuiz1 = new QuizHistory("Math Quiz", 2, "Dec 15, 2024", true);
            List<QuestionResult> dummyResults1 = new ArrayList<>();
            dummyResults1.add(new QuestionResult(1, "What is 2+2?", "4", "4", true));
            dummyResults1.add(new QuestionResult(2, "What is 5+3?", "7", "8", false));
            dummyResults1.add(new QuestionResult(3, "What is 10-5?", "5", "5", true));
            dummyQuiz1.setQuestionResults(dummyResults1);

            QuizHistory dummyQuiz2 = new QuizHistory("Science Quiz", 3, "Dec 14, 2024", true);
            List<QuestionResult> dummyResults2 = new ArrayList<>();
            dummyResults2.add(new QuestionResult(1, "What is H2O?", "Water", "Water", true));
            dummyResults2.add(new QuestionResult(2, "What is the speed of light?", "300,000 km/s", "300,000 km/s", true));
            dummyResults2.add(new QuestionResult(3, "What is gravity?", "A force", "A force", true));
            dummyQuiz2.setQuestionResults(dummyResults2);

            historyList.add(dummyQuiz1);
            historyList.add(dummyQuiz2);
        }

        historyAdapter.notifyDataSetChanged();
    }
}
package com.example.personalizedlearningexperienceapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedlearningexperienceapp.util.DBHelper;
import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.entity.UserStats;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameText, totalQuestionsText, correctAnswersText, incorrectAnswersText;
    private Button shareButton;
    private ImageView profileIcon, backButton;

    private long userId;
    private String userName;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();
        getUserData();
        setupClickListeners();
        loadUserStats();
    }

    @SuppressLint("WrongViewCast")
    private void initializeViews() {
        usernameText = findViewById(R.id.usernameText);
        totalQuestionsText = findViewById(R.id.totalQuestionsText);
        correctAnswersText = findViewById(R.id.correctAnswersText);
        incorrectAnswersText = findViewById(R.id.incorrectAnswersText);
        shareButton = findViewById(R.id.shareButton);
        backButton = findViewById(R.id.backButton);
        profileIcon = findViewById(R.id.profileIcon);

        dbHelper = new DBHelper(this);
    }

    private void getUserData() {
        userId = getIntent().getLongExtra("USER_ID", -1);
        userName = getIntent().getStringExtra("USERNAME");

        if (userName != null) {
            usernameText.setText(userName);
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        shareButton.setOnClickListener(v -> shareProfile());
    }

    private void loadUserStats() {
        UserStats stats = dbHelper.getUserStats(userId);

        totalQuestionsText.setText(String.valueOf(stats.getTotalQuestions()));
        correctAnswersText.setText(String.valueOf(stats.getCorrectAnswers()));
        incorrectAnswersText.setText(String.valueOf(stats.getIncorrectAnswers()));
    }

    private void shareProfile() {
        String shareText = String.format(
                "🎯 My Learning Progress 🎯\n\n" +
                        "👤 User: %s\n" +
                        "📝 Total Questions: %s\n" +
                        "✅ Correct Answers: %s\n" +
                        "❌ Incorrect Answers: %s\n\n" +
                        "Check out this amazing learning app! 📚",
                userName,
                totalQuestionsText.getText().toString(),
                correctAnswersText.getText().toString(),
                incorrectAnswersText.getText().toString()
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Learning Progress");

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share your progress");

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooserIntent, SHARE_REQUEST_CODE);
        }
    }

    private static final int SHARE_REQUEST_CODE = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHARE_REQUEST_CODE) {
            showShareSuccessMessage();
        }
    }

    private void showShareSuccessMessage() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Share Successful!")
                .setMessage("Your learning progress has been shared successfully!")
                .setIcon(R.drawable.ic_check_circle)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }
}
package com.example.personalizedlearningexperienceapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedlearningexperienceapp.util.DBHelper;
import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.entity.User;

import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    private List<String> selectedInterests = new ArrayList<>();
    private Button nextButton;
    private TextView selectedCountText;
    private LinearLayout topicsContainer;


    private final String[] availableTopics = {
            "Algorithms", "Data Structures", "Web Development", "Testing",
            "Mobile Development", "Database Design", "Software Architecture",
            "UI/UX Design", "Machine Learning", "Cloud Computing",
            "DevOps", "Security", "Python", "Java", "JavaScript",
            "Blockchain", "IoT Development", "Game Development", "Big Data",
            "Network Programming", "Embedded Systems", "Natural Language Processing",
            "Computer Vision", "Deep Learning", "Cybersecurity",
            "AR/VR Development", "Functional Programming", "Microservices",
            "Docker & Kubernetes", "Design Patterns"
    };

    private User user;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        dbHelper = new DBHelper(this);
        user = (User) getIntent().getSerializableExtra("USER_DATA");

        initializeViews();
        createTopicButtons();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedInterests.size() < 1) {
                    Toast.makeText(InterestsActivity.this,
                            "Please select at least one interest", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveUserInterests();
                navigateToNextScreen();
            }
        });

        updateSelectionCounter();
    }

    private void initializeViews() {
        selectedCountText = findViewById(R.id.textViewSelectedCount);
        topicsContainer = findViewById(R.id.topicsContainer);
        nextButton = findViewById(R.id.buttonNext);
    }

    private void createTopicButtons() {
        topicsContainer.removeAllViews();

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 0, 0, 8);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0,
                120,
                1.0f);
        buttonParams.setMargins(4, 0, 4, 0);

        LinearLayout currentRow = null;

        for (int i = 0; i < availableTopics.length; i++) {
            if (i % 2 == 0) {
                currentRow = new LinearLayout(this);
                currentRow.setLayoutParams(rowParams);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                topicsContainer.addView(currentRow);
            }

            Button topicButton = new Button(this);
            String topic = availableTopics[i];
            topicButton.setText(topic);
            topicButton.setTextSize(18);
            topicButton.setLayoutParams(buttonParams);
            topicButton.setBackgroundColor(Color.parseColor("#6F8FAF"));
            topicButton.setTag(topic);

            if (currentRow != null) {
                currentRow.addView(topicButton);
            }

            topicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleTopicSelection((Button) v);
                }
            });
        }
    }

    private void toggleTopicSelection(Button button) {
        String topic = (String) button.getTag();

        if (selectedInterests.contains(topic)) {
            selectedInterests.remove(topic);
            button.setBackgroundColor(Color.parseColor("#6F8FAF"));
        } else {
            selectedInterests.add(topic);
            button.setBackgroundColor(Color.parseColor("#00FFFF"));
        }

        updateSelectionCounter();
    }

    private void updateSelectionCounter() {
        selectedCountText.setText("You may select up to 10 topics (" + selectedInterests.size() + " selected)");
    }

    private void saveUserInterests() {
        if (user != null) {
            String[] interestsArray = selectedInterests.toArray(new String[0]);
            user.setInterests(interestsArray);

            for (String interest : interestsArray) {
                dbHelper.addUserInterest(user.getId(), interest);
            }
        }
    }

    private void navigateToNextScreen() {
         Intent intent = new Intent(InterestsActivity.this, DashboardActivity.class);
        intent.putExtra("USER_ID", user.getId());
        intent.putExtra("USERNAME", user.getUsername());
         startActivity(intent);
         finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("selectedInterests", new ArrayList<>(selectedInterests));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<String> savedInterests = savedInstanceState.getStringArrayList("selectedInterests");
            if (savedInterests != null) {
                selectedInterests.clear();
                selectedInterests.addAll(savedInterests);

                for (int i = 0; i < topicsContainer.getChildCount(); i++) {
                    LinearLayout row = (LinearLayout) topicsContainer.getChildAt(i);
                    for (int j = 0; j < row.getChildCount(); j++) {
                        Button button = (Button) row.getChildAt(j);
                        String topic = (String) button.getTag();
                        boolean isSelected = selectedInterests.contains(topic);
                        button.setBackgroundColor(
                                isSelected ? Color.parseColor("#00FFFF") : Color.parseColor("#6F8FAF")
                        );
                    }
                }

                updateSelectionCounter();
            }
        }
    }
}
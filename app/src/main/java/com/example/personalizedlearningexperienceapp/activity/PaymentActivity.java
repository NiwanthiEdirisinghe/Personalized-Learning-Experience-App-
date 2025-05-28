package com.example.personalizedlearningexperienceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedlearningexperienceapp.R;

public class PaymentActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView upgradeTitle, upgradeSubtitle, planNameText, planPriceText;
    private Button purchaseButton;
    private LinearLayout googlePayContainer;

    private String planName, planPrice;
    private long userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initializeViews();
        getUserData();
        setupClickListeners();
        displayPlanInfo();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        upgradeTitle = findViewById(R.id.upgradeTitle);
        upgradeSubtitle = findViewById(R.id.upgradeSubtitle);
        planNameText = findViewById(R.id.planNameText);
        planPriceText = findViewById(R.id.planPriceText);
        purchaseButton = findViewById(R.id.purchaseButton);
        googlePayContainer = findViewById(R.id.googlePayContainer);
    }

    private void getUserData() {
        planName = getIntent().getStringExtra("PLAN_NAME");
        planPrice = getIntent().getStringExtra("PLAN_PRICE");
        userId = getIntent().getLongExtra("USER_ID", -1);
        userName = getIntent().getStringExtra("USERNAME");
    }

    private void displayPlanInfo() {
        planNameText.setText(planName);
        planPriceText.setText(planPrice);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        purchaseButton.setOnClickListener(v -> {
            processPayment();
        });

        googlePayContainer.setOnClickListener(v -> {
            simulateGooglePay();
        });
    }

    private void processPayment() {
        Toast.makeText(this, "Processing payment for " + planName + " plan...", Toast.LENGTH_SHORT).show();
        new android.os.Handler().postDelayed(() -> {
            Toast.makeText(PaymentActivity.this, "Payment successful! Welcome to " + planName + " plan!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(PaymentActivity.this, DashboardActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USERNAME", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 2000);
    }

    private void simulateGooglePay() {
        Toast.makeText(this, "Opening Google Pay...", Toast.LENGTH_SHORT).show();

        new android.os.Handler().postDelayed(() -> {
            Toast.makeText(PaymentActivity.this, "Google Pay payment successful!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(PaymentActivity.this, DashboardActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("USERNAME", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
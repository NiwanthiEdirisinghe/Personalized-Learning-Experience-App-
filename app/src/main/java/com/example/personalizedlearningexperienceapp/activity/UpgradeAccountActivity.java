package com.example.personalizedlearningexperienceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedlearningexperienceapp.R;
import com.example.personalizedlearningexperienceapp.util.DBHelper;

public class UpgradeAccountActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView upgradeTitle, upgradeSubtitle;
    private Button starterPurchaseButton, intermediatePurchaseButton, advancedPurchaseButton;

    private long userId;
    private String userName;
    private String currentSubscription;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_account);

        initializeViews();
        getUserData();
        checkCurrentSubscription();
        setupClickListeners();
        updateButtonStates();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        upgradeTitle = findViewById(R.id.upgradeTitle);
        upgradeSubtitle = findViewById(R.id.upgradeSubtitle);
        starterPurchaseButton = findViewById(R.id.starterPurchaseButton);
        intermediatePurchaseButton = findViewById(R.id.intermediatePurchaseButton);
        advancedPurchaseButton = findViewById(R.id.advancedPurchaseButton);

        dbHelper = new DBHelper(this);
    }

    private void getUserData() {
        userId = getIntent().getLongExtra("USER_ID", -1);
        userName = getIntent().getStringExtra("USERNAME");
    }

    private void checkCurrentSubscription() {
        currentSubscription = dbHelper.getUserSubscriptionPlan(userId);
        if (currentSubscription == null || currentSubscription.isEmpty()) {
            currentSubscription = "free";
        }
    }

    private void updateButtonStates() {
        updateButtonForPlan("Starter", starterPurchaseButton);
        updateButtonForPlan("Intermediate", intermediatePurchaseButton);
        updateButtonForPlan("Advanced", advancedPurchaseButton);
    }

    private void updateButtonForPlan(String planName, Button button) {
        if (currentSubscription.equalsIgnoreCase(planName)) {
            button.setText("✓ Already Purchased");
            button.setEnabled(false);
            button.setAlpha(0.6f);
        } else {
            button.setText("Purchase");
            button.setEnabled(true);
            button.setAlpha(1.0f);
        }
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        starterPurchaseButton.setOnClickListener(v -> {
            handlePlanSelection("Starter", "$4.99");
        });

        intermediatePurchaseButton.setOnClickListener(v -> {
            handlePlanSelection("Intermediate", "$9.99");
        });

        advancedPurchaseButton.setOnClickListener(v -> {
            handlePlanSelection("Advanced", "$19.99");
        });
    }

    private void handlePlanSelection(String planName, String price) {

        if (currentSubscription.equalsIgnoreCase(planName)) {
            showAlreadyPurchasedDialog(planName);
            return;
        }

        showPurchaseConfirmationDialog(planName, price);
    }

    private void showAlreadyPurchasedDialog(String planName) {
        new AlertDialog.Builder(this)
                .setTitle("Already Purchased")
                .setMessage("You have already purchased the " + planName + " plan!")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void showPurchaseConfirmationDialog(String planName, String price) {
        String currentPlanText = currentSubscription.equals("free") ? "Free" : currentSubscription;
        String message = "Upgrade from " + currentPlanText + " to " + planName + " plan for " + price + "?\n\n" +
                "You can pay using:\n" +
                "• Credit/Debit Card\n" +
                "• Google Pay\n" +
                "• PayPal\n" +
                "• Bank Transfer\n" +
                "• Other payment methods";

        new AlertDialog.Builder(this)
                .setTitle("Choose Payment Method")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Continue", (dialog, which) -> {
                    showPaymentMethodDialog(planName, price);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void showPaymentMethodDialog(String planName, String price) {
        String[] paymentMethods = {
                "💳 Credit/Debit Card",
                "📱 Google Pay",
                "💰 PayPal",
                "🏦 Bank Transfer",
                "💸 Apple Pay",
                "🔗 Other Methods"
        };

        new AlertDialog.Builder(this)
                .setTitle("Select Payment Method")
                .setItems(paymentMethods, (dialog, which) -> {
                    String selectedMethod = paymentMethods[which];
                    processPurchase(planName, price, selectedMethod);
                })
                .setNegativeButton("Back", (dialog, which) -> {
                    showPurchaseConfirmationDialog(planName, price);
                })
                .setCancelable(true)
                .show();
    }

    private void processPurchase(String planName, String price, String paymentMethod) {
        showProcessingDialog(paymentMethod);

        new android.os.Handler().postDelayed(() -> {
            boolean success = dbHelper.updateUserSubscription(userId, planName);

            dismissProcessingDialog();

            if (success) {
                currentSubscription = planName;
                updateButtonStates();

                showPurchaseSuccessDialog(planName, paymentMethod);
            } else {
                showPurchaseErrorDialog(paymentMethod);
            }
        }, 3000);
    }

    private AlertDialog processingDialog;

    private void showProcessingDialog(String paymentMethod) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Processing Payment...");
        builder.setMessage("Processing your payment via " + paymentMethod + "\n\nPlease wait...");
        builder.setCancelable(false);

        processingDialog = builder.create();
        processingDialog.show();
    }

    private void dismissProcessingDialog() {
        if (processingDialog != null && processingDialog.isShowing()) {
            processingDialog.dismiss();
        }
    }

    private void processPurchase(String planName, String price) {
        processPurchase(planName, price, "💳 Default Payment");
    }

    private void showPurchaseSuccessDialog(String planName, String paymentMethod) {
        new AlertDialog.Builder(this)
                .setTitle("🎉 Payment Successful!")
                .setMessage("Congratulations! You have successfully upgraded to the " + planName + " plan.\n\n" +
                        "Payment Method: " + paymentMethod + "\n\n" +
                        "You now have access to all " + planName + " features!")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Great!", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();

        Toast.makeText(this, "Welcome to " + planName + " plan! 🎉", Toast.LENGTH_LONG).show();
    }

    private void showPurchaseErrorDialog(String paymentMethod) {
        new AlertDialog.Builder(this)
                .setTitle("Payment Failed")
                .setMessage("Sorry, there was an error processing your payment via " + paymentMethod +
                        ".\n\nPlease try again with a different payment method or contact support.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Try Again", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void initiatePayment(String planName, String price) {
        handlePlanSelection(planName, price);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCurrentSubscription();
        updateButtonStates();
    }
}
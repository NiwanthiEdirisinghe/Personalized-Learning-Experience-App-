package com.example.personalizedlearningexperienceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {


    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText confirmEmailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText phoneNumberEditText;
    private Button createAccountButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        usernameEditText = findViewById(R.id.editTextRegisterUsername);
        emailEditText = findViewById(R.id.editTextEmail);
        confirmEmailEditText = findViewById(R.id.editTextConfirmEmail);
        passwordEditText = findViewById(R.id.editTextRegisterPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        createAccountButton = findViewById(R.id.buttonCreateAccount);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String confirmEmail = confirmEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || confirmEmail.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.equals(confirmEmail)) {
            Toast.makeText(this, "Email addresses do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, password, username, email, phoneNumber);


        long userId = dbHelper.insertUser(user);
        user.setId(userId);
        if (userId != -1) {
            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, InterestsActivity.class);
            intent.putExtra("USER_DATA", user);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        }
   }
}
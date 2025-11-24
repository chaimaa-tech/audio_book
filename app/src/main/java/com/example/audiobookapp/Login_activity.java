package com.example.audiobookapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey; // Updated import
import java.security.GeneralSecurityException;
import java.io.IOException;

public class Login_activity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Find views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Validation
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Enter username");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        // Disable inputs during "login"
        setUiEnabled(false);
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();

        // Simulate network call with a background thread
        new Thread(() -> {
            try {
                Thread.sleep(1000); // simulate network delay

                // Mock login logic
                if (username.equals("testuser") && password.equals("password123")) {
                    saveTokenSecurely("mock-token-abc-123");
                    runOnUiThread(this::navigateToBooks);
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(Login_activity.this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(Login_activity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            } finally {
                runOnUiThread(() -> setUiEnabled(true));
            }
        }).start();
    }

    private void setUiEnabled(boolean enabled) {
        etUsername.setEnabled(enabled);
        etPassword.setEnabled(enabled);
        btnLogin.setEnabled(enabled);
    }

    private void saveTokenSecurely(String token) {
        try {
            // 1. Create the MasterKey using the recommended builder
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // 2. Create EncryptedSharedPreferences using the new MasterKey object
            android.content.SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    getApplicationContext(),
                    "auth_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // 3. Save the token
            sharedPreferences.edit().putString("auth_token", token).apply();

        } catch (GeneralSecurityException | IOException e) {
            // It's better to handle these specific exceptions
            e.printStackTrace();
            runOnUiThread(() ->
                    Toast.makeText(Login_activity.this, "Failed to save token securely.", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void navigateToBooks() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}



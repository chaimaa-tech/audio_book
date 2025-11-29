package com.example.audiobookapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Login_activity extends AppCompatActivity {

    TextInputEditText etUsername, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity); // The XML you created

        // --- Link UI elements ---
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);

        // --- Login button action ---
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Simple validation
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login_activity.this,
                            "Please enter username and password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Replace this with your real authentication later
                if (username.equals("admin") && password.equals("1234")) {
                    Toast.makeText(Login_activity.this,
                            "Login successful!", Toast.LENGTH_SHORT).show();

                    // Open next screen
                    Intent i = new Intent(Login_activity.this, AudioBooks.class);
                    startActivity(i);
                    finish(); // close login screen
                }
                else {
                    Toast.makeText(Login_activity.this,
                            "Incorrect username or password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



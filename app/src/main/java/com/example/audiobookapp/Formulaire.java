package com.example.audiobookapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

public class Formulaire extends AppCompatActivity {

    ImageButton backArrow;
    EditText editNom, editPrenom, editEmail, editFeedback, editProblemes;
    RatingBar ratingBar;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire);

        // --- Find all the views from the XML layout ---
        backArrow = findViewById(R.id.back_arrow);
        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editEmail = findViewById(R.id.editEmail);
        editFeedback = findViewById(R.id.editFeedback);
        editProblemes = findViewById(R.id.editProblemes);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmit);

        // --- Set the click listener for the back arrow ---
        backArrow.setOnClickListener(v -> {
            finish(); // Closes the current activity and returns to the previous one
        });

        // --- Set the click listener for the submit button ---
        btnSubmit.setOnClickListener(v -> {
            // Get the values from the form fields
            String nom = editNom.getText().toString();
            String prenom = editPrenom.getText().toString();
            String email = editEmail.getText().toString();
            String feedback = editFeedback.getText().toString();
            String problemes = editProblemes.getText().toString();
            float note = ratingBar.getRating();

            // Create a message with the collected data
            String message = "Nom: " + nom + "\n"
                    + "Prénom: " + prenom + "\n"
                    + "Email: " + email + "\n"
                    + "Note: " + note + "\n"
                    + "Feedback: " + feedback + "\n"
                    + "Problèmes: " + problemes;

            // Display the data in a Toast message
            Toast.makeText(Formulaire.this, message, Toast.LENGTH_LONG).show();
        });
    }
}
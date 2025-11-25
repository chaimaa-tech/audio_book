
package com.example.audiobookapp;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

public class Formulaire extends AppCompatActivity {

    EditText editNom, editPrenom, editFeedback, editProblemes,editEmail;
    RatingBar ratingBar;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire);

        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editFeedback = findViewById(R.id.editFeedback);
        editProblemes = findViewById(R.id.editProblemes);
        btnSubmit = findViewById(R.id.btnSubmit);
        editEmail = findViewById(R.id.editEmail);
        ratingBar = findViewById(R.id.ratingBar);


        btnSubmit.setOnClickListener(v -> {

            String nom = editNom.getText().toString();
            String prenom = editPrenom.getText().toString();
            String feedback = editFeedback.getText().toString();
            String problemes = editProblemes.getText().toString();
            String email = editEmail.getText().toString();
            float note = ratingBar.getRating();


            // Affichage simple
            String message = "Nom : " + nom +
                    "\nPrénom : " + prenom +
                    "\nFeedback : " + feedback +
                    "\nProblèmes : " + problemes;

            Toast.makeText(Formulaire.this, message, Toast.LENGTH_LONG).show();
        });
    }
}

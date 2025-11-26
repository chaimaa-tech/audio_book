package com.example.audiobookapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.audiobookapp.Book; // Make sure to import your Book model

public class summary extends AppCompatActivity {

    // 2. Declare a variable to hold the book data
    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary); // This now correctly references your project's layout

        // 3. Get the entire Book object, not just the summary text
        currentBook = (Book) getIntent().getSerializableExtra("book");

        // Defensive check to prevent app from crashing if data is missing
        if (currentBook == null) {
            Toast.makeText(this, "Book data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Find Views ---
        TextView summaryText = findViewById(R.id.summary_text);
        ImageButton back = findViewById(R.id.backBtn);
        TextView aboutBookTab = findViewById(R.id.tabAbout);
        TextView chaptersTab = findViewById(R.id.tabChapters);

        // --- Populate Views ---
        summaryText.setText(currentBook.getSummary());

        // --- Setup Listeners ---
        back.setOnClickListener(v -> finish()); // Simply close this activity to go back

        View.OnClickListener tabListener = v -> {
            Intent intent;
            int id = v.getId();

            if (id == R.id.tabAbout) {
                // 4. To go to 'about_book', just finish this activity.
                // This assumes 'about_book' is the screen that opened 'summary'.
                finish();
                return; // Stop further execution

            } else if (id == R.id.tabChapters) {
                // 5. Navigate to the chapters activity, passing the book data FORWARD
                intent = new Intent(summary.this, chapters.class); // Use the correct class name
                intent.putExtra("book", currentBook); // Pass the whole book object

            } else {
                return; // Do nothing if another view is clicked
            }

            startActivity(intent);
        };

        aboutBookTab.setOnClickListener(tabListener);
        chaptersTab.setOnClickListener(tabListener);
    }
}



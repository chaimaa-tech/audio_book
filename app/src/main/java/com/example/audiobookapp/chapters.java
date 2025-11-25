package com.example.audiobookapp;

// 1. REMOVED incorrect import: com.example.projet.R
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiobookapp.model.Book; // Assuming you have a Book model
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class chapters extends AppCompatActivity {

    private Book currentBook;
    private List<chapter> chapterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapters);

        // --- Get Data from Intent ---
        // 2. Retrieve the book object passed from the previous activity
        currentBook = (Book) getIntent().getSerializableExtra("book");

        // Defensive check to prevent crashes
        if (currentBook == null || currentBook.getChapters() == null) {
            Toast.makeText(this, "Chapter data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        chapterList = currentBook.getChapters();

        // --- Find Views ---
        ImageButton back = findViewById(R.id.backBtn);
        TextView aboutBookTab = findViewById(R.id.tabAbout);
        TextView summaryTab = findViewById(R.id.tabSummary);
        RecyclerView chaptersRecyclerView = findViewById(R.id.chaptersRecyclerView);
        // --- Setup RecyclerView ---
        // 3. The RecyclerView is configured to display the list of chapters
        ChapterAdapter adapter = new ChapterAdapter(chapterList);
        chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chaptersRecyclerView.setAdapter(adapter);

        // --- Setup Listeners ---
        back.setOnClickListener(v -> finish()); // Go back to the previous screen

        View.OnClickListener tabListener = v -> {
            Intent intent;
            int id = v.getId();

            if (id == R.id.tabAbout) {
                // To go back to 'about_book', it's better to just finish this activity
                // if 'about_book' is the one that opened it.
                finish(); // This is simpler and avoids creating a new messy stack.
                return;

            } else if (id == R.id.tabSummary) {
                intent = new Intent(chapters.this, summary.class);
                // 4. Pass the book data FORWARD to the summary screen
                intent.putExtra("book", currentBook);

            } else {
                return;
            }

            startActivity(intent);
        };

        aboutBookTab.setOnClickListener(tabListener);
        summaryTab.setOnClickListener(tabListener);
    }
}


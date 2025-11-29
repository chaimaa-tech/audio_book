package com.example.audiobookapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.audiobookapp.model.Book;
import com.example.audiobookapp.Chapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChapterActivity extends AppCompatActivity {

    public static final String EXTRA_CHAPTER = "chapter_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter);

        // --- Get Data ---
        Book book = (Book) getIntent().getSerializableExtra("book");
        Chapter chapter = (Chapter) getIntent().getSerializableExtra(EXTRA_CHAPTER);

        if (book == null || chapter == null) {
            Toast.makeText(this, "Data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Find Views ---
        ImageButton backBtn = findViewById(R.id.back_button);
        TextView bookTitle = findViewById(R.id.book_title_text);
        TextView chapterTitle = findViewById(R.id.chapter_title_text);
        FloatingActionButton playPauseBtn = findViewById(R.id.play_pause_button);

        // --- Populate Views ---
        bookTitle.setText(book.getTitle());
        chapterTitle.setText(chapter.getTitle());

        // --- Set Listeners ---
        backBtn.setOnClickListener(v -> finish());

        playPauseBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Playing: " + chapter.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }
}
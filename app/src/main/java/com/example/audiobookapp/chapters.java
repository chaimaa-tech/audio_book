package com.example.audiobookapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiobookapp.model.Book;
import com.example.audiobookapp.Chapter;

public class chapters extends AppCompatActivity {

    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapters);

        // --- Get Data from Intent ---
        currentBook = (Book) getIntent().getSerializableExtra("book");

        if (currentBook == null) {
            Toast.makeText(this, "Book data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Find Views ---
        TextView mainTitle = findViewById(R.id.main_title);
        RecyclerView chaptersRecyclerView = findViewById(R.id.chaptersList);

        // --- Populate Views ---
        mainTitle.setText(currentBook.getTitle());

        // --- Setup RecyclerView ---
        // Pass the entire book object to the adapter
        ChapterAdapter adapter = new ChapterAdapter(currentBook);
        chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chaptersRecyclerView.setAdapter(adapter);
    }
}

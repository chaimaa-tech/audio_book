package com.example.audiobookapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.audiobookapp.model.Book;

public class about_book extends AppCompatActivity {

    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_book);

        // Retrieve the book from the intent
        currentBook = (Book) getIntent().getSerializableExtra("book");

        // Check if the book data is valid
        if (currentBook == null) {
            Toast.makeText(this, "Book data not found.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no book is found
            return;
        }

        // Find the views from the layout
        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.author);
        TextView categoryName = findViewById(R.id.categoryName);
        TextView yearName = findViewById(R.id.yearName);
        TextView tabSummary = findViewById(R.id.tabSummary);
        TextView tabChapters = findViewById(R.id.tabChapters);

        // Populate the views with the book's data
        title.setText(currentBook.getTitle());
        author.setText(currentBook.getAuthor());
        categoryName.setText(currentBook.getCategory());
        yearName.setText(String.valueOf(currentBook.getYear()));

        // Set up listeners
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        tabSummary.setOnClickListener(v -> {
            Intent intent = new Intent(about_book.this, summary.class);
            intent.putExtra("book", currentBook);
            startActivity(intent);
        });

        tabChapters.setOnClickListener(v -> {
            Intent intent = new Intent(about_book.this, chapters.class);
            intent.putExtra("book", currentBook);
            startActivity(intent);
        });
    }
}

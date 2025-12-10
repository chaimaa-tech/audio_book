package com.example.audiobookapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.audiobookapp.model.Book;

public class about_book extends AppCompatActivity {

    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_book);

        currentBook = (Book) getIntent().getSerializableExtra("book");

        if (currentBook == null || currentBook.getId() == null) {
            Toast.makeText(this, "Book data is invalid.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Find Views ---
        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.author);
        TextView categoryName = findViewById(R.id.categoryName);
        TextView yearName = findViewById(R.id.yearName);
        TextView tabChapters = findViewById(R.id.tabChapters);
        ImageView bookCover = findViewById(R.id.bookCover);

        // --- Populate Views ---
        title.setText(currentBook.getTitle());
        author.setText(currentBook.getAuthor());
        categoryName.setText(currentBook.getCategory());
        yearName.setText(String.valueOf(currentBook.getYear()));

        // Load the book cover image using Glide
        Glide.with(this)
                .load(currentBook.getCover())
                .into(bookCover);

        // --- Set Listeners ---
        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        tabChapters.setOnClickListener(v -> {
            Intent intent = new Intent(about_book.this, ChaptersActivity.class);
            intent.putExtra("book", currentBook);
            startActivity(intent);
        });
    }
}
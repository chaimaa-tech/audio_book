package com.example.audiobookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.audiobookapp.model.Book;

import java.io.Serializable;

public class about_book extends AppCompatActivity {

    // 2. Declare the book object here, but initialize it inside onCreate
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 3. Set the correct layout for this activity
        setContentView(R.layout.about_book);

        // 4. Get the book from the intent *inside* onCreate
        book = (Book) getIntent().getSerializableExtra("book");

        // Defensive check to prevent crash if book is null
        if (book == null) {
            // Optional: Show an error message and finish the activity
            // Toast.makeText(this, "Error: Book data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Find Views ---
        ImageButton back = findViewById(R.id.backBtn);
        TextView title = findViewById(R.id.title);
        ImageView book_Cover = findViewById(R.id.bookCover);
        TextView author = findViewById(R.id.author);
        TextView category = findViewById(R.id.categoryName);
        TextView year = findViewById(R.id.yearName);
        TextView summaryTab = findViewById(R.id.tabSummary);
        TextView chaptersTab = findViewById(R.id.tabChapters);

        // --- 5. Populate Views with Book Data ---
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        category.setText(book.getCategory());
        year.setText(String.valueOf(book.getYear())); // Convert int/year to String
        // To set the book cover, you'll need a library like Glide or Picasso
        // Example with Glide:
        // Glide.with(this).load(book.getCoverUrl()).into(book_Cover);


        // --- Set OnClick Listeners ---
        back.setOnClickListener(v -> finish());

        View.OnClickListener tabListener = v -> {
            Intent intent = null; // Initialize to null
            int id = v.getId();

            if (id == R.id.tabSummary) {
                intent = new Intent(about_book.this, summary.class);
                intent.putExtra("summaryText", book.getSummary());
            } else if (id == R.id.tabChapters) {
                intent = new Intent(about_book.this, chapters.class);
                intent.putExtra("chapters", (Serializable) book.getChapters());
            }

            // 6. Start the activity if the intent was created
            if (intent != null) {
                startActivity(intent);
            }
        };

        summaryTab.setOnClickListener(tabListener);
        chaptersTab.setOnClickListener(tabListener);
    }
}

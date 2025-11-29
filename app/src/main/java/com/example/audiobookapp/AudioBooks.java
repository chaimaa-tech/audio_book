package com.example.audiobookapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiobookapp.model.Book;
import com.example.audiobookapp.Chapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioBooks extends AppCompatActivity {

    private RecyclerView rvBooks;
    private BookAdapter bookAdapter;
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.audio_books);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        rvBooks = findViewById(R.id.rv_books);
        rvBooks.setLayoutManager(new GridLayoutManager(this, 2));

        // Load books
        loadDummyBooks();

        // Set adapter
        bookAdapter = new BookAdapter(this, filteredBooks);
        rvBooks.setAdapter(bookAdapter);

        // --- Setup Buttons ---
        ImageButton actionFilter = findViewById(R.id.action_filter);
        actionFilter.setOnClickListener(v -> showGenreFilter());

        ImageButton actionForm = findViewById(R.id.action_form);
        actionForm.setOnClickListener(v -> {
            // FIX: Changed to launch the correct Formulaire class
            Intent intent = new Intent(AudioBooks.this, Formulaire.class);
            startActivity(intent);
        });
    }

    private void loadDummyBooks() {

        // (title, author, category, year, summary, chapters)
        allBooks.add(new Book(
                "Book 1", "Author 1", "Adventure", 2020, "Summary 1",
                Arrays.asList(new Chapter("The Adventure Begins", "..."), new Chapter("Into the Woods", "..."))
        ));
        allBooks.add(new Book(
                "Book 2", "Author 2", "Romance", 2021, "Summary 2",
                Arrays.asList(new Chapter("First Encounter", "..."))
        ));
        allBooks.add(new Book(
                "Book 3", "Author 3", "Fantasy", 2022, "Summary 3",
                Arrays.asList(new Chapter("The Hidden Kingdom", "..."), new Chapter("A Dragon's Lair", "..."), new Chapter("The Final Battle", "..."))
        ));
        filteredBooks.addAll(allBooks);
    }

    private void showGenreFilter() {
        GenreFilterBottomSheet bottomSheet = new GenreFilterBottomSheet();
        bottomSheet.setOnFilterAppliedListener(this::applyGenreFilter);
        bottomSheet.show(getSupportFragmentManager(), "GenreFilter");
    }

    private void applyGenreFilter(List<String> selectedGenres) {
        filteredBooks.clear();
        if (selectedGenres.isEmpty()) {
            filteredBooks.addAll(allBooks);
        } else {
            for (Book book : allBooks) {
                if (selectedGenres.contains(book.getCategory())) {
                    filteredBooks.add(book);
                }
            }
        }
        bookAdapter.notifyDataSetChanged();
    }
}

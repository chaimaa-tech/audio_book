package com.example.audiobookapp;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.audiobookapp.model.Book; // Assuming you have a Book model class
import java.util.ArrayList;
import java.util.List;

public class AudioBooks extends AppCompatActivity {

    private RecyclerView rvBooks;
    private BookAdapter bookAdapter; // You'll need to create this adapter class
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.audio_books); // Corrected to match your XML file name (assuming it's res/layout/audio_books.xml)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        rvBooks = findViewById(R.id.rv_books);
        rvBooks.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns as per your XML

        // Load dummy books (replace with your actual data loading logic, e.g., from API or database)
        loadDummyBooks();

        // Set up adapter
        bookAdapter = new BookAdapter(this, filteredBooks); // Create BookAdapter class as shown below
        rvBooks.setAdapter(bookAdapter);

        // Set up filter button
        ImageButton actionFilter = findViewById(R.id.action_filter);
        actionFilter.setOnClickListener(v -> showGenreFilter());
    }

    private void loadDummyBooks() {
        // Dummy data - replace with real books
        allBooks.add(new Book("Book 1", "Author 1", "Adventure", 2020, "Summary 1", null));
        allBooks.add(new Book("Book 2", "Author 2", "Romance", 2021, "Summary 2", null));
        allBooks.add(new Book("Book 3", "Author 3", "Fantasy", 2022, "Summary 3", null));
        // Add more...
        filteredBooks.addAll(allBooks); // Initially show all
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
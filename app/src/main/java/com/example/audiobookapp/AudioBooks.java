package com.example.audiobookapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiobookapp.model.Book;

import java.util.ArrayList;
import java.util.List;

public class AudioBooks extends AppCompatActivity {

    private static final String TAG = "AudioBooksActivity";

    private RecyclerView rvBooks;
    private BookAdapter bookAdapter;
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_books);

        rvBooks = findViewById(R.id.rv_books);
        rvBooks.setLayoutManager(new GridLayoutManager(this, 2));
        
        filteredBooks = new ArrayList<>();
        bookAdapter = new BookAdapter(this, filteredBooks);
        rvBooks.setAdapter(bookAdapter);

        loadFakeBookData();

        ImageButton actionFilter = findViewById(R.id.action_filter);
        actionFilter.setOnClickListener(v -> showGenreFilter());

        ImageButton actionForm = findViewById(R.id.action_form);
        actionForm.setOnClickListener(v -> {
            Intent intent = new Intent(AudioBooks.this, Formulaire.class);
            startActivity(intent);
        });
    }

    private void loadFakeBookData() {
        Log.d(TAG, "Loading fake book data...");
        allBooks.clear();

        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 1925, "A novel about the American dream.", null, "https://covers.openlibrary.org/b/id/8264783-L.jpg");
        book1.setId("fake1");
        allBooks.add(book1);

        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, "A novel about racial injustice in the American South.", null, "https://covers.openlibrary.org/b/id/1024843-L.jpg");
        book2.setId("fake2");
        allBooks.add(book2);
        
        Book book3 = new Book("1984", "George Orwell", "Dystopian", 1949, "A novel about a totalitarian future society.", null, "https://covers.openlibrary.org/b/id/8363412-L.jpg");
        book3.setId("fake3");
        allBooks.add(book3);

        Log.d(TAG, "Loaded " + allBooks.size() + " fake books.");
        applyGenreFilter(new ArrayList<>());
    }

    private void showGenreFilter() {
        GenreFilterBottomSheet bottomSheet = new GenreFilterBottomSheet();
        bottomSheet.setOnFilterAppliedListener(this::applyGenreFilter);
        bottomSheet.show(getSupportFragmentManager(), "GenreFilter");
    }

    private void applyGenreFilter(List<String> selectedGenres) {
        filteredBooks.clear();
        if (selectedGenres == null || selectedGenres.isEmpty()) {
            filteredBooks.addAll(allBooks);
        } else {
            for (Book book : allBooks) {
                if (selectedGenres.contains(book.getCategory())) {
                    filteredBooks.add(book);
                }
            }
        }
        Log.d(TAG, "Adapter updated. Final item count: " + filteredBooks.size());
        if (bookAdapter != null) {
            bookAdapter.notifyDataSetChanged();
        }
    }
}

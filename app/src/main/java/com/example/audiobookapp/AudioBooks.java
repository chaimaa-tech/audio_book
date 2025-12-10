package com.example.audiobookapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiobookapp.model.Book;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AudioBooks extends AppCompatActivity {

    private static final String TAG = "AudioBooksActivity";

    private RecyclerView rvBooks;
    private BookAdapter bookAdapter;
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_books);

        db = FirebaseFirestore.getInstance();
        rvBooks = findViewById(R.id.rv_books);
        rvBooks.setLayoutManager(new GridLayoutManager(this, 2));
        bookAdapter = new BookAdapter(this, filteredBooks);
        rvBooks.setAdapter(bookAdapter);

        fetchBooksFromFirestore();

        ImageButton actionFilter = findViewById(R.id.action_filter);
        actionFilter.setOnClickListener(v -> showGenreFilter());

        ImageButton actionForm = findViewById(R.id.action_form);
        actionForm.setOnClickListener(v -> {
            Intent intent = new Intent(AudioBooks.this, Formulaire.class);
            startActivity(intent);
        });
    }

    private void fetchBooksFromFirestore() {
        Log.d(TAG, "Fetching books from Firestore...");
        db.collection("Books")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firestore task successful. Found " + task.getResult().size() + " documents.");
                        allBooks.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Book book = document.toObject(Book.class);
                            // FIX: Manually set the document ID on the book object
                            book.setId(document.getId());

                            if (book.getTitle() != null) {
                                allBooks.add(book);
                                Log.d(TAG, "\tSUCCESS: Converted book: " + book.getTitle() + " with ID: " + book.getId());
                            } else {
                                Log.w(TAG, "\tWARNING: Failed to convert document to Book object. Check fields.");
                            }
                        }
                        applyGenreFilter(new ArrayList<>());
                    } else {
                        Log.e(TAG, "Firestore task failed: ", task.getException());
                    }
                });
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
        bookAdapter.notifyDataSetChanged();
    }
}
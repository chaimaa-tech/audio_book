package com.example.audiobookapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiobookapp.ChapterAdapter;
import com.example.audiobookapp.model.Book;
import com.example.audiobookapp.model.Chapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChaptersActivity extends AppCompatActivity {

    private static final String TAG = "ChaptersActivity";
    private RecyclerView chaptersRecyclerView;
    private ChapterAdapter chapterAdapter;
    private List<Chapter> chapterList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        db = FirebaseFirestore.getInstance();
        Book book = (Book) getIntent().getSerializableExtra("book");

        if (book == null || book.getId() == null) {
            Toast.makeText(this, "Book data is missing.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Find Views ---
        ImageButton backButton = findViewById(R.id.backBtn);
        TextView bookTitleTextView = findViewById(R.id.chaptersTitle);

        // --- Populate Views ---
        bookTitleTextView.setText(book.getTitle());

        // --- Setup Listeners ---
        backButton.setOnClickListener(v -> finish());

        // --- Setup RecyclerView ---
        chaptersRecyclerView = findViewById(R.id.rv_chapters);
        chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chapterList = new ArrayList<>();
        chapterAdapter = new ChapterAdapter(this, chapterList, book);
        chaptersRecyclerView.setAdapter(chapterAdapter);

        // --- Fetch Chapters with Real-time Updates ---
        fetchChaptersInRealTime(book.getId());
    }

    private void fetchChaptersInRealTime(String bookId) {
        db.collection("Books").document(bookId).collection("chapters")
            .orderBy("title") 
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        Toast.makeText(ChaptersActivity.this, "Failed to load chapters. Check Firestore index.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (snapshots != null) {
                        chapterList.clear();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Chapter chapter = doc.toObject(Chapter.class);
                            chapterList.add(chapter);
                        }
                        Log.d(TAG, "Chapters updated: " + chapterList.size());
                        chapterAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
    }
}

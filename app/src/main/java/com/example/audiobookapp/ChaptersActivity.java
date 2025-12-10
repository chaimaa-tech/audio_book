package com.example.audiobookapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiobookapp.model.Book;
import com.example.audiobookapp.model.Chapter;

import java.util.ArrayList;
import java.util.List;

public class ChaptersActivity extends AppCompatActivity {

    private static final String TAG = "ChaptersActivity";
    private RecyclerView chaptersRecyclerView;
    private ChapterAdapter chapterAdapter;
    private List<Chapter> chapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        Book book = (Book) getIntent().getSerializableExtra("book");

        if (book == null) { 
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

        // --- Load Fake Data ---
        loadFakeChapterData();
    }

    private void loadFakeChapterData() {
        chapterList.clear();

        // Chapter 1: Huckleberry Finn
        Chapter chapter1 = new Chapter();
        chapter1.setTitle("Chapter 1: Huckleberry Finn");
        chapter1.setNumber(1);
        chapter1.setAudioURL("https://etc.usf.edu/lit2go/audio/mp3/the-adventures-of-huckleberry-finn-001-chapter-i.462.mp3");
        chapterList.add(chapter1);

        // Chapter 2: The Call of the Wild
        Chapter chapter2 = new Chapter();
        chapter2.setTitle("Chapter 1: Into the Primitive");
        chapter2.setNumber(2);
        chapter2.setAudioURL("https://etc.usf.edu/lit2go/audio/mp3/the-call-of-the-wild-001-chapter-1-into-the-primitive.986.mp3");
        chapterList.add(chapter2);

        // Chapter 3: The Scarlet Letter
        Chapter chapter3 = new Chapter();
        chapter3.setTitle("The Custom-House: Introductory");
        chapter3.setNumber(3);
        chapter3.setAudioURL("https://etc.usf.edu/lit2go/audio/mp3/the-scarlet-letter-001-the-custom-house-introductory.112.mp3");
        chapterList.add(chapter3);

        Log.d(TAG, "Loaded fake audiobook chapters. Total chapters: " + chapterList.size());
        chapterAdapter.notifyDataSetChanged();
    }
}

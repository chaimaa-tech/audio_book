package com.example.audiobookapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.audiobookapp.model.Book;
import com.example.audiobookapp.model.Chapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    private FloatingActionButton playPauseBtn;
    private ImageButton prevChapterButton;
    private ImageButton nextChapterButton;
    private ImageButton seekBackwardButton;
    private ImageButton seekForwardButton;

    private TextView bookTitleTextView;
    private TextView chapterTitleTextView;
    private ImageView coverArtImageView;

    private List<Chapter> chapterList;
    private int currentChapterIndex;

    private boolean isPlaying = false;

    private static final int NOTIFICATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter);

        // --- Get Data from Intent ---
        Book book = (Book) getIntent().getSerializableExtra("book");
        chapterList = (List<Chapter>) getIntent().getSerializableExtra("chapters");
        currentChapterIndex = getIntent().getIntExtra("position", -1);

        if (book == null || chapterList == null || chapterList.isEmpty() || currentChapterIndex == -1) {
            Toast.makeText(this, "Chapter data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- Find Views ---
        ImageButton backBtn = findViewById(R.id.back_button);
        bookTitleTextView = findViewById(R.id.book_title_text);
        chapterTitleTextView = findViewById(R.id.chapter_title_text);
        coverArtImageView = findViewById(R.id.cover_art_image);
        playPauseBtn = findViewById(R.id.play_pause_button);
        prevChapterButton = findViewById(R.id.previous_track_button);
        nextChapterButton = findViewById(R.id.next_track_button);
        seekBackwardButton = findViewById(R.id.rewind_button);
        seekForwardButton = findViewById(R.id.forward_button);

        // --- Request Permission ---
        requestNotificationPermission();

        // --- Populate Initial UI ---
        updateChapterUI();

        // --- Set Listeners ---
        backBtn.setOnClickListener(v -> finish());

        playPauseBtn.setOnClickListener(v -> {
            if (isPlaying) {
                pauseAudioService();
            } else {
                playCurrentChapter();
            }
        });

        nextChapterButton.setOnClickListener(v -> playNextChapter());
        prevChapterButton.setOnClickListener(v -> playPreviousChapter());

        seekForwardButton.setOnClickListener(v -> seekAudioService(AudioService.ACTION_SEEK_FORWARD));
        seekBackwardButton.setOnClickListener(v -> seekAudioService(AudioService.ACTION_SEEK_BACKWARD));
    }

    private void playCurrentChapter() {
        Chapter chapter = chapterList.get(currentChapterIndex);
        if (chapter.getAudioURL() != null && !chapter.getAudioURL().isEmpty()) {
            startAudioService(chapter.getAudioURL());
            isPlaying = true;
            updatePlayPauseButton();
        } else {
            Toast.makeText(this, "Audio URL is missing.", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseAudioService() {
        Intent serviceIntent = new Intent(this, AudioService.class);
        serviceIntent.setAction(AudioService.ACTION_PAUSE_PLAYBACK);
        startService(serviceIntent);
        isPlaying = false;
        updatePlayPauseButton();
    }

    private void playNextChapter() {
        if (currentChapterIndex < chapterList.size() - 1) {
            currentChapterIndex++;
            updateChapterUI();
            playCurrentChapter();
        } else {
            Toast.makeText(this, "This is the last chapter.", Toast.LENGTH_SHORT).show();
        }
    }

    private void playPreviousChapter() {
        if (currentChapterIndex > 0) {
            currentChapterIndex--;
            updateChapterUI();
            playCurrentChapter();
        } else {
            Toast.makeText(this, "This is the first chapter.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateChapterUI() {
        Book book = (Book) getIntent().getSerializableExtra("book");
        Chapter chapter = chapterList.get(currentChapterIndex);
        bookTitleTextView.setText(book.getTitle());
        chapterTitleTextView.setText(chapter.getTitle());

        Glide.with(this)
                .load(book.getCover())
                .into(coverArtImageView);
    }

    private void updatePlayPauseButton() {
        if (isPlaying) {
            playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void startAudioService(String audioUrl) {
        Intent serviceIntent = new Intent(this, AudioService.class);
        serviceIntent.setAction(AudioService.ACTION_START_PLAYBACK);
        serviceIntent.putExtra(AudioService.EXTRA_AUDIO_URL, audioUrl);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void seekAudioService(String action) {
        if (isPlaying) {
            Intent serviceIntent = new Intent(this, AudioService.class);
            serviceIntent.setAction(action);
            startService(serviceIntent);
        } else {
            Toast.makeText(this, "Start playback first.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The service will continue to run in the background
    }

    private void stopAudioService() {
        Intent serviceIntent = new Intent(this, AudioService.class);
        stopService(serviceIntent);
        isPlaying = false;
        updatePlayPauseButton();
    }

    // --- Notification Permission Handling ---
    private void requestNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ChapterActivity", "Notification permission granted.");
            }
            else {
                Log.w("ChapterActivity", "Notification permission denied.");
                Toast.makeText(this, "Notification permission is required for background playback.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

package com.example.audiobookapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.audiobookapp.model.Book;
import com.example.audiobookapp.model.Chapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChapterActivity extends AppCompatActivity {

    private FloatingActionButton playPauseBtn;
    private ImageButton prevChapterButton;
    private ImageButton nextChapterButton;
    private ImageButton seekBackwardButton;
    private ImageButton seekForwardButton;

    private String CHAPTER_URL;
    private static final int NOTIFICATION_PERMISSION_CODE = 100;   // Constante pour la gestion des permissions

    private boolean isPlaying = false;  // État local de l'activité

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter);

        // --- Get Data ---
        Book book = (Book) getIntent().getSerializableExtra("book");
        Chapter chapter = (Chapter) getIntent().getSerializableExtra("chapter");

        if (book == null || chapter == null) {
            Toast.makeText(this, "Data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Extraction de l'URL DYNAMIQUE
        CHAPTER_URL = chapter.getAudioURL(); // FIX: Use the correct getter
        if (CHAPTER_URL == null || CHAPTER_URL.isEmpty()){
            Toast.makeText(this, "Erreur: URL audio du chapitre non trouvée.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // --- Find Views ---
        ImageButton backBtn = findViewById(R.id.back_button);
        TextView bookTitle = findViewById(R.id.book_title_text);
        TextView chapterTitle = findViewById(R.id.chapter_title_text);


        // --- Populate Views ---
        bookTitle.setText(book.getTitle());
        chapterTitle.setText(chapter.getTitle());

        // --- Set Listeners ---
        backBtn.setOnClickListener(v -> finish());

        //__________Audio Service_________________

        // 1- Audio Service Buttons

        playPauseBtn = findViewById(R.id.play_pause_button);
        prevChapterButton = findViewById(R.id.previous_track_button);
        nextChapterButton = findViewById(R.id.next_track_button);
        seekBackwardButton = findViewById(R.id.rewind_button);
        seekForwardButton = findViewById(R.id.forward_button);


        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseAudioService();
                    Toast.makeText(ChapterActivity.this, "Lecture mise en pause", Toast.LENGTH_SHORT).show();
                } else {
                    startAudioService(CHAPTER_URL);
                }
                isPlaying = !isPlaying;
                updatePlayPauseButton();
            }
        });
        // Navigation (Avancer 10s / Reculer 10s)
        seekBackwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    seekAudioService(AudioService.ACTION_SEEK_BACKWARD);
                    Toast.makeText(ChapterActivity.this, "Reculé de 10 secondes", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ChapterActivity.this, "Démarrez d'abord la lecture.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    seekAudioService(AudioService.ACTION_SEEK_FORWARD);
                    Toast.makeText(ChapterActivity.this, "Avancé de 10 secondes", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChapterActivity.this, "Démarrez d'abord la lecture.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Changement de Chapitre

        // Chapitre Précedent
        prevChapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekAudioService(AudioService.ACTION_PREVIOUS_CHAPTER);
                isPlaying = false;
                updatePlayPauseButton();
                Toast.makeText(ChapterActivity.this, "Chapitre Précédent (Lecture arrêtée)", Toast.LENGTH_SHORT).show();

            }
        });
        // Chapitre Suivant
        nextChapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekAudioService(AudioService.ACTION_NEXT_CHAPTER);
                isPlaying = false;
                updatePlayPauseButton();
                Toast.makeText(ChapterActivity.this, "Chapitre Suivant (Lecture arrêtée)", Toast.LENGTH_SHORT).show();

            }
        });
    }
    // --- Fonctions de Mise à Jour UI ---

    //Met à jour le texte et la couleur du bouton Play/Pause
    private void updatePlayPauseButton(){
        if (isPlaying){
            playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
            playPauseBtn.setContentDescription("Bouton Pause");
        }
        else{
            playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
            playPauseBtn.setContentDescription("Bouton Play");
        }
    }
    // --- Fonctions d'Envoi des Commandes au Service  ---
    private void startAudioService(String audioUrl){
        Intent serviceIntent = new Intent(this, AudioService.class);
        serviceIntent.setAction(AudioService.ACTION_START_PLAYBACK);
        serviceIntent.putExtra(AudioService.EXTRA_AUDIO_URL,audioUrl);

        ContextCompat.startForegroundService(this,serviceIntent);
    }

    private void pauseAudioService(){
        Intent serviceIntent = new Intent(this, AudioService.class);
        serviceIntent.setAction(AudioService.ACTION_PAUSE_PLAYBACK);
        startService(serviceIntent);
    }

    private void stopAudioService(){
        Intent serviceIntent = new Intent(this, AudioService.class);
        serviceIntent.setAction(AudioService.ACTION_STOP_PLAYBACK);
        stopService(serviceIntent);

        isPlaying = false;
        updatePlayPauseButton();
    }

    private void seekAudioService(String action){
        Intent serviceIntent = new Intent(this, AudioService.class);
        serviceIntent.setAction(action);
        startService(serviceIntent);
    }

    // Gestion de Permission
    // verif

    private void requestNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("AudioPlayerActivity", "Permission de notification accordée.");
            }
            else {
                Log.w("AudioPlayerActivity", "Permission de notification refusée.");
            }
        }

    }


}

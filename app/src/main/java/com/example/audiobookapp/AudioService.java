package com.example.audiobookapp;

import android.app.PendingIntent;
import android.app.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.media.MediaPlayer;

import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;


public class AudioService extends Service implements MediaPlayer.OnPreparedListener {

    // Constantes d'action (commandes) que le Service peut recevoir
    public static final String ACTION_START_PLAYBACK = "ACTION_START_PLAYBACK"; // Démarrer ou reprendre
    public static final String ACTION_PAUSE_PLAYBACK = "ACTION_PAUSE_PLAYBACK"; // Mettre en pause
    public static final String ACTION_STOP_PLAYBACK = "ACTION_STOP_PLAYBACK";   // Arrêter et libérer les ressources
    public static final String ACTION_SEEK_FORWARD = "ACTION_SEEK_FORWARD";    // Avancer de quelques secondes
    public static final String ACTION_SEEK_BACKWARD = "ACTION_SEEK_BACKWARD";   // Reculer de quelques secondes
    public static final String ACTION_NEXT_CHAPTER = "ACTION_NEXT_CHAPTER";    // Passer au chapitre suivant
    public static final String ACTION_PREVIOUS_CHAPTER = "ACTION_PREVIOUS_CHAPTER"; // Revenir au chapitre précédent

    public static final String NOTIFICATION_CHANNEL_ID = "AudioBookChannel";
    public static final String EXTRA_AUDIO_URL = "EXTRA_AUDIO_URL";
    private static final int NOTIFICATION_ID = 101;
    private static final int SEEK_DURATION_MS = 10000; // 10 secondes pour les sauts

    private MediaPlayer mediaPlayer;
    private String requestedSourceUrl = null;
    private String activeSourceUrl = null;
    private boolean isPrepared = false; // Indique si le MediaPlayer a fini de charger la source


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Log.d("AudioService", "Service créé.");
    }
    // fonction qui reçoit toutes les actions (PLAY, PAUSE, SEEK, etc.) et les achemine :

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            String url = intent.getStringExtra(EXTRA_AUDIO_URL);

            if (url != null && !url.isEmpty()) {
                requestedSourceUrl = url;
            }
            if (action != null) {
                switch (action) {
                    case ACTION_START_PLAYBACK:
                        startPlayback();
                        break;
                    case ACTION_PAUSE_PLAYBACK:
                        pausePlayback();
                        break;
                    case ACTION_STOP_PLAYBACK:
                        stopPlayback();
                        break;
                    case ACTION_SEEK_FORWARD:
                        seekForward();
                        break;
                    case ACTION_SEEK_BACKWARD:
                        seekBackward();
                        break;
                    case ACTION_NEXT_CHAPTER:
                        handleNextChapter();
                        break;
                    case ACTION_PREVIOUS_CHAPTER:
                        handlePreviousChapter();
                        break;
                }
            }
        }
        // verif
        return START_STICKY;
    }

    // Démarre ou reprend la lecture audio:
    private void startPlayback() {
        // Condition de re-préparation: mediaPlayer est null OU la source a changé
        if (mediaPlayer == null || (requestedSourceUrl != null && !requestedSourceUrl.equals(activeSourceUrl))) {

            if (mediaPlayer != null) mediaPlayer.release();

            isPrepared = false;
            mediaPlayer = new MediaPlayer();

            // Écouteur OnPreparedListener (utilise le "this" du service)
            mediaPlayer.setOnPreparedListener(AudioService.this);

            // NOUVELLE CORRECTION : Utilisation des références de méthode (this::methode)
            // C'est la forme la plus courte et propre pour déléguer à la méthode déjà implémentée.
            mediaPlayer.setOnErrorListener(this::onError);
            mediaPlayer.setOnCompletionListener(this::onCompletion);

            try {
                if (requestedSourceUrl == null) {
                    Log.e("AudioService", "URL audio non définie.");
                    return;
                }

                Log.d("AudioService", "Tentative de chargement de la source: " + requestedSourceUrl);
                mediaPlayer.setDataSource(requestedSourceUrl);
                // Mettre à jour l'URL active après avoir configuré la source
                activeSourceUrl = requestedSourceUrl;

                mediaPlayer.prepareAsync();

                startForeground(NOTIFICATION_ID, buildNotification(true));

            } catch (IOException e) {
                Log.e("AudioService", "IOException lors de la configuration ou preparation: " + e.getMessage(), e);
                stopSelf();
            } catch (IllegalArgumentException e) {
                Log.e("AudioService", "IllegalArgumentException (mauvaise URL/format): " + e.getMessage(), e);
                stopSelf();
            }
        }
        // Logique de REPRISE (si déjà préparé et en pause)
        else if (isPrepared && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            startForeground(NOTIFICATION_ID, buildNotification(true));
            Log.d("AudioService", "Lecture reprise (Resumed).");
        }
    }
    // Met en pause la lecture audio.
    private void pausePlayback(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            startForeground(NOTIFICATION_ID, buildNotification(false));
            Log.d("AudioService", "Lecture mise en pause.");
        }
    }
    // Avance la lecture de 10 secondes.
    private void seekForward(){
        if (mediaPlayer != null && isPrepared){
            int currentPosition = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            int newPosition = Math.min(duration, currentPosition + SEEK_DURATION_MS);
            mediaPlayer.seekTo(newPosition);
            Log.d("AudioService", "Avancé à : " + newPosition);
        }
    }
    // Recule la lecture de 10 secondes.
    private void seekBackward() {
        if (mediaPlayer != null && isPrepared) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int newPosition = Math.max(0, currentPosition - SEEK_DURATION_MS);
            mediaPlayer.seekTo(newPosition);
            Log.d("AudioService", "Reculé à : " + newPosition);
        }
    }
    // Passage au chapitre suivant.
    private void handleNextChapter(){
        Log.d("AudioService", "Passage au chapitre suivant (Arrêt pour l'exemple).");
        stopPlayback();
    }
    private void handlePreviousChapter() {
        Log.d("AudioService", "Retour au chapitre précédent (Arrêt pour l'exemple).");
        stopPlayback();
    }

    // arreter lecture + libère ressources
    private void stopPlayback(){
        if (mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            isPrepared = false;
            mediaPlayer = null;
            activeSourceUrl = null; // Réinitialiser l'URL active
        }
        stopForeground(true);
        stopSelf();
    }

    // Callbacks du MediaPlayer
    // Appelé lorsque MediaPlayer a terminé de se préparer (chargement du fichier)

    //Démarre la lecture effective.
    @Override
    public void onPrepared(@NonNull MediaPlayer mp){
        isPrepared = true;
        Log.d("AudioService", "MediaPlayer prêt. Démarrage de la lecture.");
        mp.start();
        startForeground(NOTIFICATION_ID, buildNotification(true));
    }

    // Description: Appelé lorsque la lecture est complétée.
    // Rôle: Appelle stopPlayback().
    public void onCompletion(MediaPlayer mp){
        Log.d("AudioService", "Lecture du chapitre terminée.");
        stopPlayback();
    }

    //Description: Appelé en cas d'erreur lors de la lecture.
    //Rôle: Journalise l'erreur et arrête le service.

    public boolean onError(MediaPlayer mp, int what, int extra){
        Log.e("AudioService", "Erreur MediaPlayer: what=" + what + ", extra=" + extra);
        stopPlayback();
        return true;
    }

    // --- Gestion de la Notification ---


    // Description: Crée et retourne l'objet Notification pour le Service de premier plan.
    // Rôle: Construit l'UI de la notification avec les boutons de contrôle (via PendingIntent).

    private Notification buildNotification(boolean isPlaying){
        Intent intent = new Intent(this, ChapterActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Audiobook en cours")
                .setContentText("Lecture de: " + (activeSourceUrl != null ? activeSourceUrl.substring(activeSourceUrl.lastIndexOf('/') + 1) : "Chapitre inconnu"))
                .setSmallIcon(isPlaying ? android.R.drawable.ic_media_play : android.R.drawable.ic_media_pause)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        // Action Précédent (Contrôle 1)
        Intent prevIntent = new Intent(this, AudioService.class);
        prevIntent.setAction(ACTION_PREVIOUS_CHAPTER);
        PendingIntent prevPendingIntent = PendingIntent.getService(this, 3, prevIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.addAction(android.R.drawable.ic_media_previous, "Précédent", prevPendingIntent);

        // Action Play/Pause (Contrôle 2, qui change selon l'état isPlaying)
        Intent playPauseIntent = new Intent(this, AudioService.class);
        if (isPlaying) {
            playPauseIntent.setAction(ACTION_PAUSE_PLAYBACK);
            PendingIntent pausePendingIntent = PendingIntent.getService(this, 2, playPauseIntent, PendingIntent.FLAG_IMMUTABLE);
            builder.addAction(android.R.drawable.ic_media_pause, "Pause", pausePendingIntent);
        } else {
            playPauseIntent.setAction(ACTION_START_PLAYBACK);
            PendingIntent resumePendingIntent = PendingIntent.getService(this, 2, playPauseIntent, PendingIntent.FLAG_IMMUTABLE);
            builder.addAction(android.R.drawable.ic_media_play, "Play", resumePendingIntent);
        }

        // Action Suivant (Contrôle 3)
        Intent nextIntent = new Intent(this, AudioService.class);
        nextIntent.setAction(ACTION_NEXT_CHAPTER);
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 4, nextIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.addAction(android.R.drawable.ic_media_next, "Suivant", nextPendingIntent);

        return builder.build();
    }

    // Description: Crée le canal de notification (obligatoire depuis Android Oreo).
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Lecture Audio",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
    // Non utilisé dans ce cas (communication par Intent)
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Méthode utilitaire interne pour vérifier la source du MediaPlayer
    private String getMediaPlayerSource() {
        return requestedSourceUrl;
    }



}

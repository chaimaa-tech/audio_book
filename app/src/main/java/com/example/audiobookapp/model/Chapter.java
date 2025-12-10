package com.example.audiobookapp.model;

import java.io.Serializable;

public class Chapter implements Serializable {
    private String title;
    private String audioURL;
    private int number;

    // A no-argument constructor is required for Firestore data mapping
    public Chapter() {}

    // --- Getters ---
    public String getTitle() {
        return title;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public int getNumber() {
        return number;
    }

    // --- Setters ---
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

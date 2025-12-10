package com.example.audiobookapp.model;

import com.google.firebase.firestore.Exclude;
import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    @Exclude // Exclude from Firestore mapping since we set it manually
    public String id;

    // FIX: Fields must be public for Firestore data mapping
    public String title;
    public String author;
    public String category;
    public String summary;
    public int year;
    public List<Chapter> chapters;
    public String cover; // Added for the cover image URL

    // IMPORTANT: A no-argument constructor is required for Firestore data mapping
    public Book() {}

    public Book(String title, String author, String category, int year, String summary, List<Chapter> chapters, String cover) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
        this.summary = summary;
        this.chapters = chapters;
        this.cover = cover;
    }

    // Getter and Setter for the ID
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    public String getSummary() {
        return summary;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public String getCover() {
        return cover;
    }
}

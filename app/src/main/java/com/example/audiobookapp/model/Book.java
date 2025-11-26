package com.example.audiobookapp.model;

import com.example.audiobookapp.chapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {

    private String title;
    private String author;
    private String category;        // e.g., "Adventure", "Romance", "Fantasy"...
    private int year;
    private String summary;
    private List<chapter> chapters; // List of chapters (your existing chapter class)
    private String coverUrl;        // Optional: if you want to load images from URL later

    // Required empty constructor for Serializable
    public Book() {
    }

    // Constructor with all fields (recommended)
    public Book(String title, String author, String category, int year, String summary, List<chapter> chapters) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
        this.summary = summary;
        this.chapters = chapters != null ? chapters : new ArrayList<>();
    }

    // Constructor without chapters (for quick testing)
    public Book(String title, String author, String category, int year, String summary) {
        this(title, author, category, year, summary, new ArrayList<>());
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getYear() { return year; }
    public String getSummary() { return summary; }
    public List<chapter> getChapters() { return chapters; }
    public String getCoverUrl() { return coverUrl; }

    // Setters (optional, useful when loading from JSON/database)
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setYear(int year) { this.year = year; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setChapters(List<chapter> chapters) { this.chapters = chapters; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}
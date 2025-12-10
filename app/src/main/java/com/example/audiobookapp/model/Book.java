package com.example.audiobookapp.model;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    
    public String id;
    public String title;
    public String author;
    public String category;
    public String summary;
    public int year;
    public List<Chapter> chapters;
    public String cover;

    // A no-argument constructor is required for Serializable
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

    // Getters and Setters
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

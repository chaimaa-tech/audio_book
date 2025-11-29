package com.example.audiobookapp.model;

import com.example.audiobookapp.Chapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {

    private String title;
    private String author;
    private String category;
    private int year;
    private String summary;
    private List<Chapter> chapters;
    private String coverUrl;

    public Book() {
    }

    // THIS CONSTRUCTOR IS NOW CORRECTED
    public Book(String title, String author, String category, int year, String summary, List<Chapter> chapters) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
        this.summary = summary;
        // THE FIX: Ensure the passed chapter list is assigned to the Book object.
        this.chapters = chapters;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getYear() { return year; }
    public String getSummary() { return summary; }
    public List<Chapter> getChapters() { return chapters; }
    public String getCoverUrl() { return coverUrl; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setYear(int year) { this.year = year; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setChapters(List<Chapter> chapters) { this.chapters = chapters; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}
package com.example.audiobookapp;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    String title;
    String author;
    String category;
    String summary;
    int year;
    List<Chapter> chapters;

    public Book  (String title, String author, String category, String summary, int year, List<Chapter> chapters){
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
        this.summary = summary;
        this.chapters = chapters;

    }
    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    public String getSummary() {
        return summary;
    }

    public int getYear() {
        return year;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }
}

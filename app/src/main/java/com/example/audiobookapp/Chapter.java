package com.example.audiobookapp;



import java.io.Serializable;

public class Chapter implements Serializable {
    public String title;
    public String content;
    private String audio;

    public Chapter(String title, String content,String audioUrl) {
        this.title = title;
        this.content = content;
        this.audio= audioUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    public String getUrl() {
        return audio;
    }
}
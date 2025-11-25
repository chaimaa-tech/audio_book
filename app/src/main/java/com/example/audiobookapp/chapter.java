package com.example.audiobookapp;

import java.io.Serializable;

public class chapter implements Serializable {
    public String title;
    public String content;

    public chapter(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

package com.example.cnpm_lt_da_ta;

import java.io.Serializable;

public class Lesson implements Serializable {
    private String id; // ID của bài học
    private String title;
    private String type;
    private String content;

    // Constructor đầy đủ
    public Lesson(String id, String title, String type, String content) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.content = content;
    }

    // Constructor không có ID (dùng khi thêm bài học mới)
    public Lesson(String title, String type, String content) {
        this.title = title;
        this.type = type;
        this.content = content;
    }


    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
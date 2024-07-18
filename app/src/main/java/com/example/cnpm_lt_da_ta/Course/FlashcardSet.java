package com.example.cnpm_lt_da_ta.Course;

public class FlashcardSet {
    private int id;
    private String name;
    private int courseId;

    // Constructor
    public FlashcardSet(int id, String name, int courseId) {
        this.id = id;
        this.name = name;
        this.courseId = courseId;
    }

    public FlashcardSet( String name, int courseId)
    {
        this.name = name;
        this.courseId = courseId;
    }
    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCourseId() {
        return courseId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}

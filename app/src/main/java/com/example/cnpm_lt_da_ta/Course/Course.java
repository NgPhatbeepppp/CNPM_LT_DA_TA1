package com.example.cnpm_lt_da_ta.Course;

public class Course {
    private int id;
    private String name;
    private String description;
    private String image;
    private int popularity;
    private int isNew;

    // Constructor
    public Course(int id, String name, String description, String image, int popularity, int isNew) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.popularity = popularity;
        this.isNew = isNew;
    }
    public Course( String name, String description, String image, int popularity, int isNew)
    {
        this.name = name;
        this.description = description;
        this.image = image;
        this.popularity = popularity;
        this.isNew = isNew;
    }


    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getIsNew() {
        return isNew;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}

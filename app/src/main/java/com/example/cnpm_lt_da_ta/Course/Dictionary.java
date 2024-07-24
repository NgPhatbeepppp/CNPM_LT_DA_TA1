package com.example.cnpm_lt_da_ta.Course;

public class Dictionary {
    private int id;
    private String word;
    private String meaning;
    private String pronunciation;
    private String type; // Loại từ (danh từ, động từ, tính từ,...)
    private String example; // Câu ví dụ

    // Constructors
    public Dictionary(int id, String word, String meaning, String pronunciation, String type, String example) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
        this.type = type;
        this.example = example;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getType() {
        return type;
    }

    public String getExample() {
        return example;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExample(String example) {
        this.example = example;
    }
}

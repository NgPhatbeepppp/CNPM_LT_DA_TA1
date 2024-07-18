package com.example.cnpm_lt_da_ta.Course;
import android.os.Parcel;
import android.os.Parcelable;
public class Flashcard implements Parcelable {
    private int id;
    private String word;
    private String meaning;
    private String pronunciation;
    private String image;
    private int flashcardSetId;

    // Constructor
    public Flashcard(int id, String word, String meaning, String pronunciation, String image, int flashcardSetId) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
        this.image = image;
        this.flashcardSetId = flashcardSetId;
    }
    public Flashcard( String word, String meaning, String pronunciation, String image, int flashcardSetId) {
        this.word = word;
        this.meaning = meaning;
        this.pronunciation = pronunciation;
        this.image = image;
        this.flashcardSetId = flashcardSetId;
    }
    public Flashcard(Parcel in) {
        id = in.readInt();
        word = in.readString();
        meaning = in.readString();
        pronunciation = in.readString();
        image = in.readString();
        flashcardSetId = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(word);
        dest.writeString(meaning);
        dest.writeString(pronunciation);
        dest.writeString(image);
        dest.writeInt(flashcardSetId);
    }

    public static final Parcelable.Creator<Flashcard> CREATOR = new Parcelable.Creator<Flashcard>() {
        @Override
        public Flashcard createFromParcel(Parcel in) {
            return new Flashcard(in);
        }

        @Override
        public Flashcard[] newArray(int size) {
            return new Flashcard[size];
        }
    };
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

    public String getImage() {
        return image;
    }

    public int getFlashcardSetId() {
        return flashcardSetId;
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

    public void setImage(String image) {
        this.image = image;
    }

    public void setFlashcardSetId(int flashcardSetId) {
        this.flashcardSetId = flashcardSetId;
    }
}

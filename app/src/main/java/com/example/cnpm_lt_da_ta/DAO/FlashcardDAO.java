package com.example.cnpm_lt_da_ta.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cnpm_lt_da_ta.Course.FlashcardStudyActivity;
import com.example.cnpm_lt_da_ta.DatabaseHelper;
import com.example.cnpm_lt_da_ta.Course.Flashcard;

import java.util.ArrayList;
import java.util.List;

public class FlashcardDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public FlashcardDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Flashcard> getFlashcardsBySetId(int flashcardSetId) {
        List<Flashcard> flashcards = new ArrayList<>();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FLASHCARD,
                null,
                DatabaseHelper.COLUMN_FLASHCARD_SET_ID + " = ?",
                new String[]{String.valueOf(flashcardSetId)},
                null, null, null
        );
        while (cursor.moveToNext()) {
            @SuppressLint("Range") Flashcard flashcard = new Flashcard(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_WORD)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_MEANING)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_PRONUNCIATION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_IMAGE)),
                    flashcardSetId // Không cần lấy lại flashcardSetId vì đã lọc theo nó
            );
            flashcards.add(flashcard);
        }
        cursor.close();
        return flashcards;
    }

    public Flashcard getFlashcardById(int id) {
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FLASHCARD,
                null,
                DatabaseHelper.COLUMN_FLASHCARD_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") Flashcard flashcard = new Flashcard(
                    id, // Không cần lấy lại id vì đã lọc theo nó
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_WORD)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_MEANING)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_PRONUNCIATION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_IMAGE)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARD_SET_ID))
            );
            cursor.close();
            return flashcard;
        } else {
            cursor.close();
            return null;
        }
    }

    public void insertFlashcard(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FLASHCARD_WORD, flashcard.getWord());
        values.put(DatabaseHelper.COLUMN_FLASHCARD_MEANING, flashcard.getMeaning());
        values.put(DatabaseHelper.COLUMN_FLASHCARD_PRONUNCIATION, flashcard.getPronunciation());
        values.put(DatabaseHelper.COLUMN_FLASHCARD_IMAGE, flashcard.getImage());
        values.put(DatabaseHelper.COLUMN_FLASHCARD_SET_ID, flashcard.getFlashcardSetId());
        db.insert(DatabaseHelper.TABLE_FLASHCARD, null, values);
    }

    public void updateFlashcard(Flashcard flashcard) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FLASHCARD_WORD, flashcard.getWord());
        values.put(DatabaseHelper.COLUMN_FLASHCARD_MEANING, flashcard.getMeaning());
        values.put(DatabaseHelper.COLUMN_FLASHCARD_PRONUNCIATION, flashcard.getPronunciation());
        values.put(DatabaseHelper.COLUMN_FLASHCARD_IMAGE, flashcard.getImage());
        values.put(DatabaseHelper.COLUMN_FLASHCARD_SET_ID, flashcard.getFlashcardSetId());
        db.update(DatabaseHelper.TABLE_FLASHCARD, values, DatabaseHelper.COLUMN_FLASHCARD_ID + " = ?", new String[]{String.valueOf(flashcard.getId())});
    }

    public void deleteFlashcard(int id) {
        db.delete(DatabaseHelper.TABLE_FLASHCARD, DatabaseHelper.COLUMN_FLASHCARD_ID + " = ?", new String[]{String.valueOf(id)});
    }
}

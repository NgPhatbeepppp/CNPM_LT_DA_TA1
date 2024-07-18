package com.example.cnpm_lt_da_ta.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cnpm_lt_da_ta.DatabaseHelper;
import com.example.cnpm_lt_da_ta.Course.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public FlashcardSetDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<FlashcardSet> getFlashcardSetsByCourseId(int courseId) {
        List<FlashcardSet> flashcardSets = new ArrayList<>();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FLASHCARDSET,
                null,
                DatabaseHelper.COLUMN_FLASHCARDSET_COURSE_ID + " = ?",
                new String[]{String.valueOf(courseId)},
                null, null, null
        );
        while (cursor.moveToNext()) {
            @SuppressLint("Range") FlashcardSet flashcardSet = new FlashcardSet(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_NAME)),
                    courseId // Không cần lấy lại courseId vì đã lọc theo nó
            );
            flashcardSets.add(flashcardSet);
        }
        cursor.close();
        return flashcardSets;
    }

    public FlashcardSet getFlashcardSetById(int id) {
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FLASHCARDSET,
                null,
                DatabaseHelper.COLUMN_FLASHCARDSET_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") FlashcardSet flashcardSet = new FlashcardSet(
                    id, // Không cần lấy lại id vì đã lọc theo nó
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_COURSE_ID))
            );
            cursor.close();
            return flashcardSet;
        } else {
            cursor.close();
            return null;
        }
    }

    public void insertFlashcardSet(FlashcardSet flashcardSet) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FLASHCARDSET_NAME, flashcardSet.getName());
        values.put(DatabaseHelper.COLUMN_FLASHCARDSET_COURSE_ID, flashcardSet.getCourseId());
        db.insert(DatabaseHelper.TABLE_FLASHCARDSET, null, values);
    }

    public void updateFlashcardSet(FlashcardSet flashcardSet) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FLASHCARDSET_NAME, flashcardSet.getName());
        values.put(DatabaseHelper.COLUMN_FLASHCARDSET_COURSE_ID, flashcardSet.getCourseId());
        db.update(DatabaseHelper.TABLE_FLASHCARDSET, values, DatabaseHelper.COLUMN_FLASHCARDSET_ID + " = ?", new String[]{String.valueOf(flashcardSet.getId())});
    }

    public void deleteFlashcardSet(int id) {
        db.delete(DatabaseHelper.TABLE_FLASHCARDSET, DatabaseHelper.COLUMN_FLASHCARDSET_ID + " = ?", new String[]{String.valueOf(id)});
    }
}

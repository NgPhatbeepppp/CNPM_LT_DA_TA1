package com.example.cnpm_lt_da_ta.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.DatabaseHelper;
import com.example.cnpm_lt_da_ta.Course.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private List<Flashcard> selectedFlashcards;

    public FlashcardSetDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    public FlashcardSetDAO(Context context, List<Flashcard> selectedFlashcards) {
        this(context);
        this.selectedFlashcards = selectedFlashcards;
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

    // Phương thức chèn FlashcardSet với courseId tùy chọn
    public int insertFlashcardSet(FlashcardSet flashcardSet) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FLASHCARDSET_NAME, flashcardSet.getName());
        if (flashcardSet.getCourseId() != 0) {
            values.put(DatabaseHelper.COLUMN_FLASHCARDSET_COURSE_ID, flashcardSet.getCourseId());
        }
        int newRowId = (int) db.insert(DatabaseHelper.TABLE_FLASHCARDSET, null, values);
        flashcardSet.setId(newRowId); // Cập nhật ID cho flashcardSet

        // Chèn vào bảng trung gian nếu có selectedFlashcards
        if (selectedFlashcards != null) {
            for (Flashcard flashcard : selectedFlashcards) {
                ContentValues values2 = new ContentValues();
                values2.put(DatabaseHelper.COLUMN_FLASHCARDSET_ID, newRowId);
                values2.put(DatabaseHelper.COLUMN_FLASHCARD_ID, flashcard.getId());
                db.insert(DatabaseHelper.TABLE_FLASHCARDSET_FLASHCARD, null, values2);
            }
        }

        return newRowId;
    }

    public void updateFlashcardSet(FlashcardSet flashcardSet) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FLASHCARDSET_NAME, flashcardSet.getName());
        values.put(DatabaseHelper.COLUMN_FLASHCARDSET_COURSE_ID, flashcardSet.getCourseId());
        db.update(DatabaseHelper.TABLE_FLASHCARDSET, values, DatabaseHelper.COLUMN_FLASHCARDSET_ID + " = ?", new String[]{String.valueOf(flashcardSet.getId())});
    }

    public void deleteFlashcardSet(long id) {
        db.delete(DatabaseHelper.TABLE_FLASHCARDSET, DatabaseHelper.COLUMN_FLASHCARDSET_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public List<FlashcardSet> getAllFlashcardSets() {
        List<FlashcardSet> flashcardSets = new ArrayList<>();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FLASHCARDSET,
                null, // Tất cả các cột
                null, // Không có mệnh đề WHERE
                null,
                null, null, null
        );
        while (cursor.moveToNext()) {
            @SuppressLint("Range") FlashcardSet flashcardSet = new FlashcardSet(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_COURSE_ID))
            );
            flashcardSets.add(flashcardSet);
        }
        cursor.close();
        return flashcardSets;
    }
    public List<FlashcardSet> searchFlashcardSets(String query) {
        List<FlashcardSet> flashcardSets = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_FLASHCARDSET_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"}; // Tìm kiếm theo phần khớp của tên

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FLASHCARDSET,
                null,
                selection,
                selectionArgs,
                null, null, null
        );
        while (cursor.moveToNext()) {
            @SuppressLint("Range") FlashcardSet flashcardSet = new FlashcardSet(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_FLASHCARDSET_COURSE_ID))
            );
            flashcardSets.add(flashcardSet);
        }
        cursor.close();
        return flashcardSets;
    }




}

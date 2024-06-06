package com.example.cnpm_lt_da_ta;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Databasehelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lessons.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_LESSON = "lessons";

    public Databasehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LESSON_TABLE = "CREATE TABLE " + LessonContract.LessonEntry.TABLE_NAME + " ("
                + LessonContract.LessonEntry.COLUMN_ID + " TEXT PRIMARY KEY , "
                + LessonContract.LessonEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + LessonContract.LessonEntry.COLUMN_TYPE + " TEXT, "
                + LessonContract.LessonEntry.COLUMN_CONTENT + " TEXT"
                + ");";
        db.execSQL(CREATE_LESSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LESSON); //
        onCreate(db);
    }

    public long insertLesson(String id,String title, String type, String content) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LessonContract.LessonEntry.COLUMN_ID, id);
        values.put(LessonContract.LessonEntry.COLUMN_TITLE, title);
        values.put(LessonContract.LessonEntry.COLUMN_TYPE, type); // Thêm giá trị cho cột loại bài học
        values.put(LessonContract.LessonEntry.COLUMN_CONTENT, content);

        return db.insert(TABLE_LESSON, null, values); // Đổi tên bảng
    }
    public List<Lesson> getAllLessons() {
        List<Lesson> lessonList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                LessonContract.LessonEntry.COLUMN_ID,
                LessonContract.LessonEntry.COLUMN_TITLE,
                LessonContract.LessonEntry.COLUMN_TYPE,
                LessonContract.LessonEntry.COLUMN_CONTENT
        };

        Cursor cursor = db.query(
                LessonContract.LessonEntry.TABLE_NAME,
                projection,
                null, null, null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_TITLE));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_TYPE));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_CONTENT));

                Lesson lesson = new Lesson(id, title, type, content);
                lessonList.add(lesson);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return lessonList;
    }
}


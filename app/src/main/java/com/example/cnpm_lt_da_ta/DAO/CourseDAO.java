
package com.example.cnpm_lt_da_ta.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cnpm_lt_da_ta.Course.Course;
import com.example.cnpm_lt_da_ta.Course.CourseDetailActivity;
import com.example.cnpm_lt_da_ta.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;


    public CourseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_COURSE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") Course course = new Course(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_IMAGE)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_POPULARITY)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_IS_NEW))

            );
            courses.add(course);
        }
        cursor.close();
        return courses;
    }

    public Course getCourseById(int id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_COURSE, null, DatabaseHelper.COLUMN_COURSE_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") Course course = new Course(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_IMAGE)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_POPULARITY)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_IS_NEW))
            );
            cursor.close();
            return course;
        } else {
            cursor.close();
            return null;
        }
    }

    public void insertCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COURSE_NAME, course.getName());
        values.put(DatabaseHelper.COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(DatabaseHelper.COLUMN_COURSE_IMAGE, course.getImage());
        values.put(DatabaseHelper.COLUMN_COURSE_POPULARITY, course.getPopularity());
        values.put(DatabaseHelper.COLUMN_COURSE_IS_NEW, course.getIsNew());
        db.insert(DatabaseHelper.TABLE_COURSE, null, values);
    }
    public void updateCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COURSE_NAME, course.getName());
        values.put(DatabaseHelper.COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(DatabaseHelper.COLUMN_COURSE_IMAGE, course.getImage());
        values.put(DatabaseHelper.COLUMN_COURSE_POPULARITY, course.getPopularity());
        values.put(DatabaseHelper.COLUMN_COURSE_IS_NEW, course.getIsNew());
        db.update(DatabaseHelper.TABLE_COURSE, values, DatabaseHelper.COLUMN_COURSE_ID + " = ?", new String[]{String.valueOf(course.getId())});
    }
    public void deleteCourse(int id) {
        db.delete(DatabaseHelper.TABLE_COURSE, DatabaseHelper.COLUMN_COURSE_ID + " = ?", new String[]{String.valueOf(id)});
    }
}


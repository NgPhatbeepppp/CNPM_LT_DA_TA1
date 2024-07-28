
package com.example.cnpm_lt_da_ta.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COURSE_IS_NEW)));
            Log.d("CourseDAO", "Cursor count: " + cursor.getCount());
            Log.d("CourseDAO", "Course: " + course.toString());
            courses.add(course);
        }
        cursor.close();
        Log.d("CourseDAO", "getAllCourses - Returning courses: " + courses.size());
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
    public List<Course> searchCourses(String query) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COLUMN_COURSE_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_COURSE,
                null, // Hoặc bạn có thể chỉ định các cột cụ thể cần lấy
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_DESCRIPTION));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_IMAGE));
            int popularity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_POPULARITY));
            boolean isNewFromDatabase = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_IS_NEW)) == 1;
            int isNew = isNewFromDatabase ? 1 : 0; // Chuyển boolean thành int (1 hoặc 0)(DatabaseHelper.COLUMN_COURSE_IS_NEW)) == 1;

            Course course = new Course(id, name, description, image, popularity, isNew);
            courses.add(course);
        }

        cursor.close();
        return courses;
    }
}


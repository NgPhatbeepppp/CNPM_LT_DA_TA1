package com.example.cnpm_lt_da_ta.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cnpm_lt_da_ta.model.Course;
import com.example.cnpm_lt_da_ta.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CourseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        open();  // Đảm bảo database đã mở

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
        close();  // Đóng database sau khi truy vấn xong
        return courses;
    }

    public Course getCourseById(int id) {
        open();  // Đảm bảo database đã mở
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
            close();  // Đóng database sau khi lấy dữ liệu
            return course;
        } else {
            cursor.close();
            close();
            return null;
        }
    }

    public long insertCourse(Course course) {  // Chỉnh sửa void -> long để kiểm tra ID chèn
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COURSE_NAME, course.getName());
        values.put(DatabaseHelper.COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(DatabaseHelper.COLUMN_COURSE_IMAGE, course.getImage());
        values.put(DatabaseHelper.COLUMN_COURSE_POPULARITY, course.getPopularity());
        values.put(DatabaseHelper.COLUMN_COURSE_IS_NEW, course.getIsNew());

        long result = db.insert(DatabaseHelper.TABLE_COURSE, null, values);
        if (result != -1) {
            addNews("Khóa học mới: " + course.getName());
        }

        close();
        return result;
    }

    private void addNews(String message) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NEWS_MESSAGE, message);
        values.put(DatabaseHelper.COLUMN_NEWS_TIMESTAMP, System.currentTimeMillis());

        db.insert(DatabaseHelper.TABLE_NEWS, null, values);
    }

    public void updateCourse(Course course) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COURSE_NAME, course.getName());
        values.put(DatabaseHelper.COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(DatabaseHelper.COLUMN_COURSE_IMAGE, course.getImage());
        values.put(DatabaseHelper.COLUMN_COURSE_POPULARITY, course.getPopularity());
        values.put(DatabaseHelper.COLUMN_COURSE_IS_NEW, course.getIsNew());

        db.update(DatabaseHelper.TABLE_COURSE, values, DatabaseHelper.COLUMN_COURSE_ID + " = ?", new String[]{String.valueOf(course.getId())});
        close();
    }
    public long addCourse(Course course) {
        open(); // Mở database trước khi thao tác

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COURSE_NAME, course.getName());
        values.put(DatabaseHelper.COLUMN_COURSE_DESCRIPTION, course.getDescription());
        values.put(DatabaseHelper.COLUMN_COURSE_IMAGE, course.getImage());
        values.put(DatabaseHelper.COLUMN_COURSE_POPULARITY, course.getPopularity());
        values.put(DatabaseHelper.COLUMN_COURSE_IS_NEW, course.getIsNew());

        long result = db.insert(DatabaseHelper.TABLE_COURSE, null, values);

        if (result == -1) {
            Log.e("SQLite", "Lỗi khi thêm khóa học vào database");
        } else {
            Log.d("SQLite", "Thêm khóa học thành công, ID: " + result);
        }

        close(); // Đóng database sau khi thao tác xong
        return result;
    }

    public void deleteCourse(int id) {
        open();
        db.delete(DatabaseHelper.TABLE_COURSE, DatabaseHelper.COLUMN_COURSE_ID + " = ?", new String[]{String.valueOf(id)});
        close();
    }

    public List<Course> searchCourses(String query) {
        List<Course> courses = new ArrayList<>();
        open();

        String selection = DatabaseHelper.COLUMN_COURSE_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        Cursor cursor = db.query(DatabaseHelper.TABLE_COURSE, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_DESCRIPTION));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_IMAGE));
            int popularity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_POPULARITY));
            int isNew = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_IS_NEW));

            courses.add(new Course(id, name, description, image, popularity, isNew));
        }

        cursor.close();
        close();
        return courses;
    }
}

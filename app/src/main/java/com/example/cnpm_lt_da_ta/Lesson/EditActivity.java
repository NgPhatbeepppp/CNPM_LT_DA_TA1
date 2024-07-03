package com.example.cnpm_lt_da_ta.Lesson;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cnpm_lt_da_ta.MainActivity;
import com.example.cnpm_lt_da_ta.R;
import com.example.cnpm_lt_da_ta.User.UserManagementActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditActivity extends AppCompatActivity {

    private EditText editTextLessonName;
    private EditText editTextLessonID;
    private EditText editTextLessonType;
    private EditText editTextLessonContent;
    private Button buttonSaveEdit;
    private Databasehelper dbHelper;
    private String lessonId; // Lưu trữ id của bài học cần sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Ánh xạ các thành phần giao diện
        editTextLessonName = findViewById(R.id.edit_text_lesson_name);
        editTextLessonID = findViewById(R.id.edit_text_lesson_id);
        editTextLessonType = findViewById(R.id.edit_text_lesson_type);
        editTextLessonContent = findViewById(R.id.edit_text_lesson_content);
        buttonSaveEdit = findViewById(R.id.button_save_edit);
        dbHelper = new Databasehelper(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId(); // Lấy itemId

            if (itemId == R.id.nav_home) {
                // Chuyển đến MainActivity
                startActivity(new Intent(EditActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_back) {
                onBackPressed();
                return true;

            } else if (itemId == R.id.nav_users) {
                // Chuyển đến UserManagementActivity
                startActivity(new Intent(EditActivity.this, UserManagementActivity.class));
                finish();
                return true;
            }

            return false; // Trả về false nếu không xử lý được itemId
        });

        // Lấy lessonId từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("lessonId")) {
            lessonId = intent.getStringExtra("lessonId");
            loadLessonData(lessonId); // Load dữ liệu bài học từ database
        }

        // Xử lý sự kiện khi nhấn nút "Lưu thay đổi"
        buttonSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLesson();
            }
        });
    }

    private void loadLessonData(String lessonId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LessonContract.LessonEntry.COLUMN_TITLE,
                LessonContract.LessonEntry.COLUMN_TYPE,
                LessonContract.LessonEntry.COLUMN_CONTENT
        };

        String selection = LessonContract.LessonEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {lessonId};

        Cursor cursor = db.query(
                LessonContract.LessonEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_TITLE);
            int typeColumnIndex = cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_TYPE);
            int contentColumnIndex = cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_CONTENT);

            String title = cursor.getString(titleColumnIndex);
            String type = cursor.getString(typeColumnIndex);
            String content = cursor.getString(contentColumnIndex);

            editTextLessonName.setText(title);
            editTextLessonID.setText(lessonId);
            editTextLessonType.setText(type);
            editTextLessonContent.setText(content);
        }

        cursor.close();
    }

    private void updateLesson() {
        String lessonName = editTextLessonName.getText().toString().trim();
        String lessonType = editTextLessonType.getText().toString().trim();
        String lessonContent = editTextLessonContent.getText().toString().trim();

        if (lessonName.isEmpty() || lessonContent.isEmpty() || lessonType.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LessonContract.LessonEntry.COLUMN_TITLE, lessonName);
        values.put(LessonContract.LessonEntry.COLUMN_TYPE, lessonType);
        values.put(LessonContract.LessonEntry.COLUMN_CONTENT, lessonContent);

        String selection = LessonContract.LessonEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = {lessonId};

        int rowsUpdated = db.update(LessonContract.LessonEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Cập nhật bài học thành công", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish(); // Trở lại LessonManagementActivity sau khi cập nhật
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật bài học", Toast.LENGTH_SHORT).show();
        }
    }
}

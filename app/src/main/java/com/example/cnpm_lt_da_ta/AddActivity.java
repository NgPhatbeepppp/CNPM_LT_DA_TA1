package com.example.cnpm_lt_da_ta;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;


public class AddActivity extends AppCompatActivity {
    private EditText editTextLessonName;
    private EditText editTextLessonID;
    private EditText editTextLessonType;  //  Thêm EditText cho loại bài học
    private EditText editTextLessonContent;
    private Databasehelper dbHelper;
    private static final int REQUEST_ADD_LESSON = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        editTextLessonName = findViewById(R.id.editTextLessonName);
        editTextLessonID = findViewById(R.id.editTextLessonID);
        editTextLessonType = findViewById(R.id.editTextLessonType);  //  Ánh xạ EditText mới
        editTextLessonContent = findViewById(R.id.editTextLessonContent);
        Button btnSaveLesson = findViewById(R.id.btnSaveLesson);
        dbHelper = new Databasehelper(this);

        int lessonCount = getLessonCount();
        Log.d("LessonCount", "Số lượng bài học: " + lessonCount);


        btnSaveLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLesson();
            }
        });
    }
        private void saveLesson() {
            String lessonName = editTextLessonName.getText().toString().trim();
            String lessonID = editTextLessonID.getText().toString().trim();
            String lessonType = editTextLessonType.getText().toString().trim(); // Lấy giá trị từ EditText mới
            String lessonContent = editTextLessonContent.getText().toString().trim();

            if (lessonName.isEmpty() || lessonContent.isEmpty() || lessonType.isEmpty()) {  // Kiểm tra cả lessonType
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(LessonContract.LessonEntry.COLUMN_TITLE, lessonName);
            values.put(LessonContract.LessonEntry.COLUMN_ID, lessonID);
            values.put(LessonContract.LessonEntry.COLUMN_TYPE, lessonType); // Thêm giá trị cho cột type
            values.put(LessonContract.LessonEntry.COLUMN_CONTENT, lessonContent);

            long newRowId = db.insert(LessonContract.LessonEntry.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Toast.makeText(this, "Lỗi khi lưu bài học", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bài học đã được lưu", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            db.close();


       }


    private int getLessonCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + LessonContract.LessonEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if(cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }
}

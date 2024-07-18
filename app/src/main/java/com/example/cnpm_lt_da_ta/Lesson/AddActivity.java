package com.example.cnpm_lt_da_ta.Lesson;

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

import com.example.cnpm_lt_da_ta.DatabaseHelper;
import com.example.cnpm_lt_da_ta.R;


public class AddActivity extends AppCompatActivity {
    private EditText editTextLessonName;
    private EditText editTextLessonID;
    private EditText editTextLessonType;  //  Thêm EditText cho loại bài học
    private EditText editTextLessonContent;
    private static final int REQUEST_ADD_LESSON = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

    }

}

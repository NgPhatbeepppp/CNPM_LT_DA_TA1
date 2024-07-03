package com.example.cnpm_lt_da_ta.Lesson;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.cnpm_lt_da_ta.MainActivity;
import com.example.cnpm_lt_da_ta.R;
import com.example.cnpm_lt_da_ta.User.UserManagementActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class LessonManagementActivity extends AppCompatActivity  {
    private Button addbtn, editbtn, deletebtn;
    private static final int REQUEST_ADD_LESSON = 1;
    private static final int REQUEST_EDIT_LESSON = 2;
    private ListView listviewLessons;
    private LessonAdapter lessonAdapter;
    private Databasehelper dbHelper;
    private List<Lesson> lessonList;
    private int selectedLessonPosition = ListView.INVALID_POSITION; // Lưu vị trí bài học được chọn


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_management);
        listviewLessons = findViewById(R.id.listview_lessons);
        listviewLessons.setOnItemLongClickListener((parent, view, position, id) -> {
            selectedLessonPosition = position; // Lưu vị trí khi nhấn giữ lâu
            Toast.makeText(LessonManagementActivity.this, "Đã chọn bài học ở vị trí " + position, Toast.LENGTH_SHORT).show();
            return true; // Trả về true để ngăn chặn sự kiện click bình thường
        });

        dbHelper = new Databasehelper(this);
        lessonList = fetchLessonsFromDatabase();
        lessonAdapter = new LessonAdapter(this, lessonList);
        listviewLessons.setAdapter(lessonAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId(); // Lấy itemId

            if (itemId == R.id.nav_home) {
                // Chuyển đến MainActivity
                startActivity(new Intent(LessonManagementActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_back) {
                onBackPressed();
                return true;

            } else if (itemId == R.id.nav_users) {
                // Chuyển đến UserManagementActivity
                startActivity(new Intent(LessonManagementActivity.this, UserManagementActivity.class));
                finish();
                return true;
            }

            return false; // Trả về false nếu không xử lý được itemId
        });





        addbtn = findViewById(R.id.btn_add_lesson);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonManagementActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_ADD_LESSON);
            }
        });

        editbtn = findViewById(R.id.btn_edit_lesson);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLessonPosition != ListView.INVALID_POSITION) { // Kiểm tra xem đã có bài học nào được chọn chưa
                    Lesson selectedLesson = lessonList.get(selectedLessonPosition); // Sử dụng selectedLessonPosition
                    Intent intent = new Intent(LessonManagementActivity.this, EditActivity.class);
                    // Truyền id của bài học đã chọn
                    intent.putExtra("lessonId", selectedLesson.getId());
                    startActivityForResult(intent, REQUEST_EDIT_LESSON);
                } else {
                    Toast.makeText(LessonManagementActivity.this, "Vui lòng chọn một bài học", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deletebtn = findViewById(R.id.btn_delete_lesson);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedLessonPosition != ListView.INVALID_POSITION) {
                    showDeleteConfirmationDialog(selectedLessonPosition);
                } else {
                    Toast.makeText(LessonManagementActivity.this, "Vui lòng chọn một bài học", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa bài học này?");

        // Nút "Có"
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteLesson(position);
            }
        });

        // Nút "Không"
        builder.setNegativeButton("Không!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Không làm gì cả
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void deleteLesson(int position) {
        Lesson selectedLesson = lessonAdapter.getItem(position);
        if (selectedLesson != null) {
            String lessonId = selectedLesson.getId();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String whereClause = LessonContract.LessonEntry.COLUMN_ID + " = ?";
            String[] whereArgs = {lessonId};
            int rowsDeleted = db.delete(LessonContract.LessonEntry.TABLE_NAME, whereClause, whereArgs);

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Bài học đã được xóa", Toast.LENGTH_SHORT).show();
                lessonList.clear();
                lessonList.addAll(fetchLessonsFromDatabase());
                lessonAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Lỗi khi xóa bài học", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_LESSON || requestCode == REQUEST_EDIT_LESSON) { // Xử lý cả 2 trường hợp
                lessonList.clear();
                lessonList.addAll(fetchLessonsFromDatabase());
                lessonAdapter.notifyDataSetChanged();
            }
        }
    }

    private List<Lesson> fetchLessonsFromDatabase() {
        List<Lesson> lessonList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LessonContract.LessonEntry.COLUMN_ID,
                LessonContract.LessonEntry.COLUMN_TITLE,
                LessonContract.LessonEntry.COLUMN_TYPE,
                LessonContract.LessonEntry.COLUMN_CONTENT
        };

        Cursor cursor = db.query(
                LessonContract.LessonEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_ID);
            int titleColumnIndex = cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_TITLE);
            int typeColumnIndex = cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_TYPE);
            int contentColumnIndex = cursor.getColumnIndexOrThrow(LessonContract.LessonEntry.COLUMN_CONTENT);

            String id = cursor.getString(idColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String type = cursor.getString(typeColumnIndex);
            String content = cursor.getString(contentColumnIndex);

            Lesson lesson = new Lesson(id, title, type, content);
            lessonList.add(lesson);
        }

        cursor.close();

        return lessonList;

    }
}

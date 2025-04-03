package com.example.cnpm_lt_da_ta.Manager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.DatabaseHelper;
import com.example.cnpm_lt_da_ta.model.Course;
import com.example.cnpm_lt_da_ta.model.FlashcardSet;
import com.example.cnpm_lt_da_ta.DAO.CourseDAO;
import com.example.cnpm_lt_da_ta.DAO.FlashcardSetDAO;
import com.example.cnpm_lt_da_ta.ManagerAdapter.FlashcardSetManagementAdapter;
import com.example.cnpm_lt_da_ta.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CourseCreateActivity extends AppCompatActivity {

    private EditText etCourseName, etCourseDescription, etCourseImage;
    private RecyclerView rvFlashcardSets;
    private FlashcardSetDAO flashcardSetDAO;
    private CourseDAO courseDAO;
    private FlashcardSetManagementAdapter flashcardSetAdapter;
    private List<FlashcardSet> selectedFlashcardSets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_create);

        etCourseName = findViewById(R.id.etCourseName);
        etCourseDescription = findViewById(R.id.etCourseDescription);
        etCourseImage = findViewById(R.id.etCourseImage);
        rvFlashcardSets = findViewById(R.id.rvFlashcardSets);
        rvFlashcardSets.setLayoutManager(new LinearLayoutManager(this));

        flashcardSetDAO = new FlashcardSetDAO(this);
        courseDAO = new CourseDAO(this);
        flashcardSetDAO.open();
        courseDAO.open();

        loadFlashcardSets();

        Button btnSaveCourse = findViewById(R.id.btnSaveCourse);
        btnSaveCourse.setOnClickListener(v -> saveCourse());
    }

    private void loadFlashcardSets() {
        List<FlashcardSet> flashcardSets = flashcardSetDAO.getAllFlashcardSets();
        flashcardSetAdapter = new FlashcardSetManagementAdapter(flashcardSets, this); // Bật chế độ chọn
        flashcardSetAdapter.setSelectionMode(true);
        flashcardSetAdapter.setOnItemClickListener(flashcardSet -> {
            if (selectedFlashcardSets.contains(flashcardSet)) {
                selectedFlashcardSets.remove(flashcardSet);
            } else {
                selectedFlashcardSets.add(flashcardSet);
            }
        });
        rvFlashcardSets.setAdapter(flashcardSetAdapter);
    }

    private void saveCourse() {
        String name = etCourseName.getText().toString().trim();
        String description = etCourseDescription.getText().toString().trim();
        String image = etCourseImage.getText().toString().trim();
        int popularity = 0; // Giá trị mặc định
        int isNew = 1; // Đánh dấu là khóa học mới

        if (name.isEmpty() || description.isEmpty() || image.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Course course = new Course(name, description, image, popularity, isNew);

        // Thêm vào SQLite
        CourseDAO courseDAO = new CourseDAO(this);
        long result = courseDAO.addCourse(course);

        if (result != -1) {
            Toast.makeText(this, "Thêm khóa học thành công!", Toast.LENGTH_SHORT).show();
            Log.d("SQLite", "Khóa học đã thêm vào SQLite với ID: " + result);

            // 🔹 Thêm thông báo vào bảng news
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            String newsMessage = "Khóa học " + name + " đã được tạo";
            dbHelper.addNews(newsMessage);

            finish(); // Đóng Activity sau khi thêm thành công
        } else {
            Toast.makeText(this, "Lỗi khi thêm khóa học", Toast.LENGTH_SHORT).show();
            Log.e("SQLite", "Lỗi khi thêm khóa học vào database");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flashcardSetDAO.close();
        courseDAO.close();
    }
}

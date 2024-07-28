package com.example.cnpm_lt_da_ta.Course;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cnpm_lt_da_ta.Adapter.FlashcardSetAdapter;
import com.example.cnpm_lt_da_ta.DAO.CourseDAO;
import com.example.cnpm_lt_da_ta.DAO.FlashcardSetDAO;
import com.example.cnpm_lt_da_ta.R;

import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private ImageView ivCourseImage;
    private TextView tvCourseName, tvCourseDescription, tvCourseLevel;
    private RecyclerView rvFlashcardSets;
    private CourseDAO courseDAO;
    private FlashcardSetDAO flashcardSetDAO;
    private FlashcardSetAdapter flashcardSetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        ivCourseImage = findViewById(R.id.ivCourseImage);
        tvCourseName = findViewById(R.id.tvCourseName);
        tvCourseDescription = findViewById(R.id.tvCourseDescription);
        rvFlashcardSets = findViewById(R.id.rvFlashcardSets);

        courseDAO = new CourseDAO(this);
        flashcardSetDAO = new FlashcardSetDAO(this);


        courseDAO.open();
        flashcardSetDAO.open();

        int courseId = getIntent().getIntExtra("courseId", -1);
        if (courseId != -1) {
            Course course = courseDAO.getCourseById(courseId);
            if (course != null) {
                tvCourseName.setText(course.getName());
                tvCourseDescription.setText(course.getDescription());
                try {
                    Glide.with(this).load(course.getImage()).into(ivCourseImage);
                } catch (Exception e) {
                    // Xử lý lỗi tải ảnh ở đây (ví dụ: hiển thị ảnh mặc định)
                    ivCourseImage.setImageResource(R.drawable.lesson);
                }
            }

            // Trong CourseDetailActivity.java
            List<FlashcardSet> flashcardSets = flashcardSetDAO.getFlashcardSetsByCourseId(courseId);
            flashcardSetAdapter = new FlashcardSetAdapter(flashcardSets, this);
            rvFlashcardSets.setAdapter(flashcardSetAdapter);
            rvFlashcardSets.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        courseDAO.close();
        flashcardSetDAO.close();
    }
}

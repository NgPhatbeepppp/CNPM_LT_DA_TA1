package com.example.cnpm_lt_da_ta.Lesson;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;


import com.example.cnpm_lt_da_ta.R;
import com.example.cnpm_lt_da_ta.fragment.HomeFragment;


public class LessonManagementActivity extends AppCompatActivity  {
    private Button addbtn, editbtn, deletebtn;
    private static final int REQUEST_ADD_LESSON = 1;
    private static final int REQUEST_EDIT_LESSON = 2;
    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_NEWS = 2;
    public static final int FRAGMENT_USER = 3;
    private  int mCurrentFRAGMENT =1;

    private ListView listviewLessons;
    private LessonAdapter lessonAdapter;

    private List<Lesson> lessonList;
    private int selectedLessonPosition = ListView.INVALID_POSITION; // Lưu vị trí bài học được chọn


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_management);
    }












    private void replacefragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

    }
}

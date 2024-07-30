package com.example.cnpm_lt_da_ta.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.DAO.CourseDAO;
import com.example.cnpm_lt_da_ta.DAO.FlashcardDAO;
import com.example.cnpm_lt_da_ta.DAO.FlashcardSetDAO;
import com.example.cnpm_lt_da_ta.Manager.CourseCreateActivity;
import com.example.cnpm_lt_da_ta.Manager.FlashcardCreateActivity;
import com.example.cnpm_lt_da_ta.Manager.FlashcardSetCreateActivity;
import com.example.cnpm_lt_da_ta.ManagerAdapter.FlashcardManagementAdapter;
import com.example.cnpm_lt_da_ta.ManagerAdapter.FlashcardSetManagementAdapter;
import com.example.cnpm_lt_da_ta.ManagerAdapter.CourseManagementAdapter;
import com.example.cnpm_lt_da_ta.Course.Course;
import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.Course.FlashcardSet;
import com.example.cnpm_lt_da_ta.R;

import java.util.List;

public class LearnManagementActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnFlashcards, btnFlashcardSets, btnCourses, btnAddItem;
    private RecyclerView rvItems;

    private FlashcardDAO flashcardDAO;
    private FlashcardSetDAO flashcardSetDAO;
    private CourseDAO courseDAO;

    private RecyclerView.Adapter currentAdapter;
    private int currentDataType = 1; // 1: Flashcard, 2: FlashcardSet, 3: Course

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_management_activity);

        // Khởi tạo các View từ layout
        etSearch = findViewById(R.id.etSearch);
        btnFlashcards = findViewById(R.id.btnFlashcards);
        btnFlashcardSets = findViewById(R.id.btnFlashcardSets);
        btnCourses = findViewById(R.id.btnCourses);
        btnAddItem = findViewById(R.id.btnAddItem);
        rvItems = findViewById(R.id.rvItems);

        // Khởi tạo các DAO
        flashcardDAO = new FlashcardDAO(this);
        flashcardSetDAO = new FlashcardSetDAO(this);
        courseDAO = new CourseDAO(this);

        // Mở kết nối đến cơ sở dữ liệu
        flashcardDAO.open();
        flashcardSetDAO.open();
        courseDAO.open();

        // Thiết lập RecyclerView
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Tải danh sách flashcard ban đầu (mặc định hiển thị flashcard)
        loadItems();
        // Thiết lập OnClickListener cho các nút
        btnFlashcards.setOnClickListener(v -> {
            currentDataType = 1; // Chuyển sang chế độ quản lý Flashcard
            loadItems(); // Tải lại danh sách Flashcard
            btnAddItem.setText("Thêm Flashcard"); // Cập nhật text của nút "Thêm"
        });

        btnFlashcardSets.setOnClickListener(v -> {
            currentDataType = 2; // Chuyển sang chế độ quản lý FlashcardSet
            loadItems(); // Tải lại danh sách FlashcardSet
            btnAddItem.setText("Thêm Bộ Flashcard"); // Cập nhật text của nút "Thêm"
        });

        btnCourses.setOnClickListener(v -> {
            currentDataType = 3; // Chuyển sang chế độ quản lý Course
            loadItems(); // Tải lại danh sách Course
            btnAddItem.setText("Thêm Khóa học"); // Cập nhật text của nút "Thêm"
        });

        btnAddItem.setOnClickListener(v -> onAddItemClick());
        // Thiết lập TextWatcher cho EditText tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                searchItems(query); // Thực hiện tìm kiếm khi nội dung thay đổi
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý gì ở đây
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadItems(); // Tải lại danh sách khi quay lại Activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDAOs(); // Đóng kết nối đến cơ sở dữ liệu
    }




    private void closeDAOs() {
        flashcardDAO.close();
        flashcardSetDAO.close();
        courseDAO.close();
    }

    private void loadItems() {
        switch (currentDataType) {
            case 1: // Flashcard
                List<Flashcard> flashcards = flashcardDAO.getAllFlashcards();
                currentAdapter = new FlashcardManagementAdapter(flashcards, this);
                break;
            case 2: // FlashcardSet
                List<FlashcardSet> flashcardSets = flashcardSetDAO.getAllFlashcardSets();
                currentAdapter = new FlashcardSetManagementAdapter(flashcardSets, this);
                break;
            case 3: // Course
                List<Course> courses = courseDAO.getAllCourses();
                currentAdapter = new CourseManagementAdapter(courses, this);
                break;
            default:
                return; // Hoặc xử lý lỗi nếu currentDataType không hợp lệ
        }
        rvItems.setAdapter(currentAdapter); // Đặt adapter cho RecyclerView
    }

    private void searchItems(String query) {
        switch (currentDataType) {
            case 1: // Flashcard
                List<Flashcard> filteredFlashcards = flashcardDAO.searchFlashcards(query);
                currentAdapter = new FlashcardManagementAdapter(filteredFlashcards, this);
                break;
            case 2: // FlashcardSet
                List<FlashcardSet> filteredFlashcardSets = flashcardSetDAO.searchFlashcardSets(query);
                currentAdapter = new FlashcardSetManagementAdapter(filteredFlashcardSets, this);
                break;
            case 3: // Course
                List<Course> filteredCourses = courseDAO.searchCourses(query);
                currentAdapter = new CourseManagementAdapter(filteredCourses, this);
                break;
        }
        rvItems.setAdapter(currentAdapter); // Cập nhật adapter cho RecyclerView
    }


    private void onAddItemClick() {
        Intent intent;
        switch (currentDataType) {
            case 1: // Flashcard
                intent = new Intent(this, FlashcardCreateActivity.class);
                break;
            case 2: // FlashcardSet
                intent = new Intent(this, FlashcardSetCreateActivity.class);
                break;
            case 3: // Course
                intent = new Intent(this, CourseCreateActivity.class);
                break;
            default:
                return; // Hoặc xử lý lỗi nếu currentDataType không hợp lệ
        }
        startActivity(intent);
    }

}

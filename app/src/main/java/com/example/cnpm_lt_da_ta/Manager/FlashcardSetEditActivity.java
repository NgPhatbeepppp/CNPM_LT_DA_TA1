package com.example.cnpm_lt_da_ta.Manager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.Course.FlashcardSet;
import com.example.cnpm_lt_da_ta.DAO.FlashcardDAO;
import com.example.cnpm_lt_da_ta.DAO.FlashcardSetDAO;
import com.example.cnpm_lt_da_ta.ManagerAdapter.FlashcardManagementAdapter;
import com.example.cnpm_lt_da_ta.R;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetEditActivity extends AppCompatActivity {

    private EditText etFlashcardSetName;
    private RecyclerView rvFlashcards;
    private FlashcardDAO flashcardDAO;
    private FlashcardSetDAO flashcardSetDAO;
    private FlashcardManagementAdapter flashcardAdapter;
    private List<Flashcard> selectedFlashcards = new ArrayList<>();
    private int flashcardSetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_set_create);

        etFlashcardSetName = findViewById(R.id.etFlashcardSetName);
        rvFlashcards = findViewById(R.id.rvFlashcards);
        rvFlashcards.setLayoutManager(new LinearLayoutManager(this));

        flashcardDAO = new FlashcardDAO(this);
        flashcardSetDAO = new FlashcardSetDAO(this);
        flashcardDAO.open();
        flashcardSetDAO.open();

        flashcardSetId = getIntent().getIntExtra("flashcardSetId", -1);
        loadFlashcardSet(); // Tải thông tin bộ flashcard
        loadFlashcards();

        Button btnUpdateFlashcardSet = findViewById(R.id.btnSaveFlashcardSet); // Hoặc đổi id thành btnUpdateFlashcardSet
        btnUpdateFlashcardSet.setOnClickListener(v -> updateFlashcardSet());
    }

    private void loadFlashcardSet() {
        FlashcardSet flashcardSet = flashcardSetDAO.getFlashcardSetById(flashcardSetId);
        if (flashcardSet != null) {
            etFlashcardSetName.setText(flashcardSet.getName());
            // TODO: Lấy danh sách flashcard hiện tại của bộ flashcard và cập nhật selectedFlashcards
        } else {
            Toast.makeText(this, "Không tìm thấy bộ flashcard", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadFlashcards() {
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcards();
        flashcardAdapter = new FlashcardManagementAdapter(flashcards, this);

        flashcardAdapter.setSelectionMode(true); // Bật chế độ chọn

        flashcardAdapter.setOnItemClickListener(flashcard -> {
            if (selectedFlashcards.contains(flashcard)) {
                selectedFlashcards.remove(flashcard);
            } else {
                selectedFlashcards.add(flashcard);
            }
            flashcardAdapter.notifyDataSetChanged();
        });

        rvFlashcards.setAdapter(flashcardAdapter);
    }

    private void updateFlashcardSet() {
        String name = etFlashcardSetName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên bộ flashcard", Toast.LENGTH_SHORT).show();
            return;
        }

        FlashcardSet flashcardSet = new FlashcardSet(flashcardSetId, name, 0); // Giả sử courseId không thay đổi
        flashcardSetDAO.updateFlashcardSet(flashcardSet);

        // TODO: Cập nhật danh sách flashcard trong bảng trung gian (FlashcardSet_Flashcard)

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flashcardDAO.close();
        flashcardSetDAO.close();
    }
}

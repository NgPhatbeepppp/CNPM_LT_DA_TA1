package com.example.cnpm_lt_da_ta.Manager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.model.Flashcard;
import com.example.cnpm_lt_da_ta.model.FlashcardSet;
import com.example.cnpm_lt_da_ta.DAO.FlashcardDAO;
import com.example.cnpm_lt_da_ta.DAO.FlashcardSetDAO;
import com.example.cnpm_lt_da_ta.ManagerAdapter.FlashcardManagementAdapter;
import com.example.cnpm_lt_da_ta.R;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Looper;

public class FlashcardSetCreateActivity extends AppCompatActivity {

    private EditText etFlashcardSetName;
    private RecyclerView rvFlashcards;
    private FlashcardDAO flashcardDAO;
    private FlashcardSetDAO flashcardSetDAO;
    private FlashcardManagementAdapter flashcardAdapter;
    private List<Flashcard> selectedFlashcards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_set_create);

        etFlashcardSetName = findViewById(R.id.etFlashcardSetName);
        rvFlashcards = findViewById(R.id.rvFlashcards);
        rvFlashcards.setLayoutManager(new LinearLayoutManager(this));

        flashcardDAO = new FlashcardDAO(this);
        flashcardSetDAO = new FlashcardSetDAO(this, selectedFlashcards); // Truyền selectedFlashcards vào FlashcardSetDAO
        flashcardDAO.open();
        flashcardSetDAO.open();

        loadFlashcards();

        Button btnSaveFlashcardSet = findViewById(R.id.btnSaveFlashcardSet);
        btnSaveFlashcardSet.setOnClickListener(v -> saveFlashcardSet());
    }

    private void loadFlashcards() {
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcards();
        flashcardAdapter = new FlashcardManagementAdapter(flashcards, this);

        // Bật chế độ chọn flashcard
        flashcardAdapter.setSelectionMode(true);

        // Thiết lập OnItemClickListener để theo dõi các flashcard được chọn
        final Handler handler = new Handler(Looper.getMainLooper());

        flashcardAdapter.setOnItemClickListener(flashcard -> {
            int index = flashcardAdapter.getFlashcardList().indexOf(flashcard);

            if (index != -1) {
                if (selectedFlashcards.contains(flashcard)) {
                    selectedFlashcards.remove(flashcard);
                } else {
                    selectedFlashcards.add(flashcard);
                }

                // Dùng Handler để trì hoãn cập nhật giao diện tránh lỗi IllegalStateException
                handler.post(() -> flashcardAdapter.notifyItemChanged(index));
            }
        });

        rvFlashcards.setAdapter(flashcardAdapter); // ✅ Đặt ngoài setOnItemClickListener()
    }




    private void saveFlashcardSet() {
        String name = etFlashcardSetName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên bộ flashcard", Toast.LENGTH_SHORT).show();
            return;
        }

        FlashcardSet flashcardSet = new FlashcardSet(name, 0); // Không có courseId
        flashcardSetDAO.insertFlashcardSet(flashcardSet); // Lưu flashcard set và các flashcard liên quan

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flashcardDAO.close();
        flashcardSetDAO.close();
    }
}

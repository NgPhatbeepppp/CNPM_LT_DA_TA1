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

import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.Manager.FlashcardCreateActivity;
import com.example.cnpm_lt_da_ta.ManagerAdapter.FlashcardManagementAdapter;
import com.example.cnpm_lt_da_ta.DAO.FlashcardDAO;
import com.example.cnpm_lt_da_ta.R;

import java.util.List;

public class FlashcardManagementActivity extends AppCompatActivity {

    private RecyclerView rvFlashcards;
    private FlashcardManagementAdapter flashcardAdapter;
    private FlashcardDAO flashcardDAO;
    private int flashcardSetId;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_management_activity);

        rvFlashcards = findViewById(R.id.rvFlashcards);


        rvFlashcards.setLayoutManager(new LinearLayoutManager(this));
        etSearch = findViewById(R.id.etSearch);

        flashcardDAO = new FlashcardDAO(this);
        flashcardDAO.open();

        flashcardSetId = getIntent().getIntExtra("flashcardSetId", -1);
        loadFlashcards();

        Button btnAddFlashcard = findViewById(R.id.btnAddFlashcard);
        btnAddFlashcard.setOnClickListener(v -> {
            Intent intent = new Intent(this, FlashcardCreateActivity.class);
            intent.putExtra("flashcardSetId", flashcardSetId);
            startActivity(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                flashcardAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadFlashcards() {
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcards(); // Lấy tất cả flashcard
        flashcardAdapter = new FlashcardManagementAdapter(flashcards, this);
        rvFlashcards.setAdapter(flashcardAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFlashcards();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flashcardDAO.close();
    }
}

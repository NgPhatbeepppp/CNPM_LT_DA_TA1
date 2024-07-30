package com.example.cnpm_lt_da_ta.Manager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.DAO.FlashcardDAO;
import com.example.cnpm_lt_da_ta.R;

import java.io.IOException;

public class FlashcardEditActivity extends AppCompatActivity {

    private EditText etWord, etMeaning, etPronunciation;
    private ImageView ivFlashcardImage;
    private FlashcardDAO flashcardDAO;
    private ImageButton btnback;
    private int flashcardId;
    private Uri selectedImageUri; // Lưu trữ Uri của ảnh đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_create);

        etWord = findViewById(R.id.etWord);
        etMeaning = findViewById(R.id.etMeaning);
        etPronunciation = findViewById(R.id.etPronunciation);
        ivFlashcardImage = findViewById(R.id.ivFlashcardImage);

        flashcardDAO = new FlashcardDAO(this);
        flashcardDAO.open();

        flashcardId = getIntent().getIntExtra("flashcardId", -1);
        loadFlashcard();
        btnback = findViewById(R.id.btn_back);
        btnback.setOnClickListener(v -> {
            super.finish();
        });
        Button btnUpdateFlashcard = findViewById(R.id.btnSaveFlashcard);
        btnUpdateFlashcard.setOnClickListener(v -> updateFlashcard());

        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(v -> openImageChooser());

        // Khởi tạo ActivityResultLauncher để nhận kết quả từ Intent chọn ảnh
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            ivFlashcardImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Lỗi khi tải ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    private void loadFlashcard() {
        Flashcard flashcard = flashcardDAO.getFlashcardById(flashcardId);
        if (flashcard != null) {
            etWord.setText(flashcard.getWord());
            etMeaning.setText(flashcard.getMeaning());
            etPronunciation.setText(flashcard.getPronunciation());
            // ... (Tải và hiển thị ảnh nếu có)
        } else {
            Toast.makeText(this, "Không tìm thấy flashcard", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateFlashcard() {
        String word = etWord.getText().toString().trim();
        String meaning = etMeaning.getText().toString().trim();
        String pronunciation = etPronunciation.getText().toString().trim();
        String image = selectedImageUri != null ? selectedImageUri.toString() : ""; // Lấy đường dẫn ảnh nếu có

        if (word.isEmpty() || meaning.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập từ vựng và nghĩa", Toast.LENGTH_SHORT).show();
            return;
        }

        Flashcard flashcard = new Flashcard(flashcardId, word, meaning, pronunciation, image, 0); // Giả sử flashcardSetId không thay đổi
        flashcardDAO.updateFlashcard(flashcard);

        finish();
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flashcardDAO.close();
    }
}

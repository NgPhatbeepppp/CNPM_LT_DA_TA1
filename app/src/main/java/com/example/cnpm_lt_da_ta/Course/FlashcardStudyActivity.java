package com.example.cnpm_lt_da_ta.Course;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cnpm_lt_da_ta.Adapter.FlashcardAdapter;
import com.example.cnpm_lt_da_ta.DAO.FlashcardDAO;
import com.example.cnpm_lt_da_ta.R;
import com.example.cnpm_lt_da_ta.fragment.FlashcardFragment;

import java.util.List;

public class FlashcardStudyActivity extends AppCompatActivity {

    private ViewPager2 viewPagerFlashcards;
    private FlashcardDAO flashcardDAO;
    private FlashcardAdapter flashcardAdapter;

    private Button btnPrevious, btnFlip, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_study);

        viewPagerFlashcards = findViewById(R.id.viewPagerFlashcards);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnFlip = findViewById(R.id.btnFlip);
        btnNext = findViewById(R.id.btnNext);

        flashcardDAO = new FlashcardDAO(this);
        flashcardDAO.open();

        int flashcardSetId = getIntent().getIntExtra("flashcardSetId", -1);
        if (flashcardSetId != -1) {
            List<Flashcard> flashcards = flashcardDAO.getFlashcardsBySetId(flashcardSetId);
            flashcardAdapter = new FlashcardAdapter(flashcards, this);
            viewPagerFlashcards.setAdapter(flashcardAdapter);
            viewPagerFlashcards.setOffscreenPageLimit(2); // Giữ lại 2 Fragment xung quanh
        } else {

        }

        btnPrevious.setOnClickListener(v -> {
            int currentItem = viewPagerFlashcards.getCurrentItem();
            if (currentItem > 0) {
                viewPagerFlashcards.setCurrentItem(currentItem - 1);
            }
        });
        btnNext.setOnClickListener(v -> {
            int currentItem = viewPagerFlashcards.getCurrentItem();
            if (currentItem < flashcardAdapter.getItemCount() - 1) {
                viewPagerFlashcards.setCurrentItem(currentItem + 1);
            }
        });

        btnFlip.setOnClickListener(v -> {
            int currentItem = viewPagerFlashcards.getCurrentItem();
            String tag = "f" + currentItem;

            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (currentFragment instanceof FlashcardFragment) {
                ((FlashcardFragment) currentFragment).flipCard();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flashcardDAO.close();
    }
}

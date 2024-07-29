package com.example.cnpm_lt_da_ta.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.fragment.FlashcardFragment;
import com.example.cnpm_lt_da_ta.Course.FlashcardStudyActivity; // Import FlashcardStudyActivity

import java.util.List;

public class FlashcardAdapter extends FragmentStateAdapter {
    private List<Flashcard> flashcards;
    private Context context;

    public FlashcardAdapter(List<Flashcard> flashcards, FlashcardStudyActivity activity) {
        super(activity.getSupportFragmentManager(), activity.getLifecycle());
        this.flashcards = flashcards;
        this.context = activity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Flashcard flashcard = flashcards.get(position);
        return FlashcardFragment.newInstance(flashcard); // Truyền đối tượng Flashcard
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}
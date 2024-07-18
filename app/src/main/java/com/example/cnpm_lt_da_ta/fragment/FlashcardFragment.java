package com.example.cnpm_lt_da_ta.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.R;

public class FlashcardFragment extends Fragment {

    private Flashcard flashcard;
    private TextView tvWord, tvMeaning, tvPronunciation;
    private ImageView ivFlashcardImage;
    private CardView cardViewFlashcard; // Thêm biến cardViewFlashcard
    private boolean isFlipped = false;

    public static FlashcardFragment newInstance(Flashcard flashcard) {
        FlashcardFragment fragment = new FlashcardFragment();
        Bundle args = new Bundle();
        args.putParcelable("flashcard",flashcard);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flashcard = getArguments().getParcelable("flashcard");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_flashcard, container, false);
        tvWord = view.findViewById(R.id.tvWord);
        tvMeaning = view.findViewById(R.id.tvMeaning);
        tvPronunciation = view.findViewById(R.id.tvPronunciation);
        ivFlashcardImage = view.findViewById(R.id.ivFlashcardImage);
        cardViewFlashcard = view.findViewById(R.id.cardViewFlashcard); // Khởi tạo cardViewFlashcard

        if (flashcard != null) {
            tvWord.setText(flashcard.getWord());
            tvMeaning.setText(flashcard.getMeaning());
            tvPronunciation.setText(flashcard.getPronunciation());

            if (flashcard.getImage() != null && !flashcard.getImage().isEmpty()) {
                int imageResourceId = getResources().getIdentifier(flashcard.getImage(), "drawable", requireContext().getPackageName());
                Glide.with(this).load(imageResourceId).into(ivFlashcardImage);
                ivFlashcardImage.setVisibility(View.GONE);
            } else {
                ivFlashcardImage.setVisibility(View.GONE);
            }
        }

        view.setOnClickListener(v -> flipCard());

        return view;
    }

    public void flipCard() {
        isFlipped = !isFlipped;

        // Sử dụng ObjectAnimator để tạo hiệu ứng lật mượt mà
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cardViewFlashcard, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(cardViewFlashcard, "scaleX", 0f, 1f);
        oa1.setInterpolator(new AccelerateDecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // Đổi nội dung thẻ ở đây
                tvWord.setVisibility(isFlipped ? View.GONE : View.VISIBLE);
                tvMeaning.setVisibility(isFlipped ? View.VISIBLE : View.GONE);
                tvPronunciation.setVisibility(isFlipped ? View.VISIBLE : View.GONE);
                ivFlashcardImage.setVisibility(isFlipped && flashcard.getImage() != null ? View.VISIBLE : View.GONE);
                oa2.start();
            }
        });
        oa1.start();
    }
}

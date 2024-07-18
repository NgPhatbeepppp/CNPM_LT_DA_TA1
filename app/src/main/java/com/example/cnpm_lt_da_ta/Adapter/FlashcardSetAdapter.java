package com.example.cnpm_lt_da_ta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.Course.CourseDetailActivity;
import com.example.cnpm_lt_da_ta.Course.FlashcardSet;
import com.example.cnpm_lt_da_ta.Course.FlashcardStudyActivity;
import com.example.cnpm_lt_da_ta.DAO.FlashcardDAO;
import com.example.cnpm_lt_da_ta.R;

import java.util.List;

public class FlashcardSetAdapter extends RecyclerView.Adapter<FlashcardSetAdapter.FlashcardSetViewHolder> {
    private List<FlashcardSet> flashcardSets;
    private Context context;
    private FlashcardDAO flashcardDAO;

    public FlashcardSetAdapter(List<FlashcardSet> flashcardSets, Context context) {
        this.flashcardSets = flashcardSets;
        this.context = context;
        this.flashcardDAO = new FlashcardDAO(context); // Truyền context trực tiếp
        flashcardDAO.open();
    }


    @NonNull
    @Override
    public FlashcardSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard_set, parent, false);
        return new FlashcardSetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardSetViewHolder holder, int position) {
        FlashcardSet flashcardSet = flashcardSets.get(position);
        holder.tvFlashcardSetName.setText(flashcardSet.getName());

        int flashcardCount = flashcardDAO.getFlashcardsBySetId(flashcardSet.getId()).size();
        holder.tvFlashcardCount.setText("Số lượng Flashcard: " + flashcardCount);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flashcardSetId = flashcardSet.getId();
                Intent intent = new Intent(context, FlashcardStudyActivity.class);
                intent.putExtra("flashcardSetId", flashcardSetId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashcardSets.size();
    }

    public static class FlashcardSetViewHolder extends RecyclerView.ViewHolder {
        TextView tvFlashcardSetName, tvFlashcardCount;

        public FlashcardSetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFlashcardSetName = itemView.findViewById(R.id.tvFlashcardSetName);
            tvFlashcardCount = itemView.findViewById(R.id.tvFlashcardCount);
        }
    }
}

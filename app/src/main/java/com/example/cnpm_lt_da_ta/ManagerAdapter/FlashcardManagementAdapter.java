package com.example.cnpm_lt_da_ta.ManagerAdapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.Manager.FlashcardEditActivity;
import com.example.cnpm_lt_da_ta.R;

import java.util.ArrayList;
import java.util.List;

public class FlashcardManagementAdapter extends RecyclerView.Adapter<FlashcardManagementAdapter.ViewHolder> implements Filterable {

    private List<Flashcard> flashcardList;
    private List<Flashcard> flashcardListFiltered;
    private Context context;

    public FlashcardManagementAdapter(List<Flashcard> flashcardList, Context context) {
        this.flashcardList = flashcardList;
        this.flashcardListFiltered = flashcardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flashcard flashcard = flashcardListFiltered.get(position);
        holder.tvWord.setText(flashcard.getWord());
        holder.tvMeaning.setText(flashcard.getMeaning());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FlashcardEditActivity.class);
            intent.putExtra("flashcardId", flashcard.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flashcardListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    flashcardListFiltered = flashcardList;
                } else {
                    List<Flashcard> filteredList = new ArrayList<>();
                    for (Flashcard row : flashcardList) {
                        if (row.getWord().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    flashcardListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = flashcardListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                flashcardListFiltered = (ArrayList<Flashcard>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord, tvMeaning;
        //ImageView ivFlashcardImage; // Nếu bạn muốn hiển thị ảnh
        //TextView tvPronunciation; // Nếu bạn muốn hiển thị phiên âm

        ViewHolder(View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            // ivFlashcardImage = itemView.findViewById(R.id.ivFlashcardImage);
            // tvPronunciation = itemView.findViewById(R.id.tvPronunciation);
        }
    }
}

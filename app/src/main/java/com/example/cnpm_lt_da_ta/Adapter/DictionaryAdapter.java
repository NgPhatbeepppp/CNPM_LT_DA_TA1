package com.example.cnpm_lt_da_ta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.Course.Dictionary; // Import lớp Dictionary
import com.example.cnpm_lt_da_ta.R;

import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder> {

    private List<Dictionary> dictionaries; // Danh sách các từ vựng
    private Context context;

    public DictionaryAdapter(List<Dictionary> dictionaries, Context context) {
        this.dictionaries = dictionaries;
        this.context = context;
    }

    @NonNull
    @Override
    public DictionaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dictionary, parent, false);
        return new DictionaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryViewHolder holder, int position) {
        Dictionary dictionary = dictionaries.get(position);
        holder.tvWord.setText(dictionary.getWord());
        holder.tvPronunciation.setText(dictionary.getPronunciation());
        holder.tvMeaning.setText(dictionary.getMeaning());
        holder.tvType.setText("Loại từ: " + dictionary.getType());
        holder.tvExample.setText("Ví dụ: " + dictionary.getExample());
    }

    @Override
    public int getItemCount() {
        return dictionaries.size();
    }

    // ViewHolder
    public static class DictionaryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvWord, tvPronunciation, tvMeaning, tvType, tvExample;

        public DictionaryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvPronunciation = itemView.findViewById(R.id.tvPronunciation);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            tvType = itemView.findViewById(R.id.tvType);
            tvExample = itemView.findViewById(R.id.tvExample);
        }
    }

    // Phương thức để cập nhật dữ liệu cho Adapter (khi có kết quả tìm kiếm mới)
    public void updateData(List<Dictionary> newDictionaries) {
        dictionaries = newDictionaries;
        notifyDataSetChanged();
    }
}

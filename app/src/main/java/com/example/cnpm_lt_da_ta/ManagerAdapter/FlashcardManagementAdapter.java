package com.example.cnpm_lt_da_ta.ManagerAdapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.Course.Flashcard;
import com.example.cnpm_lt_da_ta.DAO.FlashcardDAO;
import com.example.cnpm_lt_da_ta.Manager.FlashcardEditActivity;
import com.example.cnpm_lt_da_ta.R;

import java.util.ArrayList;
import java.util.List;

public class FlashcardManagementAdapter extends RecyclerView.Adapter<FlashcardManagementAdapter.ViewHolder> implements Filterable {

    private final FlashcardDAO flashcardDAO;
    private List<Flashcard> flashcardList;
    private List<Flashcard> flashcardListFiltered;
    private Context context;
    private boolean isSelectionMode;
    private OnItemClickListener onItemClickListener;
    private List<Flashcard> selectedFlashcards = new ArrayList<>();
    public FlashcardManagementAdapter(List<Flashcard> flashcardList, Context context) {
        this.flashcardList = flashcardList;
        this.flashcardListFiltered = flashcardList;
        this.context = context;
        this.flashcardDAO = new FlashcardDAO(context);
        this.flashcardDAO.open(); // Mở kết nối đến cơ sở dữ liệu
        this.isSelectionMode = isSelectionMode;
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
        // Cập nhật trạng thái hiển thị của CheckBox dựa trên isSelectionMode
        holder.cbFlashcard.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
        holder.cbFlashcard.setChecked(selectedFlashcards.contains(flashcard)); // Cập nhật trạng thái checkbox
        holder.btnEditFlashcard.setOnClickListener(v -> {
            Intent intent = new Intent(context, FlashcardEditActivity.class);
            intent.putExtra("flashcardId", flashcard.getId());
            context.startActivity(intent);
        });

        holder.btnDeleteFlashcard.setOnClickListener(v -> {
            // Hiển thị AlertDialog xác nhận xóa
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa flashcard này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        flashcardDAO.deleteFlashcard(flashcard.getId());
                        flashcardListFiltered.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(flashcardListFiltered.get(position));
            }
        });
        // Xử lý sự kiện click vào checkbox
        holder.cbFlashcard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedFlashcards.add(flashcard);
            } else {
                selectedFlashcards.remove(flashcard);
            }
            // Thông báo cho Activity hoặc Fragment biết về sự thay đổi (nếu cần)
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(flashcard);
            }
            notifyItemChanged(position);
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
        CheckBox cbFlashcard; // Thêm checkbox vào ViewHolder
        ImageView btnEditFlashcard, btnDeleteFlashcard;
        ViewHolder(View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvMeaning = itemView.findViewById(R.id.tvMeaning);
            btnEditFlashcard = itemView.findViewById(R.id.btnEditFlashcard);
            btnDeleteFlashcard = itemView.findViewById(R.id.btnDeleteFlashcard);
            cbFlashcard = itemView.findViewById(R.id.cbFlashcard);
        }

    }
    // Interface OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(Flashcard flashcard);
    }


    // Setter cho OnItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    // Phương thức để lấy danh sách các flashcard đã chọn
    public List<Flashcard> getSelectedFlashcards() {
        return selectedFlashcards;
    }

    // Phương thức để đặt chế độ chọn
    public void setSelectionMode(boolean selectionMode) {
        this.isSelectionMode = selectionMode;
        notifyDataSetChanged();
    }
}

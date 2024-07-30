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

import com.example.cnpm_lt_da_ta.DAO.FlashcardSetDAO;
import com.example.cnpm_lt_da_ta.Manager.FlashcardSetEditActivity;
import com.example.cnpm_lt_da_ta.Course.FlashcardSet;
import com.example.cnpm_lt_da_ta.R;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetManagementAdapter extends RecyclerView.Adapter<FlashcardSetManagementAdapter.ViewHolder> implements Filterable {

    private final FlashcardSetDAO flashcardSetDAO;
    private List<FlashcardSet> flashcardSetList;
    private List<FlashcardSet> flashcardSetListFiltered;
    private Context context;
    private boolean isSelectionMode = false;
    private List<FlashcardSet> selectedFlashcardSets = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public FlashcardSetManagementAdapter(List<FlashcardSet> flashcardSetList, Context context) {
        this.flashcardSetList = flashcardSetList;
        this.flashcardSetListFiltered = flashcardSetList;
        this.context = context;
        this.flashcardSetDAO = new FlashcardSetDAO(context);
        this.flashcardSetDAO.open();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_set_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlashcardSet flashcardSet = flashcardSetListFiltered.get(position);
        holder.tvFlashcardSetName.setText(flashcardSet.getName());

        // Hiển thị/ẩn checkbox dựa trên chế độ
        holder.cbFlashcardSet.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
        holder.cbFlashcardSet.setChecked(selectedFlashcardSets.contains(flashcardSet));

        holder.btnEditFlashcardSet.setOnClickListener(v -> {
            if (isSelectionMode) {
                holder.cbFlashcardSet.setChecked(!holder.cbFlashcardSet.isChecked());
            } else {
                Intent intent = new Intent(context, FlashcardSetEditActivity.class);
                intent.putExtra("flashcardSetId", flashcardSet.getId());
                context.startActivity(intent);
            }
        });

        holder.btnDeleteFlashcardSet.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa bộ flashcard này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        flashcardSetDAO.deleteFlashcardSet(flashcardSet.getId());
                        flashcardSetListFiltered.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        holder.cbFlashcardSet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedFlashcardSets.add(flashcardSet);
            } else {
                selectedFlashcardSets.remove(flashcardSet);
            }
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(flashcardSet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashcardSetListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    flashcardSetListFiltered = flashcardSetList;
                } else {
                    List<FlashcardSet> filteredList = new ArrayList<>();
                    for (FlashcardSet row : flashcardSetList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    flashcardSetListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = flashcardSetListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                flashcardSetListFiltered = (ArrayList<FlashcardSet>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFlashcardSetName;
        ImageView btnEditFlashcardSet, btnDeleteFlashcardSet;
        CheckBox cbFlashcardSet;

        ViewHolder(View itemView) {
            super(itemView);
            tvFlashcardSetName = itemView.findViewById(R.id.tvFlashcardSetName);
            btnEditFlashcardSet = itemView.findViewById(R.id.btnEditFlashcardSet);
            btnDeleteFlashcardSet = itemView.findViewById(R.id.btnDeleteFlashcardSet);
            cbFlashcardSet = itemView.findViewById(R.id.cbFlashcardSet);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(FlashcardSet flashcardSet);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setSelectionMode(boolean selectionMode) {
        this.isSelectionMode = selectionMode;
        notifyDataSetChanged();
    }

    public List<FlashcardSet> getSelectedFlashcardSets() {
        return selectedFlashcardSets;
    }
}

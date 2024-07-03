package com.example.cnpm_lt_da_ta.User;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onEditClick(User user);
        void onDeleteClick(User user);
    }

    public UserAdapter(List<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);  
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        if (user != null) {
            holder.textViewName.setText(user.getName() != null ? user.getName() : "Chưa có tên");
            holder.textViewEmail.setText(user.getEmail() != null ? user.getEmail() : "Chưa có email");
            holder.textViewPhone.setText(user.getPhone());
            holder.textViewRole.setText(user.getRole() != null ? user.getRole() : "Chưa có vai trò");
        } else {
            Log.e("UserAdapter", "User object is null at position " + position);
        }

        holder.buttonEditUser.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(user);
            }
        });

        holder.buttonDeleteUser.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateUsers(List<User> newUsers) {
        userList = newUsers;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewEmail, textViewRole, textViewPhone;
        ImageButton buttonEditUser, buttonDeleteUser;

        UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_user_name);
            textViewEmail = itemView.findViewById(R.id.text_view_user_email);
            textViewRole = itemView.findViewById(R.id.text_view_user_role);
            textViewPhone = itemView.findViewById(R.id.text_view_user_phone);
            buttonEditUser = itemView.findViewById(R.id.button_edit_user);
            buttonDeleteUser = itemView.findViewById(R.id.button_delete_user);
        }
    }
}


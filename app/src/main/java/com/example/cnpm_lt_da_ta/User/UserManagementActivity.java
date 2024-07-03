package com.example.cnpm_lt_da_ta.User;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.Lesson.LessonManagementActivity;
import com.example.cnpm_lt_da_ta.MainActivity;
import com.example.cnpm_lt_da_ta.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private DatabaseReference usersRef;
    private EditText editTextSearch;
    private FloatingActionButton fabAddUser;
    private List<User> userList = new ArrayList<>(); // Danh sách người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        recyclerViewUsers = findViewById(R.id.recycler_view_users);
        editTextSearch = findViewById(R.id.edit_text_search_user);
        fabAddUser = findViewById(R.id.fab_add_user);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList, this);
        recyclerViewUsers.setAdapter(userAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId(); // Lấy itemId

            if (itemId == R.id.nav_home) {
                // Chuyển đến MainActivity
                startActivity(new Intent(UserManagementActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_back) {
                onBackPressed();
                return true;

            } else if (itemId == R.id.nav_users) {
                // Chuyển đến UserManagementActivity
                startActivity(new Intent(UserManagementActivity.this, UserManagementActivity.class));
                finish();
                return true;
            }

            return false; // Trả về false nếu không xử lý được itemId
        });



        // Lấy danh sách người dùng từ Firebase
        fetchUsers();

        // Xử lý sự kiện tìm kiếm
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });

        fabAddUser.setOnClickListener(v -> {
            // Chuyển đến RegisterActivity
            Intent intent = new Intent(UserManagementActivity.this, RegisterActivity.class);
            startActivityForResult(intent, 1); // hoặc REQUEST_CODE nếu bạn định nghĩa hằng số này
        });
    }

    private void fetchUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setUserId(userSnapshot.getKey()); // Đặt userId cho đối tượng User
                        userList.add(user);
                    } else {
                        // Xử lý trường hợp user là null (ví dụ: ghi log lỗi)
                        Log.e("UserManagementActivity", "Null user object at: " + userSnapshot.getKey());
                    }
                }
                userAdapter.updateUsers(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
                Log.e("UserManagementActivity", "Lỗi khi lấy danh sách người dùng: " + databaseError.getMessage());
            }
        });
    }



    private void searchUsers(String query) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : userList) {
            if (user.getName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }
        userAdapter.updateUsers(filteredUsers);
    }

   @Override
    public void onEditClick(User user) {
         //Chuyển đến EditUserActivity và truyền dữ liệu user
       Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra("user", user);
       startActivity(intent);
   }

    @Override
    public void onDeleteClick(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa người dùng này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    usersRef.child(user.getUserId()).removeValue();
                    userList.remove(user);
                    userAdapter.updateUsers(userList);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Cập nhật lại danh sách người dùng khi thêm user mới
            fetchUsers();
        }
    }

}

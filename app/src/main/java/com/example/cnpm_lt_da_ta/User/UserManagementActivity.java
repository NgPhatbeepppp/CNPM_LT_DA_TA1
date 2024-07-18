package com.example.cnpm_lt_da_ta.User;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.DatabaseHelper;
import com.example.cnpm_lt_da_ta.R;
import com.example.cnpm_lt_da_ta.fragment.HomeFragment;
import com.example.cnpm_lt_da_ta.fragment.MyProfileFragment;
import com.example.cnpm_lt_da_ta.fragment.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_NEWS = 2;
    public static final int FRAGMENT_USER = 3;
    private  int mCurrentFRAGMENT =1;
    private List<User> userList = new ArrayList<>(); // Danh sách người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        recyclerViewUsers = findViewById(R.id.recycler_view_users);
        editTextSearch = findViewById(R.id.edit_text_search_user);
        fabAddUser = findViewById(R.id.fab_add_user);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(userList, this);
        recyclerViewUsers.setAdapter(userAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replacefragment(new HomeFragment());
                mCurrentFRAGMENT = FRAGMENT_HOME;
                return true;
            } else if (itemId == R.id.news) {
                replacefragment(new NewsFragment());
                mCurrentFRAGMENT = FRAGMENT_NEWS;
                return true;
            } else if (itemId == R.id.user) {
                replacefragment(new MyProfileFragment());
                mCurrentFRAGMENT = FRAGMENT_USER;
                return true;
            } else if (itemId == R.id.back) {
                onBackPressed();
                return true;
            } else {
                // Xử lý trường hợp không có itemId nào khớp
                return false;
            }
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
                    }
                }
                userAdapter.updateUsers(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UserManagementActivity", "Lỗi khi lấy danh sách người dùng", databaseError.toException());
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
    private void replacefragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

    }
}

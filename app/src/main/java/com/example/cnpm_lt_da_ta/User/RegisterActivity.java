package com.example.cnpm_lt_da_ta.User;

// ... các import

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cnpm_lt_da_ta.R;
import com.example.cnpm_lt_da_ta.fragment.HomeFragment;
import com.example.cnpm_lt_da_ta.fragment.MyProfileFragment;
import com.example.cnpm_lt_da_ta.fragment.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextName, editTextPhone;
    private RadioGroup radioGroupRole;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_NEWS = 2;
    public static final int FRAGMENT_USER = 3;
    private  int mCurrentFRAGMENT =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các view
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextName = findViewById(R.id.edit_text_name);
        editTextPhone = findViewById(R.id.edit_text_phone);
        radioGroupRole = findViewById(R.id.radio_group_role);
        Button buttonRegister = findViewById(R.id.button_register);
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

        // Khởi tạo Firebase Authentication và Realtime Database reference
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        buttonRegister.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();
            String role = selectedRoleId == R.id.radio_button_teacher ? "teacher" : "student";

            // Kiểm tra các trường bắt buộc
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo tài khoản mới trên Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Đăng ký thành công
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();

                                // Lưu thông tin người dùng vào Realtime Database
                                User newUser = new User(userId, email, role, name, phone);
                                usersRef.child(userId).setValue(newUser)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                            finish(); // Đóng RegisterActivity
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(RegisterActivity.this, "Lỗi khi lưu thông tin người dùng", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            // Đăng ký thất bại
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
    private void replacefragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

    }
}

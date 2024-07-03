package com.example.cnpm_lt_da_ta.User;

// ... các import

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cnpm_lt_da_ta.MainActivity;
import com.example.cnpm_lt_da_ta.R;
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


        // Khởi tạo Firebase Authentication và Realtime Database reference
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId(); // Lấy itemId

            if (itemId == R.id.nav_home) {
                // Chuyển đến MainActivity
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_back) {
                onBackPressed();
                return true;

            } else if (itemId == R.id.nav_users) {
                // Chuyển đến UserManagementActivity
                startActivity(new Intent(RegisterActivity.this, UserManagementActivity.class));
                finish();
                return true;
            }

            return false; // Trả về false nếu không xử lý được itemId
        });

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
}

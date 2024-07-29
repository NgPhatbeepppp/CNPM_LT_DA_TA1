package com.example.cnpm_lt_da_ta.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cnpm_lt_da_ta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhoneNumber, editTextPassword;
    private Button buttonSave;

    private ImageButton btnBack;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Ánh xạ các view
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextPassword = findViewById(R.id.edit_text_password);
        buttonSave = findViewById(R.id.button_save);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            super.finish();
        });
        // Khởi tạo tham chiếu đến node "users" trên Firebase Realtime Database
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Lấy thông tin user từ Intent
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        // Hiển thị thông tin user lên EditText
        if (user != null) {
            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
            editTextPhoneNumber.setText(user.getPhone());
        }

        // Xử lý sự kiện khi nhấn nút "Lưu"
        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();

            // Kiểm tra dữ liệu hợp lệ
            if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật thông tin user lên Firebase
            if (user != null) {
                DatabaseReference userRef = usersRef.child(user.getUserId());
                userRef.child("name").setValue(name);
                userRef.child("email").setValue(email);
                userRef.child("phone").setValue(phoneNumber);

                // Cập nhật mật khẩu (nếu có)
                String newPassword = editTextPassword.getText().toString().trim();
                if (!newPassword.isEmpty()) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        firebaseUser.updatePassword(newPassword)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditUserActivity.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditUserActivity.this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity sau khi cập nhật
            }
        });
    }
}


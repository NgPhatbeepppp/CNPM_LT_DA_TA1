package com.example.cnpm_lt_da_ta.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cnpm_lt_da_ta.MainActivity;
import com.example.cnpm_lt_da_ta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private CheckBox chkRememberMe;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các view
        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        chkRememberMe = findViewById(R.id.chkRememberMe);

        // Kiểm tra nếu người dùng đã chọn "Remember Me"
        if (sharedPreferences.getBoolean("remember", false)) {
            email.setText(sharedPreferences.getString("email", ""));
            password.setText(sharedPreferences.getString("password", ""));
            chkRememberMe.setChecked(true);
        }

        // Xử lý sự kiện click "Quên mật khẩu"
        TextView forgotPasswordText = findViewById(R.id.tv_forgot_password);
        forgotPasswordText.setOnClickListener(v -> {
            // Mở ForgotPasswordActivity khi người dùng nhấp vào "Quên mật khẩu"
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    // Phương thức xử lý đăng nhập
    public void signin(View view) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Enter User Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Enter User PassWord", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 6) {
            Toast.makeText(this, "PassWord needs 6 letters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng nhập Firebase
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    // Lưu thông tin đăng nhập nếu người dùng chọn "Remember Me"
                    if (chkRememberMe.isChecked()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", userEmail);
                        editor.putString("password", userPassword);
                        editor.putBoolean("remember", true);
                        editor.apply();
                    } else {
                        sharedPreferences.edit().clear().apply(); // Xóa dữ liệu nếu bỏ chọn
                    }

                    // Chuyển đến MainActivity sau khi đăng nhập thành công
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

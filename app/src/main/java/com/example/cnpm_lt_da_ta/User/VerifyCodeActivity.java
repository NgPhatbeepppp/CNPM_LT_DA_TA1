package com.example.cnpm_lt_da_ta.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cnpm_lt_da_ta.R;

public class VerifyCodeActivity extends AppCompatActivity {

    private EditText verificationCodeEditText;
    private Button verifyCodeButton;

    // Tạo biến để lưu mã xác nhận
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        verificationCodeEditText = findViewById(R.id.edt_verification_code);
        verifyCodeButton = findViewById(R.id.btn_verify_code);

        // Giả sử bạn đã nhận được mã xác nhận qua email từ GMailSender
        verificationCode = getVerificationCodeFromSharedPreferences(); // Lấy mã từ SharedPreferences

        verifyCodeButton.setOnClickListener(v -> {
            String enteredCode = verificationCodeEditText.getText().toString();

            // Kiểm tra mã xác nhận nhập vào có hợp lệ không
            if (isCodeValid(enteredCode)) {
                // Xử lý logic sau khi mã hợp lệ
                Toast.makeText(this, "Code verified successfully", Toast.LENGTH_SHORT).show();
                // Tiến hành thay đổi mật khẩu hoặc các thao tác khác
            } else {
                Toast.makeText(this, "Invalid verification code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Kiểm tra mã nhập vào có đúng với mã đã gửi không
    private boolean isCodeValid(String enteredCode) {
        return verificationCode != null && verificationCode.equals(enteredCode); // Kiểm tra mã nhập vào
    }

    // Lưu mã xác nhận vào SharedPreferences
    private void saveVerificationCodeToSharedPreferences(String code) {
        SharedPreferences sharedPreferences = getSharedPreferences("VerificationPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("verification_code", code);
        editor.apply();
    }

    // Lấy mã xác nhận từ SharedPreferences
    private String getVerificationCodeFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("VerificationPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("verification_code", null);
    }
}

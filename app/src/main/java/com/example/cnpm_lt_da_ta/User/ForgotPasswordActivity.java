package com.example.cnpm_lt_da_ta.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cnpm_lt_da_ta.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText, verificationCodeEditText, newPasswordEditText, confirmPasswordEditText;
    private Button sendCodeButton, verifyCodeButton, savePasswordButton;
    private String verificationCode; // Mã xác nhận gửi từ email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.edt_email);
        verificationCodeEditText = findViewById(R.id.edt_verification_code);
        newPasswordEditText = findViewById(R.id.edt_new_password);
        confirmPasswordEditText = findViewById(R.id.edt_confirm_password);
        sendCodeButton = findViewById(R.id.btn_send_code);
        verifyCodeButton = findViewById(R.id.btn_verify_code);
        savePasswordButton = findViewById(R.id.btn_save_password);

        // Ban đầu, ẩn phần nhập mã xác nhận và mật khẩu
        verificationCodeEditText.setVisibility(View.GONE);
        verifyCodeButton.setVisibility(View.GONE);
        newPasswordEditText.setVisibility(View.GONE);
        confirmPasswordEditText.setVisibility(View.GONE);
        savePasswordButton.setVisibility(View.GONE);

        // Xử lý gửi mã xác nhận
        sendCodeButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            if (!TextUtils.isEmpty(email)) {
                new SendVerificationCodeTask().execute(email);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý xác nhận mã
        verifyCodeButton.setOnClickListener(v -> {
            String enteredCode = verificationCodeEditText.getText().toString();
            if (!TextUtils.isEmpty(enteredCode)) {
                if (isCodeValid(enteredCode)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Code verified successfully.", Toast.LENGTH_SHORT).show();
                    showPasswordFields();  // Hiển thị phần nhập mật khẩu mới
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid verification code.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter the verification code.", Toast.LENGTH_SHORT).show();
            }
        });

        // Lưu mật khẩu mới
        savePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (!TextUtils.isEmpty(newPassword) && newPassword.equals(confirmPassword)) {
                // Lưu mật khẩu mới
                updatePassword(newPassword);
                Toast.makeText(ForgotPasswordActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();

                // Quay lại màn hình đăng nhập
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class); // Chuyển về màn hình đăng nhập
                startActivity(intent);
                finish(); // Đảm bảo thoát khỏi activity hiện tại
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPasswordFields() {
        newPasswordEditText.setVisibility(View.VISIBLE);
        confirmPasswordEditText.setVisibility(View.VISIBLE);
        savePasswordButton.setVisibility(View.VISIBLE);
    }

    private void updatePassword(String newPassword) {
        // Cập nhật mật khẩu mới (Ví dụ: Firebase)
        // FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword);

        // Nếu sử dụng SharedPreferences, bạn có thể lưu mật khẩu ở đây
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("new_password", newPassword);
        editor.apply();
    }

    // Kiểm tra mã xác nhận nhập vào có đúng không
    private boolean isCodeValid(String enteredCode) {
        String savedCode = getVerificationCodeFromSharedPreferences();
        return savedCode != null && savedCode.equals(enteredCode);
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

    // AsyncTask để gửi mã xác nhận trong background
    private class SendVerificationCodeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... emails) {
            String email = emails[0];
            GMailSender gmailSender = new GMailSender();
            String verificationCode = gmailSender.sendVerificationCode(email); // Gửi mã xác nhận qua email và nhận mã
            return verificationCode;
        }

        @Override
        protected void onPostExecute(String verificationCode) {
            super.onPostExecute(verificationCode);
            // Lưu mã xác nhận vào SharedPreferences
            saveVerificationCodeToSharedPreferences(verificationCode);
            // Hiển thị thông báo sau khi gửi mã thành công
            Toast.makeText(ForgotPasswordActivity.this, "Verification code sent to your email.", Toast.LENGTH_SHORT).show();
            verificationCodeEditText.setVisibility(View.VISIBLE);
            verifyCodeButton.setVisibility(View.VISIBLE);
        }
    }
}

package com.example.appnamkc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appnamkc.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        // View binding
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);

        btnLogin.setOnClickListener(v -> loginUser());

        findViewById(R.id.tv_forgot_password).setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));

        findViewById(R.id.tv_register_link).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = etEmail.getText() != null
                ? etEmail.getText().toString().trim()
                : "";

        String password = etPassword.getText() != null
                ? etPassword.getText().toString().trim()
                : "";

        // Validate
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu tối thiểu 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        showLoading(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    showLoading(false);

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        navigateByRole(email);
                    } else {
                        handleLoginError(task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(
                            this,
                            "Lỗi đăng nhập: " + (e.getMessage() != null ? e.getMessage() : "Vui lòng thử lại"),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }

    private void navigateByRole(String email) {
        Intent intent;

        if (email.contains("@admin")) {
            // Tài khoản quản lý
            intent = new Intent(this, CategoryActivity.class);
        } else {
            // Tài khoản người dùng thường
            intent = new Intent(this, HomeActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void handleLoginError(Exception exception) {
        String msg = "Email hoặc mật khẩu không đúng";

        if (exception != null && exception.getMessage() != null) {
            String error = exception.getMessage();

            if (error.contains("network")) {
                msg = "Lỗi kết nối mạng. Vui lòng thử lại";
            } else if (error.contains("user-not-found")) {
                msg = "Email không tồn tại";
            } else if (error.contains("wrong-password")) {
                msg = "Mật khẩu không đúng";
            }
        }

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!isLoading);
    }
}

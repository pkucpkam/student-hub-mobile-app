package com.tdtu.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btn_logOut, btn_viewListUser, btn_viewHisLogin, btn_studentMage, btn_viewProfile, btn_import_export;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        btn_logOut = findViewById(R.id.btn_logOut);
        btn_viewListUser = findViewById(R.id.btn_viewListUser);
        btn_viewHisLogin = findViewById(R.id.btn_viewHisLogin);
        btn_studentMage = findViewById(R.id.btn_studentMage);
        btn_viewProfile = findViewById(R.id.btn_viewProfile);
        btn_import_export = findViewById(R.id.btn_import_export);

        // Lấy vai trò người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");

        // Check role để phân quyền
        checkRole(role);

        // Xử lý đăng xuất
        btn_logOut.setOnClickListener(v -> {
            mAuth.signOut();
            clearUserSession();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Điều hướng sang màn hình profile

        btn_viewProfile.setOnClickListener(v -> {
            String userId = sharedPreferences.getString("userId", "");

            // Tạo intent và đính kèm dữ liệu
            Intent intent = new Intent(MainActivity.this, UserMainActivity.class);
            intent.putExtra("userId", userId); // Truyền userId qua Intent
            startActivity(intent);
        });

        // Điều hướng sang màn hình danh sách người dùng
        btn_viewListUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserManagementActivity.class);
            startActivity(intent);
        });

        // Điều hướng sang màn hình xem lịch sử đăng nhập
        btn_viewHisLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // Điều hướng sang màn hình quản lí sinh viên
        btn_studentMage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentManagementActivity.class);
            startActivity(intent);
        });

        btn_import_export.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImportorExportActivity.class);
            startActivity(intent);
        });
    }

    // Xóa dữ liệu trong session
    private void clearUserSession() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void checkRole(String role) {
        if (!"admin".equals(role)) {
            btn_viewListUser.setVisibility(View.GONE);
        }
    }
}

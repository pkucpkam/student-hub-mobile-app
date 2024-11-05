package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btn_logOut, btn_viewListUser, btn_viewHisLogin, btn_editProfile;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        btn_logOut = findViewById(R.id.btn_logOut);
        btn_viewListUser = findViewById(R.id.btn_viewListUser);
        btn_viewHisLogin = findViewById(R.id.btn_viewHisLogin);
        btn_editProfile = findViewById(R.id.btn_editProfile);

        btn_logOut.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
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

        // Điều hướng sang màn hình chỉnh sửa hồ sơ
        btn_editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditUserInformationActivity.class);
            startActivity(intent);
        });
    }
}

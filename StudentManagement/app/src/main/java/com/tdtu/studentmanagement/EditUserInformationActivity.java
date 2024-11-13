package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditUserInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information);

        // Ánh xạ các thành phần UI
        TextView etUserId = findViewById(R.id.etUserId);
        TextView etTitle = findViewById(R.id.etTitle); // Ánh xạ Title mới
        TextView etUsername = findViewById(R.id.etUsername);
        TextView etRole = findViewById(R.id.etRole);
        TextView etEmail = findViewById(R.id.etEmail);
        TextView etAge = findViewById(R.id.etAge);
        TextView etPhoneNumber = findViewById(R.id.etPhoneNumber);
        TextView etStatus = findViewById(R.id.etStatus);
        TextView etCreatedAt = findViewById(R.id.etCreatedAt);
        TextView etUpdatedAt = findViewById(R.id.etUpdatedAt);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String title = intent.getStringExtra("title"); // Nhận Title
        String username = intent.getStringExtra("username");
        String role = intent.getStringExtra("role");
        String email = intent.getStringExtra("email");
        int age = intent.getIntExtra("age", 0);
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String status = intent.getStringExtra("status");
        String createdAt = intent.getStringExtra("createdAt");
        String updatedAt = intent.getStringExtra("updatedAt");

        // Hiển thị dữ liệu lên giao diện
        etUserId.setText(userId);
        etTitle.setText(title); // Hiển thị Title
        etUsername.setText(username);
        etRole.setText(role);
        etEmail.setText(email);
        etAge.setText(String.valueOf(age));
        etPhoneNumber.setText(phoneNumber);
        etStatus.setText(status);
        etCreatedAt.setText(createdAt);
        etUpdatedAt.setText(updatedAt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icon_save) {
            // Mở Activity để thêm người dùng mới
            return true;

        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
package com.tdtu.studentmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class UserMainActivity extends AppCompatActivity {
    private static final int EDIT_PROFILE_REQUEST = 1;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private TextView tvTitle, tvId ,tvRole, tvName, tvPhone, tvEmail, tvAge, tvStatus, tvCreatedAt, tvUpdatedAt;
    private ShapeableImageView imgAvatar;
    private Bitmap avatarBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // Ánh xạ các thành phần UI
        tvId = findViewById(R.id.tvId);
        tvTitle = findViewById(R.id.tvTitle);
        tvRole = findViewById(R.id.tvRole);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAge = findViewById(R.id.tvAge);
        tvStatus = findViewById(R.id.tvStatus);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvUpdatedAt = findViewById(R.id.tvUpdatedAt);
        imgAvatar = findViewById(R.id.imgAvatar);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String username = intent.getStringExtra("username");
        String role = intent.getStringExtra("role");
        String email = intent.getStringExtra("email");
        int age = intent.getIntExtra("age", 0);
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String status = intent.getStringExtra("status");
        String profilePicture = intent.getStringExtra("profilePicture");
        String createdAt = intent.getStringExtra("createdAt");
        String updatedAt = intent.getStringExtra("updatedAt");

        // Hiển thị dữ liệu lên giao diện
        tvId.setText(userId);
        tvTitle.setText(username);
        tvRole.setText(role);
        tvName.setText(username);
        tvPhone.setText(phoneNumber);
        tvEmail.setText(email);
        tvAge.setText(String.valueOf(age));
        tvStatus.setText(status);
        tvCreatedAt.setText(createdAt);
        tvUpdatedAt.setText(updatedAt);

        // Thiết lập nút Back trên thanh công cụ
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icon_edit) {
            Intent intent = new Intent(UserMainActivity.this, EditUserInformationActivity.class);

            // Truyền dữ liệu người dùng qua Intent
            intent.putExtra("userId", tvId.getText().toString());
            intent.putExtra("title", tvTitle.getText().toString()); // Thêm Title
            intent.putExtra("username", tvTitle.getText().toString());
            intent.putExtra("role", tvRole.getText().toString());
            intent.putExtra("email", tvEmail.getText().toString());
            intent.putExtra("age", Integer.parseInt(tvAge.getText().toString()));
            intent.putExtra("phoneNumber", tvPhone.getText().toString());
            intent.putExtra("status", tvStatus.getText().toString());
            intent.putExtra("createdAt", tvCreatedAt.getText().toString());
            intent.putExtra("updatedAt", tvUpdatedAt.getText().toString());

            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

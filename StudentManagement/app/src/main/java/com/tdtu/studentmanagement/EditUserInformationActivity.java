package com.tdtu.studentmanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tdtu.studentmanagement.users.DatabaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditUserInformationActivity extends AppCompatActivity {

    private Uri selectedImageUri;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information);

        ImageButton btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(view -> openGallery());

        //Ref den database
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");

        // Ánh xạ các thành phần UI
        TextView etUserId = findViewById(R.id.etUserId);
        TextView etTitle = findViewById(R.id.etTitle);
        TextView etName = findViewById(R.id.etName);
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
        String name = intent.getStringExtra("name");
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
        etName.setText(name);
        etRole.setText(role);
        etEmail.setText(email);
        etAge.setText(String.valueOf(age));
        etPhoneNumber.setText(phoneNumber);
        etStatus.setText(status);
        etCreatedAt.setText(createdAt);
        etUpdatedAt.setText(updatedAt);


        ShapeableImageView shapeableImageView = findViewById(R.id.shapeableImageView);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        byte[] imageData = dbHelper.getImageByEmail(email);
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            shapeableImageView.setImageBitmap(bitmap);
        } else {
            // Nếu không có ảnh, hiển thị ảnh mặc định từ drawable
        }


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
            // Hiển thị hộp thoại xác nhận
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận lưu")
                    .setMessage("Bạn có chắc chắn muốn lưu các thay đổi không?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Thực hiện lưu dữ liệu khi chọn Yes
                        saveUserData();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Đóng hộp thoại khi chọn No
                        dialog.dismiss();
                    })
                    .show();

            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Phương thức lưu dữ liệu vào Firebase và SQLite
    private void saveUserData() {
        // Ánh xạ các thành phần UI
        TextView etUserId = findViewById(R.id.etUserId);
        TextView etName = findViewById(R.id.etName);
        TextView etRole = findViewById(R.id.etRole);
        TextView etEmail = findViewById(R.id.etEmail);
        TextView etAge = findViewById(R.id.etAge);
        TextView etPhoneNumber = findViewById(R.id.etPhoneNumber);
        TextView etStatus = findViewById(R.id.etStatus);
        TextView etCreatedAt = findViewById(R.id.etCreatedAt);

        if (selectedImageUri != null) {
            String email = etEmail.getText().toString();
            saveImageToDatabase(selectedImageUri, email); // Lưu ảnh khi bấm nút "Save"
        }

        String userId = etUserId.getText().toString();
        String name = etName.getText().toString();
        String role = etRole.getText().toString();
        String email = etEmail.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());
        String phoneNumber = etPhoneNumber.getText().toString();
        String status = etStatus.getText().toString();
        String createdAt = etCreatedAt.getText().toString();
        String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Cập nhật thông tin người dùng trên Firebase
        updateUserInfoOnFirebase(userId, name, role, email, age, phoneNumber, status, createdAt, updatedAt);

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent); // Đặt kết quả là OK
        finish();
    }



    //Vào thư viện ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    //Xử lý hiển thị hình ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // Lưu ảnh tạm thời vào biến selectedImageUri
            if (selectedImageUri != null) {
                // Hiển thị ảnh trên giao diện (ShapeableImageView chẳng hạn)
                ShapeableImageView shapeableImageView = findViewById(R.id.shapeableImageView);
                shapeableImageView.setImageURI(selectedImageUri);
            }
        }
    }

    //Chuyển ảnh qua byte
    private void saveImageToDatabase(Uri imageUri, String email) {
        try {
            // Chuyển ảnh thành byte array
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageData = IOUtils.toByteArray(inputStream);

            // Lưu dữ liệu vào SQLite
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("email", email); // Lưu email làm tên ảnh
            values.put("image", imageData); // Lưu ảnh dưới dạng BLOB

            db.insert("UserImages", null, values);
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Update tren firebase
    // Phương thức cập nhật thông tin người dùng trên Firebase
    private void updateUserInfoOnFirebase(String userId, String name, String role, String email, int age, String phoneNumber, String status, String createdAt, String updatedAt) {
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", name);
        userUpdates.put("role", role);
        userUpdates.put("email", email);
        userUpdates.put("age", age);
        userUpdates.put("phoneNumber", phoneNumber);
        userUpdates.put("status", status);
        userUpdates.put("createdAt", createdAt);
        userUpdates.put("updatedAt", updatedAt);

        Log.d("FirebaseUpdate", "User ID: " + userId);
        Log.d("FirebaseUpdate", "Name: " + name);
        Log.d("FirebaseUpdate", "Role: " + role);
        Log.d("FirebaseUpdate", "Email: " + email);
        Log.d("FirebaseUpdate", "Age: " + age);
        Log.d("FirebaseUpdate", "PhoneNumber: " + phoneNumber);
        Log.d("FirebaseUpdate", "Status: " + status);
        Log.d("FirebaseUpdate", "CreatedAt: " + createdAt);
        Log.d("FirebaseUpdate", "UpdatedAt: " + updatedAt);


        // Dùng updateChildren để chỉ cập nhật các trường có trong userUpdates, không ghi đè toàn bộ bản ghi
        databaseReference.child(userId).updateChildren(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    // Hiển thị thông báo khi cập nhật thành công
                    Toast.makeText(EditUserInformationActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Hiển thị thông báo khi cập nhật thất bại
                    Toast.makeText(EditUserInformationActivity.this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



}
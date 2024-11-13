package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditStudentInformationActivity extends AppCompatActivity {

    private EditText etStudentName, etStudentAge, etStudentPhone, etStudentEmail, etStudentAddress, etStatus;
    private String studentId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_information);

        // Ánh xạ các trường nhập liệu
        EditText etStudentId = findViewById(R.id.etStudentId);
        etStudentName = findViewById(R.id.etStudentName);
        etStudentAge = findViewById(R.id.etStudentAge);
        etStudentPhone = findViewById(R.id.etStudentPhone);
        etStudentEmail = findViewById(R.id.etStudentEmail);
        etStudentAddress = findViewById(R.id.etStudentAddress);
        EditText etCreatedAt = findViewById(R.id.etCreatedAt);
        etStatus = findViewById(R.id.etStatus);

        // Nhận dữ liệu sinh viên từ Intent
        Intent intent = getIntent();
        studentId = intent.getStringExtra("studentId");
        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age", 0);
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");
        String createdAt = intent.getStringExtra("createdAt");
        String status = intent.getStringExtra("status");

        // Hiển thị dữ liệu trong các trường nhập liệu
        etStudentId.setText(studentId);
        etStudentName.setText(name);
        etStudentAge.setText(String.valueOf(age));
        etStudentPhone.setText(phoneNumber);
        etStudentEmail.setText(email);
        etStudentAddress.setText(address);
        etCreatedAt.setText(createdAt);
        etStatus.setText(status);

        // Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students");

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
            saveStudentInformation();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Phương thức lưu thông tin sinh viên
    private void saveStudentInformation() {
        // Lấy thông tin từ các trường nhập liệu
        String name = etStudentName.getText().toString().trim();
        String ageStr = etStudentAge.getText().toString().trim();
        String phone = etStudentPhone.getText().toString().trim();
        String email = etStudentEmail.getText().toString().trim();
        String address = etStudentAddress.getText().toString().trim();
        String status = etStatus.getText().toString().trim();

        // Kiểm tra dữ liệu nhập vào
        if (name.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);

        // Lấy thời gian hiện tại cho trường updatedAt
        String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Tạo map chứa thông tin cập nhật
        Map<String, Object> studentUpdates = new HashMap<>();
        studentUpdates.put("studentId", studentId);
        studentUpdates.put("name", name);
        studentUpdates.put("age", age);
        studentUpdates.put("phoneNumber", phone);
        studentUpdates.put("email", email);
        studentUpdates.put("address", address);
        studentUpdates.put("status", status);
        studentUpdates.put("updatedAt", updatedAt);


        Log.d("EditStudentInfo", "Student ID: " + studentId);

        // Cập nhật vào Firebase
        databaseReference.child(studentId).updateChildren(studentUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditStudentInformationActivity.this, "Student information updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditStudentInformationActivity.this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

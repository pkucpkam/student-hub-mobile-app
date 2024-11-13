package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditStudentInformationActivity extends AppCompatActivity {

    private EditText etStudentName, etStudentAge, etStudentPhone, etStudentEmail, etStudentAddress;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_information);

        // Ánh xạ các trường nhập liệu
        EditText etStudentId = findViewById(R.id.etStudentId);
        EditText etStudentName = findViewById(R.id.etStudentName);
        EditText etStudentAge = findViewById(R.id.etStudentAge);
        EditText etStudentPhone = findViewById(R.id.etStudentPhone);
        EditText etStudentEmail = findViewById(R.id.etStudentEmail);
        EditText etStudentAddress = findViewById(R.id.etStudentAddress);
        EditText etCreatedAt = findViewById(R.id.etCreatedAt);
        EditText etUpdatedAt = findViewById(R.id.etUpdatedAt);
        EditText etStatus = findViewById(R.id.etStatus);

        // Nhận dữ liệu sinh viên từ Intent
        Intent intent = getIntent();
        String studentId = intent.getStringExtra("studentId");
        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age", 0);
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");
        String createdAt = intent.getStringExtra("createdAt");
        String updatedAt = intent.getStringExtra("updatedAt");
        String status = intent.getStringExtra("status");

        // Hiển thị dữ liệu trong các trường nhập liệu
        etStudentId.setText(studentId);
        etStudentName.setText(name);
        etStudentAge.setText(String.valueOf(age));
        etStudentPhone.setText(phoneNumber);
        etStudentEmail.setText(email);
        etStudentAddress.setText(address);
        etCreatedAt.setText(createdAt);
        etUpdatedAt.setText(updatedAt);
        etStatus.setText(status);

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
            // Lưu thông tin sinh viên sau khi chỉnh sửa
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
        String name = etStudentName.getText().toString();
        String ageStr = etStudentAge.getText().toString();
        String phone = etStudentPhone.getText().toString();
        String email = etStudentEmail.getText().toString();
        String address = etStudentAddress.getText().toString();

        // Kiểm tra dữ liệu nhập vào
        if (name.isEmpty() || ageStr.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);

        // Code để lưu thông tin sinh viên vào cơ sở dữ liệu hoặc Firebase
        // Ví dụ: gọi API để cập nhật thông tin sinh viên

        // Thông báo đã lưu thành công
        Toast.makeText(this, "Student information updated successfully!", Toast.LENGTH_SHORT).show();

        // Quay lại trang trước sau khi lưu
        finish();
    }

}

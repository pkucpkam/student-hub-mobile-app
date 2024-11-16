package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailStudentActivity extends AppCompatActivity {

    private TextView tvStudentId, tvStudentName, tvStudentAge, tvStudentPhone, tvStudentEmail, tvStudentAddress, tvClass, tvGrade;
    private TextView tvCreatedAt, tvUpdatedAt, tvStatus;

    private String studentId, name, phoneNumber, email, address, createdAt, updatedAt, status, studentClass;
    private int age;
    private float grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Khởi tạo các TextView
        tvStudentId = findViewById(R.id.tvStudentId);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentAge = findViewById(R.id.tvStudentAge);
        tvStudentPhone = findViewById(R.id.tvStudentPhone);
        tvStudentEmail = findViewById(R.id.tvStudentEmail);
        tvStudentAddress = findViewById(R.id.tvStudentAddress);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvUpdatedAt = findViewById(R.id.tvUpdatedAt);
        tvStatus = findViewById(R.id.tvStatus);
        tvClass = findViewById(R.id.tvClass);
        tvGrade = findViewById(R.id.tvGrade);

        // Lấy studentId từ Intent
        Intent intent = getIntent();
        studentId = intent.getStringExtra("studentId");

        if (studentId != null) {
            // Gọi hàm fetchStudentData để tải dữ liệu sinh viên từ Firebase
            fetchStudentData(studentId);
        } else {
            // Xử lý khi không có studentId
            Toast.makeText(this, "No student ID provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Gọi lại fetchStudentData để tải thông tin mới nhất sau khi quay lại
        if (studentId != null) {
            fetchStudentData(studentId);
        }
    }

    private void fetchStudentData(String studentId) {
        // Tham chiếu tới Firebase Realtime Database
        DatabaseReference studentRef = FirebaseDatabase
                .getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Students")
                .child(studentId);

        studentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    // Lấy dữ liệu sinh viên từ snapshot
                    name = snapshot.child("name").getValue(String.class);
                    Integer ageValue = snapshot.child("age").getValue(Integer.class);
                    age = (ageValue != null) ? ageValue : 0; // Giá trị mặc định nếu age là null
                    phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    address = snapshot.child("address").getValue(String.class);
                    createdAt = snapshot.child("createdAt").getValue(String.class);
                    updatedAt = snapshot.child("updatedAt").getValue(String.class);
                    status = snapshot.child("status").getValue(String.class);
                    studentClass = snapshot.child("studentClass").getValue(String.class);
                    Float gradeValue = snapshot.child("grade").getValue(Float.class);
                    grade = (gradeValue != null) ? gradeValue : 0.0f; // Giá trị mặc định nếu grade là null

                    // Hiển thị dữ liệu lên giao diện
                    tvStudentId.setText(studentId);
                    tvStudentName.setText(name != null ? name : "N/A");
                    tvStudentAge.setText(String.valueOf(age));
                    tvStudentPhone.setText(phoneNumber != null ? phoneNumber : "N/A");
                    tvStudentEmail.setText(email != null ? email : "N/A");
                    tvStudentAddress.setText(address != null ? address : "N/A");
                    tvCreatedAt.setText(createdAt != null ? createdAt : "N/A");
                    tvUpdatedAt.setText(updatedAt != null ? updatedAt : "N/A");
                    tvStatus.setText(status != null ? status : "N/A");
                    tvClass.setText(studentClass != null ? studentClass : "N/A");
                    tvGrade.setText(String.valueOf(grade));
                } else {
                    // Không tìm thấy dữ liệu sinh viên
                    Log.e("FetchStudentData", "No data found for studentId: " + studentId);
                    finish();
                }
            } else {
                // Xử lý lỗi
                Log.e("FetchStudentData", "Error fetching data", task.getException());
                finish();
            }
        });
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
            Intent intent = new Intent(DetailStudentActivity.this, EditStudentInformationActivity.class);

            // Truyền toàn bộ dữ liệu qua Intent
            intent.putExtra("studentId", studentId);
            intent.putExtra("name", name);
            intent.putExtra("age", age);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("email", email);
            intent.putExtra("address", address);
            intent.putExtra("createdAt", createdAt);
            intent.putExtra("updatedAt", updatedAt);
            intent.putExtra("status", status);
            intent.putExtra("studentClass", studentClass);
            intent.putExtra("grade", grade);

            startActivity(intent);
            return true;
        } else if (id == R.id.icon_manage_certificates) {
            Intent intent = new Intent(DetailStudentActivity.this, CertificateManagementActivity.class);
            intent.putExtra("studentId", studentId);
            startActivity(intent);
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

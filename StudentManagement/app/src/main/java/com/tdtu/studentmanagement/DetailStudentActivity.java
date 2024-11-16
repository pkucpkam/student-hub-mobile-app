package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

public class DetailStudentActivity extends AppCompatActivity {

    private TextView tvStudentId, tvStudentName, tvStudentAge, tvStudentPhone, tvStudentEmail, tvStudentAddress, tvClass, tvGrade;
    private TextView tvCreatedAt, tvUpdatedAt, tvStatus;

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

        // Lấy dữ liệu từ Intent
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
        String studentClass = intent.getStringExtra("studentClass");
        float grade = intent.getFloatExtra("grade", 0.0f);

        // Đặt dữ liệu vào TextView
        tvStudentId.setText(String.valueOf(studentId));
        tvStudentName.setText(String.valueOf(name));
        tvStudentAge.setText(String.valueOf(age));
        tvStudentPhone.setText(String.valueOf(phoneNumber));
        tvStudentEmail.setText(String.valueOf(email));
        tvStudentAddress.setText(String.valueOf(address));
        tvCreatedAt.setText(String.valueOf(createdAt));
        tvUpdatedAt.setText(String.valueOf(updatedAt));
        tvStatus.setText(String.valueOf(status));
        tvClass.setText(studentClass);  // Set class value
        tvGrade.setText(String.valueOf(grade));

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

            // Truyền dữ liệu sinh viên qua Intent
            intent.putExtra("studentId", tvStudentId.getText().toString());
            intent.putExtra("name", tvStudentName.getText().toString());
            intent.putExtra("age", Integer.parseInt(tvStudentAge.getText().toString()));
            intent.putExtra("phoneNumber", tvStudentPhone.getText().toString());
            intent.putExtra("email", tvStudentEmail.getText().toString());
            intent.putExtra("address", tvStudentAddress.getText().toString());
            intent.putExtra("createdAt", tvCreatedAt.getText().toString());
            intent.putExtra("updatedAt", tvUpdatedAt.getText().toString());
            intent.putExtra("status", tvStatus.getText().toString());
            intent.putExtra("studentClass", tvClass.getText().toString());
            intent.putExtra("grade", Float.parseFloat(tvGrade.getText().toString()));

            startActivity(intent);
            return true;
        }
        else if (id == R.id.icon_manage_certificates) {
            Log.d("s", "pl");
            Intent intent = new Intent(DetailStudentActivity.this, CertificateManagementActivity.class);
            intent.putExtra("studentId", tvStudentId.getText().toString());
            startActivity(intent);
        }
        else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

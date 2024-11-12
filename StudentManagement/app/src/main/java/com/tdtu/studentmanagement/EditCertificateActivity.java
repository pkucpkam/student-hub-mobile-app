package com.tdtu.studentmanagement;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditCertificateActivity extends AppCompatActivity {

    private EditText etCertificateName, etIssueDate, etExpiryDate, etStudentId;
    private int certificateId;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_certificate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Ánh xạ các trường EditText
        etCertificateName = findViewById(R.id.etCertificateName);
        etIssueDate = findViewById(R.id.etIssueDate);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etStudentId = findViewById(R.id.etStudentId);

        // Nhận dữ liệu từ Intent
        certificateId = getIntent().getIntExtra("certificate_id", -1);
        etStudentId.setText(String.valueOf(getIntent().getIntExtra("student_id", -1)));
        etCertificateName.setText(getIntent().getStringExtra("certificate_name"));
        etIssueDate.setText(getIntent().getStringExtra("issue_date"));
        etExpiryDate.setText(getIntent().getStringExtra("expiry_date"));

        // Khởi tạo Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance("https://your-firebase-url").getReference("Certificates");
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
            saveCertificateInformation();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveCertificateInformation() {
        // Lấy dữ liệu từ các EditText
        String certificateName = etCertificateName.getText().toString().trim();
        String issueDate = etIssueDate.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();
        int studentId = Integer.parseInt(etStudentId.getText().toString().trim());

        if (certificateName.isEmpty() || issueDate.isEmpty() || expiryDate.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin chứng chỉ trong Firebase
        DatabaseReference certRef = databaseReference.child(String.valueOf(certificateId));
        certRef.child("certificate_name").setValue(certificateName);
        certRef.child("issue_date").setValue(issueDate);
        certRef.child("expiry_date").setValue(expiryDate);
        certRef.child("student_id").setValue(studentId);

        Toast.makeText(this, "Certificate information updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}

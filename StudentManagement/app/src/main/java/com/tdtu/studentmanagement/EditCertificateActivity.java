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

    private EditText etCertificateName, etIssueDate, etExpiryDate, etStudentId, etCreatedAt, etUpdatedAt;
    private String certificateId;
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

        // Initialize EditText fields
        etCertificateName = findViewById(R.id.etCertificateName);
        etIssueDate = findViewById(R.id.etIssueDate);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etStudentId = findViewById(R.id.etStudentId);
        etCreatedAt = findViewById(R.id.etCreatedAt);
        etUpdatedAt = findViewById(R.id.etUpdatedAt);


        // Receive data from Intent
        certificateId = getIntent().getStringExtra("certificate_id");
        etStudentId.setText(getIntent().getStringExtra("student_id"));
        etCertificateName.setText(getIntent().getStringExtra("certificate_name"));
        etIssueDate.setText(getIntent().getStringExtra("issue_date"));
        etExpiryDate.setText(getIntent().getStringExtra("expiry_date"));
        etCreatedAt.setText(getIntent().getStringExtra("created_at"));
        etUpdatedAt.setText(getIntent().getStringExtra("updated_at"));


        // Initialize Firebase Database Reference
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
        // Get data from EditText fields
        String certificateName = etCertificateName.getText().toString().trim();
        String issueDate = etIssueDate.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();
        String createdAt = etCreatedAt.getText().toString().trim();
        String updatedAt = etUpdatedAt.getText().toString().trim();

        if (certificateName.isEmpty() || issueDate.isEmpty() || expiryDate.isEmpty() || studentId.isEmpty() || createdAt.isEmpty() || updatedAt.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update certificate information in Firebase
        DatabaseReference certRef = databaseReference.child(certificateId);
        certRef.child("certificate_name").setValue(certificateName);
        certRef.child("issue_date").setValue(issueDate);
        certRef.child("expiry_date").setValue(expiryDate);
        certRef.child("student_id").setValue(studentId);
        certRef.child("created_at").setValue(createdAt);
        certRef.child("updated_at").setValue(updatedAt);

        Toast.makeText(this, "Certificate information updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}

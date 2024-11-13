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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditCertificateActivity extends AppCompatActivity {

    private EditText etCertificateName, etIssueDate, etExpiryDate, etStudentId, etUpdatedAt;
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
        etUpdatedAt = findViewById(R.id.etUpdatedAt);

        // Receive data from Intent
        certificateId = getIntent().getStringExtra("certificate_id");
        etStudentId.setText(getIntent().getStringExtra("student_id"));
        etCertificateName.setText(getIntent().getStringExtra("certificate_name"));
        etIssueDate.setText(getIntent().getStringExtra("issue_date"));
        etExpiryDate.setText(getIntent().getStringExtra("expiry_date"));
        etUpdatedAt.setText(getIntent().getStringExtra("updated_at"));

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Certificates");
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

        // Automatically set updatedAt to the current time
        String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (certificateName.isEmpty() || issueDate.isEmpty() || expiryDate.isEmpty() || studentId.isEmpty() ) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data as a map for update
        Map<String, Object> certificateUpdates = new HashMap<>();
        certificateUpdates.put("certificateName", certificateName);
        certificateUpdates.put("issueDate", issueDate);
        certificateUpdates.put("expiryDate", expiryDate);
        certificateUpdates.put("studentId", studentId);
        certificateUpdates.put("updatedAt", updatedAt);

        // Update certificate information in Firebase
        databaseReference.child(certificateId).updateChildren(certificateUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditCertificateActivity.this, "Certificate information updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(EditCertificateActivity.this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

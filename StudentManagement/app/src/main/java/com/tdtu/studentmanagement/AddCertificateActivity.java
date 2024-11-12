package com.tdtu.studentmanagement;

import android.app.DatePickerDialog;
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
import com.tdtu.studentmanagement.certificates.Certificate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddCertificateActivity extends AppCompatActivity {

    private EditText etStudentId, etCertificateName, etIssueDate, etExpiryDate;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_certificate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Certificates");

        etStudentId = findViewById(R.id.etStudentId);
        etCertificateName = findViewById(R.id.etCertificateName);
        etIssueDate = findViewById(R.id.etIssueDate);
        etExpiryDate = findViewById(R.id.etExpiryDate);

        etIssueDate.setOnClickListener(v -> showDatePickerDialog(etIssueDate));
        etExpiryDate.setOnClickListener(v -> showDatePickerDialog(etExpiryDate));
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Định dạng ngày thành YYYY-MM-DD
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
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
        String studentId = etStudentId.getText().toString().trim();
        String certificateName = etCertificateName.getText().toString().trim();
        String issueDate = etIssueDate.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();

        if (studentId.isEmpty() || certificateName.isEmpty() || issueDate.isEmpty() || expiryDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String certificateId = UUID.randomUUID().toString();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Certificate certificate = new Certificate(
                certificateId,
                studentId,
                certificateName,
                issueDate,
                expiryDate,
                currentTime,
                currentTime
        );

        databaseReference.child(certificateId).setValue(certificate)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddCertificateActivity.this, "Certificate added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddCertificateActivity.this, "Failed to add certificate", Toast.LENGTH_SHORT).show();
                });
    }
}

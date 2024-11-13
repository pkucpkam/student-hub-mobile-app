package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailCertificateActivity extends AppCompatActivity {

    private TextView tvCertificateName, tvStudentId, tvIssueDate, tvExpiryDate, tvCreatedAt, tvUpdatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_certificate);

        // Retrieve UI elements
        tvCertificateName = findViewById(R.id.tvCertificateName);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvIssueDate = findViewById(R.id.tvIssueDate);
        tvExpiryDate = findViewById(R.id.tvExpiryDate);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvUpdatedAt = findViewById(R.id.tvUpdatedAt);

        // Apply window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enable back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get intent extras and display them
        Intent intent = getIntent();
        String certificateName = intent.getStringExtra("certificate_name");
        String studentId = intent.getStringExtra("student_id");
        String issueDate = intent.getStringExtra("issue_date");
        String expiryDate = intent.getStringExtra("expiry_date");
        String createdAt = intent.getStringExtra("created_at");
        String updatedAt = intent.getStringExtra("updated_at");

        // Display the data
        tvCertificateName.setText(certificateName);
        tvStudentId.setText(studentId);
        tvIssueDate.setText(issueDate);
        tvExpiryDate.setText(expiryDate);
        tvCreatedAt.setText(createdAt);
        tvUpdatedAt.setText(updatedAt);
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
            Intent intent = new Intent(DetailCertificateActivity.this, EditCertificateActivity.class);

            intent.putExtra("certificate_id", getIntent().getStringExtra("certificate_id"));
            intent.putExtra("student_id", getIntent().getStringExtra("student_id"));
            intent.putExtra("certificate_name", getIntent().getStringExtra("certificate_name"));
            intent.putExtra("issue_date", getIntent().getStringExtra("issue_date"));
            intent.putExtra("expiry_date", getIntent().getStringExtra("expiry_date"));
            intent.putExtra("created_at", getIntent().getStringExtra("created_at"));
            intent.putExtra("updated_at", getIntent().getStringExtra("updated_at"));

            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

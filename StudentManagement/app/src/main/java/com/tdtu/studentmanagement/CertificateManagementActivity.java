package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdtu.studentmanagement.certificates.Certificate;
import com.tdtu.studentmanagement.certificates.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class CertificateManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Certificate> certificateList;
    private DatabaseReference databaseReference; // Tham chiếu Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_certificate_management);

        // Thiết lập padding cho hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCertificates);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách chứng chỉ và adapter
        certificateList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(this, certificateList);
        recyclerView.setAdapter(adapter);

        // Khởi tạo Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Certificates");

        // Lấy dữ liệu từ Firebase
        fetchCertificatesFromFirebase();
    }

    // Phương thức lấy dữ liệu từ Firebase
    private void fetchCertificatesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                certificateList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Certificate certificate = snapshot.getValue(Certificate.class);
                    if (certificate != null) {
                        certificateList.add(certificate);

                        Log.d("CertificateManagement", "Certificate ID: " + certificate.getCertificate_id());
                        Log.d("CertificateManagement", "Student ID: " + certificate.getStudent_id());
                        Log.d("CertificateManagement", "Certificate Name: " + certificate.getCertificate_name());
                        Log.d("CertificateManagement", "Issue Date: " + certificate.getIssue_date());
                        Log.d("CertificateManagement", "Expiry Date: " + certificate.getExpiry_date());
                        Log.d("CertificateManagement", "Created At: " + certificate.getCreated_at());
                        Log.d("CertificateManagement", "Updated At: " + certificate.getUpdated_at());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("CertificateManagement", "Failed to read value.", error.toException());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_certificate_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icon_add) {
            Intent intent = new Intent(CertificateManagementActivity.this, AddCertificateActivity.class);
            startActivity(intent);
            return true;

        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

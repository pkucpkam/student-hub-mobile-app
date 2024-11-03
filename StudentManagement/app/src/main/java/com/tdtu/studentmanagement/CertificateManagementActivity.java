package com.tdtu.studentmanagement;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.certificates.Certificate;
import com.tdtu.studentmanagement.certificates.RecyclerViewAdapter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CertificateManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Certificate> certificateList;

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

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCertificates);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo danh sách dữ liệu mẫu
        certificateList = getSampleCertificates();

        // Thiết lập adapter cho RecyclerView
        adapter = new RecyclerViewAdapter(this, certificateList);
        recyclerView.setAdapter(adapter);
    }

    // Phương thức tạo danh sách dữ liệu mẫu cho chứng chỉ
    private List<Certificate> getSampleCertificates() {
        List<Certificate> certificates = new ArrayList<>();
        certificates.add(new Certificate(1, 101, "Chứng chỉ Anh văn B", Date.valueOf("2022-01-01"), Date.valueOf("2025-01-01"), null, null));
        certificates.add(new Certificate(2, 102, "Chứng chỉ Tin học A", Date.valueOf("2021-06-15"), Date.valueOf("2024-06-15"), null, null));
        certificates.add(new Certificate(3, 103, "Chứng chỉ Kế toán", Date.valueOf("2020-11-20"), Date.valueOf("2023-11-20"), null, null));
        return certificates;
    }
}

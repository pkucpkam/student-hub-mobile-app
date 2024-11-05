package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.history.LoginHistory;
import com.tdtu.studentmanagement.history.RecyclerViewAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<LoginHistory> loginHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewLoginHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo danh sách dữ liệu mẫu cho lịch sử đăng nhập
        loginHistoryList = getSampleLoginHistory();

        // Thiết lập adapter cho RecyclerView
        adapter = new RecyclerViewAdapter(this, loginHistoryList);
        recyclerView.setAdapter(adapter);
    }

    // Phương thức tạo dữ liệu mẫu cho lịch sử đăng nhập
    private List<LoginHistory> getSampleLoginHistory() {
        List<LoginHistory> loginHistories = new ArrayList<>();
        loginHistories.add(new LoginHistory(1, 101, "john_doe", "Admin", new Timestamp(System.currentTimeMillis() - 3600 * 1000), new Timestamp(System.currentTimeMillis())));
        loginHistories.add(new LoginHistory(2, 102, "jane_smith", "User", new Timestamp(System.currentTimeMillis() - 7200 * 1000), null));  // Chưa đăng xuất
        loginHistories.add(new LoginHistory(3, 103, "alice_wong", "Manager", new Timestamp(System.currentTimeMillis() - 10800 * 1000), new Timestamp(System.currentTimeMillis() - 3600 * 1000)));
        return loginHistories;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Kết thúc Activity này để trở về trang trước
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

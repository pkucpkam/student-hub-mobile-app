package com.tdtu.studentmanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdtu.studentmanagement.history.LoginHistory;
import com.tdtu.studentmanagement.history.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<LoginHistory> loginHistoryList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewLoginHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách và adapter
        loginHistoryList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(this, loginHistoryList);
        recyclerView.setAdapter(adapter);

        // Khởi tạo Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("LoginHistory");

        // Lấy dữ liệu từ Firebase
        fetchLoginHistoryFromFirebase();
    }

    // Phương thức lấy dữ liệu từ Firebase
    private void fetchLoginHistoryFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loginHistoryList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LoginHistory loginHistory = snapshot.getValue(LoginHistory.class);
                    if (loginHistory != null) {
                        loginHistoryList.add(loginHistory);
                        
                        // In ra log để kiểm tra dữ liệu
                        Log.d("HistoryActivity", "Login ID: " + loginHistory.getLoginId());
                        Log.d("HistoryActivity", "User ID: " + loginHistory.getUserId());
                        Log.d("HistoryActivity", "Username: " + loginHistory.getUsername());
                        Log.d("HistoryActivity", "Role: " + loginHistory.getRole());
                        Log.d("HistoryActivity", "Login Timestamp: " + loginHistory.getLoginTimestamp());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("HistoryActivity", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Kết thúc Activity để trở về trang trước
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

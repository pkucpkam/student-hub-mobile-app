package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.users.RecyclerViewAdapter;
import com.tdtu.studentmanagement.users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserManagementActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private Button btnSearch;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_management);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);

        // Xử lý khi người dùng nhấn "Enter" trong EditText
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                performSearch(edtSearch.getText().toString());  // Thực hiện tìm kiếm
                return true;
            }
            return false;
        });

        // Xử lý khi người dùng bấm nút tìm kiếm
        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString();
            performSearch(query);
        });

        // Lắng nghe thay đổi văn bản trong EditText để tìm kiếm theo thời gian thực
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                performSearch(query);
            }
        });

        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                        // Log user data
                        Log.d("UserManagementActivity", "User ID: " + user.getUserId());
                        Log.d("UserManagementActivity", "Username: " + user.getUsername());
                        Log.d("UserManagementActivity", "Role: " + user.getRole());
                        Log.d("UserManagementActivity", "Name: " + user.getName());
                        Log.d("UserManagementActivity", "Age: " + user.getAge());
                        Log.d("UserManagementActivity", "Phone Number: " + user.getPhoneNumber());
                        Log.d("UserManagementActivity", "Status: " + user.getStatus());
                    } else {
                        Log.w("UserManagementActivity", "User is null");
                    }
                }
                if (userList.isEmpty()) {
                    Log.d("UserManagementActivity", "No users found.");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("UserManagementActivity", "Failed to read value.", error.toException());
            }
        });
    }

    // Hàm thực hiện tìm kiếm
    private void performSearch(String query) {
        if (query.isEmpty()) {
            loadDataFromFirebase();  // Tải lại toàn bộ dữ liệu
            return;
        }

        List<User> filteredUsers = new ArrayList<>();
        for (User user : userList) {
            // Kiểm tra xem tên người dùng có chứa từ khóa tìm kiếm không
            if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredUsers.add(user);
            }
        }

        // Cập nhật danh sách người dùng trong adapter
        adapter.updateData(filteredUsers);

        // Nếu không tìm thấy người dùng nào, có thể hiển thị thông báo
        if (filteredUsers.isEmpty()) {
            Toast.makeText(UserManagementActivity.this, "No users found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icon_add) {
            // Mở Activity để thêm người dùng mới
            Intent intent = new Intent(UserManagementActivity.this, AddUserActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.miDeleteAll) {
            // Thực hiện hành động xóa tất cả người dùng
            deleteAllUsers();
            return true;
        } else if (id == R.id.miAbout) {
            // Thực hiện hành động sắp xếp danh sách người dùng
            sortUserList();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllUsers() {
        // Xóa tất cả người dùng trong Firebase
        databaseReference.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Xóa thành công, cập nhật lại danh sách
                    userList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(UserManagementActivity.this, "All users deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Nếu có lỗi
                    Toast.makeText(UserManagementActivity.this, "Failed to delete users", Toast.LENGTH_SHORT).show();
                });
    }

    private void sortUserList() {
        // Sắp xếp danh sách người dùng theo tên
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u1.getName().compareToIgnoreCase(u2.getName());
            }
        });
        adapter.notifyDataSetChanged();
        Toast.makeText(UserManagementActivity.this, "User list sorted", Toast.LENGTH_SHORT).show();
    }
}

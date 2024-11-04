package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {
    private Button btnAddUser;
    private TextView btn_back;
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

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo dữ liệu mẫu cho User
        userList = getSampleUsers();

        // Khởi tạo adapter và gán vào RecyclerView
        adapter = new RecyclerViewAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        btn_back = findViewById(R.id.btn_back);
        btnAddUser = findViewById(R.id.btnAddUser);

        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(UserManagementActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnAddUser.setOnClickListener(view -> {
            Intent intent = new Intent(UserManagementActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private List<User> getSampleUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "john_doe", "password1", "Admin", "John Doe", 30, "1234567890", User.Status.NORMAL, null, null, null));
        users.add(new User(2, "jane_smith", "password2", "User", "Jane Smith", 25, "0987654321", User.Status.LOCKED, null, null, null));
        users.add(new User(3, "alice_wong", "password3", "User", "Alice Wong", 27, "1112223333", User.Status.NORMAL, null, null, null));
        users.add(new User(4, "bob_martin", "password4", "Manager", "Bob Martin", 35, "4445556666", User.Status.NORMAL, null, null, null));
        users.add(new User(5, "charlie_johnson", "password5", "Admin", "Charlie Johnson", 29, "7778889999", User.Status.LOCKED, null, null, null));
        return users;
    }
}

package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class UserManagementActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
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

        // Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
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


}

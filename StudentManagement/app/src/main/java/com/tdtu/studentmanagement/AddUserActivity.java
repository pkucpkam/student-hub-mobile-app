package com.tdtu.studentmanagement;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    private EditText etName, etUsername, etPhoneNumber, etAge;
    private Spinner spinnerRole;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Khởi tạo Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Liên kết các view với id
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAge = findViewById(R.id.etAge);
        spinnerRole = findViewById(R.id.spinnerRole);

        // Thiết lập Spinner cho vai trò
        String[] roles = {"Admin", "Manager", "Employee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
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
            // Lưu thông tin người dùng
            saveUserInformation();
            return true;

        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveUserInformation() {
        String name = etName.getText().toString().trim();
        String email = etUsername.getText().toString().trim();
        String emailKey = email.replace(".", ",");  // Firebase không hỗ trợ dấu chấm trong khóa
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm người dùng vào Firebase Authentication với mật khẩu mặc định
        mAuth.createUserWithEmailAndPassword(email, "123456")
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Lấy userId từ Authentication
                        String userId = mAuth.getCurrentUser().getUid();

                        // Thiết lập trạng thái và thời gian tạo
                        String status = "Normal";
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

                        // Lưu thông tin người dùng vào Firebase Database
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("userId", userId);
                        userData.put("name", name);
                        userData.put("email", email);
                        userData.put("phoneNumber", phoneNumber);
                        userData.put("age", Integer.parseInt(age));
                        userData.put("role", role);
                        userData.put("status", status);
                        userData.put("createdAt", currentTime);
                        userData.put("updatedAt", currentTime);

                        databaseReference.child(userId).setValue(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AddUserActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(AddUserActivity.this, "Failed to add user", Toast.LENGTH_SHORT).show());

                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(AddUserActivity.this, "User with this email already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddUserActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

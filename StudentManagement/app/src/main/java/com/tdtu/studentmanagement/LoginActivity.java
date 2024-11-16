package com.tdtu.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdtu.studentmanagement.history.LoginHistory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());

        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        fetchUserDataByEmail(email);
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserDataByEmail(String email) {
        // Thay thế dấu chấm bằng dấu phẩy trong email
        String formattedEmail = email.replace(".", ",");
        Log.d("abc",formattedEmail);
        databaseReference.orderByChild("email").equalTo(formattedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.child("userId").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);
                        String username = userSnapshot.child("name").getValue(String.class);
                        String status = userSnapshot.child("status").getValue(String.class);


                        Log.d("abc", userId + " " + role);
                        if ("Normal".equalsIgnoreCase(status)) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            // Lưu vào session
                            saveUserSession(userId, role);
                            // Lưu vào Login History
                            saveLoginHistory(userId, role, username);
                            // Điều hướng tới MainActivity
                            navigateToMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Tài khoản của bạn không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Lỗi khi truy vấn dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginHistory(String userId, String role, String username) {
        // Tạo loginId tự động sử dụng Firebase Database push() để tạo một ID duy nhất
        DatabaseReference loginHistoryRef = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LoginHistory");

        String loginId = loginHistoryRef.push().getKey();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  // Đảm bảo múi giờ là UTC
        String timestamp = sdf.format(new Date());

        // Tạo đối tượng LoginHistory
        LoginHistory loginHistory = new LoginHistory(loginId, userId, username, role, timestamp);

        Log.d("a", loginHistory.toString());
        // Lưu vào Firebase
        loginHistoryRef.child(loginId).setValue(loginHistory)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("LoginHistory", "Lưu lịch sử đăng nhập thành công!");
                    } else {
                        Log.d("LoginHistory", "Lỗi khi lưu lịch sử đăng nhập: " + task.getException().getMessage());
                    }
                });
    }


    private void saveUserSession(String userId, String role) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.putString("role", role);
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

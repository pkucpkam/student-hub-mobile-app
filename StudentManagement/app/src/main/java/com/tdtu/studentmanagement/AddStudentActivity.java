package com.tdtu.studentmanagement;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tdtu.studentmanagement.students.Student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddStudentActivity extends AppCompatActivity {

    private EditText etName, etAge, etPhoneNumber, etEmail, etAddress;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etName = findViewById(R.id.etStudentName);
        etAge = findViewById(R.id.etStudentAge);
        etPhoneNumber = findViewById(R.id.etStudentPhoneNumber);
        etEmail = findViewById(R.id.etStudentEmail);
        etAddress = findViewById(R.id.etStudentAddress);

        // Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            saveStudent();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStudent() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        String status = "Active";
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        String studentId = databaseReference.push().getKey(); // Tạo ID tự động
        Student student = new Student(studentId, name, age, phoneNumber, email, address, currentTime, currentTime, status);

        if (studentId != null) {
            databaseReference.child(studentId).setValue(student)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddStudentActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddStudentActivity.this, "Failed to add student", Toast.LENGTH_SHORT).show());
        }
    }
}

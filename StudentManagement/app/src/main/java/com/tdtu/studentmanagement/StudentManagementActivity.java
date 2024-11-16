package com.tdtu.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.tdtu.studentmanagement.students.RecyclerViewAdapter;
import com.tdtu.studentmanagement.students.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentManagementActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Button btnSearch;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Student> studentList;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_management);

        // Thiết lập padding cho các thanh hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Lấy vai trò người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");

        // Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students");

        // Thiết lập RecyclerView và adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(this, studentList, role);
        recyclerView.setAdapter(adapter);

        // Setup tìm kiếm
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                performSearch(edtSearch.getText().toString());
                return true;
            }
            return false;
        });

        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString();
            performSearch(query);
        });

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
                studentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null) {
                        studentList.add(student);
                        Log.d("A", student.toString());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("StudentManagement", "Failed to read value.", error.toException());
            }
        });
    }

    // Hàm thực hiện tìm kiếm
    private void performSearch(String query) {
        if (query.isEmpty()) {
            loadDataFromFirebase();
            return;
        }

        List<Student> filteredStudents = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredStudents.add(student);
            }
        }

        adapter.updateStudentList(filteredStudents);

        if (filteredStudents.isEmpty()) {
            Toast.makeText(StudentManagementActivity.this, "No students found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.icon_add) {
            Intent intent = new Intent(StudentManagementActivity.this, AddStudentActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.miDeleteAll) {
            deleteAllStudents();
            return true;

        } else if (id == R.id.miAbout) {
            Intent intent = new Intent(StudentManagementActivity.this, ImportorExportStudentActivity.class);
            startActivity(intent);
            return true;

        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllStudents() {
        databaseReference.removeValue()
                .addOnSuccessListener(aVoid -> {
                    studentList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(StudentManagementActivity.this, "All students deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentManagementActivity.this, "Failed to delete students", Toast.LENGTH_SHORT).show();
                });
    }

}

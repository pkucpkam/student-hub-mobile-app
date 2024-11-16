package com.tdtu.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
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
import java.util.List;

public class StudentManagementActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Button btnSearch;
    private EditText edtSearch;
    private Spinner spinnerCriteria;
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
        spinnerCriteria = findViewById(R.id.spinnerCriteria);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);

        btnSearch.setOnClickListener(v -> {
            String query = edtSearch.getText().toString().trim();
            String selectedCriteria = spinnerCriteria.getSelectedItem().toString();

            switch (selectedCriteria) {
                case "Name":
                    performSearch(query, null, null, null);
                    break;
                case "Age":
                    try {
                        int age = Integer.parseInt(query);
                        performSearch(null, age, null, null);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid age entered", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Address":
                    performSearch(null, null, null, query);
                    break;
                default:
                    Toast.makeText(this, "Please select a valid criteria", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        // Tải dữ liệu ban đầu từ Firebase
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

    private void performSearch(String query, Integer ageFilter, String statusFilter, String addressFilter) {
        if ((query == null || query.isEmpty()) &&
                ageFilter == null &&
                (statusFilter == null || statusFilter.isEmpty()) &&
                (addressFilter == null || addressFilter.isEmpty())) {
            loadDataFromFirebase();
            return;
        }

        List<Student> filteredStudents = new ArrayList<>();
        for (Student student : studentList) {
            boolean matchesName = query == null || query.isEmpty() || student.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesAge = ageFilter == null || student.getAge() == ageFilter;
            boolean matchesStatus = statusFilter == null || statusFilter.isEmpty() || student.getStatus().toLowerCase().contains(statusFilter.toLowerCase());
            boolean matchesAddress = addressFilter == null || addressFilter.isEmpty() || student.getAddress().toLowerCase().contains(addressFilter.toLowerCase());

            if (matchesName && matchesAge && matchesStatus && matchesAddress) {
                filteredStudents.add(student);
            }
        }

        adapter.updateStudentList(filteredStudents);

        if (filteredStudents.isEmpty()) {
            Toast.makeText(StudentManagementActivity.this, "No students found", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSortMenu() {
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.icon_sort));
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.sort_by_name_asc) {
                sortStudentsByName(true);
            } else if (itemId == R.id.sort_by_name_desc) {
                sortStudentsByName(false);
            } else if (itemId == R.id.sort_by_age_asc) {
                sortStudentsByAge(true);
            } else if (itemId == R.id.sort_by_age_desc) {
                sortStudentsByAge(false);
            } else if (itemId == R.id.sort_by_grade_asc) {
                sortStudentsByGrade(true);
            } else if (itemId == R.id.sort_by_grade_desc) {
                sortStudentsByGrade(false);
            }

            return true;
        });

        popupMenu.show();
    }

    private void sortStudentsByName(boolean ascending) {
        if (ascending) {
            studentList.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        } else {
            studentList.sort((s1, s2) -> s2.getName().compareToIgnoreCase(s1.getName()));
        }
        adapter.notifyDataSetChanged();
    }

    private void sortStudentsByAge(boolean ascending) {
        if (ascending) {
            studentList.sort((s1, s2) -> Integer.compare(s1.getAge(), s2.getAge()));
        } else {
            studentList.sort((s1, s2) -> Integer.compare(s2.getAge(), s1.getAge()));
        }
        adapter.notifyDataSetChanged();
    }

    private void sortStudentsByGrade(boolean ascending) {
        if (ascending) {
            studentList.sort((s1, s2) -> Double.compare(s1.getGrade(), s2.getGrade()));
        } else {
            studentList.sort((s1, s2) -> Double.compare(s2.getGrade(), s1.getGrade()));
        }
        adapter.notifyDataSetChanged();
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

        } else if (id == R.id.icon_sort) {
            showSortMenu();
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

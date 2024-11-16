package com.tdtu.studentmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private List<Student> filteredStudentList;


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

            performSearch(query, selectedCriteria);
        });

        // Thêm TextWatcher để kiểm tra sự thay đổi của ô tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    adapter.updateStudentList(new ArrayList<>(studentList));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                adapter.updateStudentList(new ArrayList<>(studentList));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("StudentManagement", "Failed to read value.", error.toException());
            }
        });
    }

    private void performSearch(String query, String selectedCriteria) {
        if (query == null || query.isEmpty()) {
            filteredStudentList = new ArrayList<>(studentList); // Reset to the full list
            adapter.updateStudentList(filteredStudentList);
            return;
        }

        filteredStudentList = new ArrayList<>();
        for (Student student : studentList) {
            boolean matches = false;
            switch (selectedCriteria) {
                case "Name":
                    matches = student.getName().toLowerCase().contains(query.toLowerCase());
                    break;
                case "Age":
                    try {
                        int age = Integer.parseInt(query);
                        matches = student.getAge() == age;
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid age entered", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Address":
                    matches = student.getAddress().toLowerCase().contains(query.toLowerCase());
                    break;
                default:
                    Toast.makeText(this, "Please select a valid criteria", Toast.LENGTH_SHORT).show();
                    return;
            }

            if (matches) {
                filteredStudentList.add(student);
            }
        }

        adapter.updateStudentList(filteredStudentList);

        if (filteredStudentList.isEmpty()) {
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
        if (filteredStudentList == null) {
            filteredStudentList = new ArrayList<>(studentList);
        }

        if (ascending) {
            filteredStudentList.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        } else {
            filteredStudentList.sort((s1, s2) -> s2.getName().compareToIgnoreCase(s1.getName()));
        }
        adapter.updateStudentList(new ArrayList<>(filteredStudentList));
    }

    private void sortStudentsByAge(boolean ascending) {
        if (filteredStudentList == null) {
            filteredStudentList = new ArrayList<>(studentList);
        }

        if (ascending) {
            filteredStudentList.sort((s1, s2) -> Integer.compare(s1.getAge(), s2.getAge()));
        } else {
            filteredStudentList.sort((s1, s2) -> Integer.compare(s2.getAge(), s1.getAge()));
        }
        adapter.updateStudentList(new ArrayList<>(filteredStudentList));
    }

    private void sortStudentsByGrade(boolean ascending) {
        if (filteredStudentList == null) {
            filteredStudentList = new ArrayList<>(studentList);
        }

        if (ascending) {
            filteredStudentList.sort((s1, s2) -> Double.compare(s1.getGrade(), s2.getGrade()));
        } else {
            filteredStudentList.sort((s1, s2) -> Double.compare(s2.getGrade(), s1.getGrade()));
        }
        adapter.updateStudentList(new ArrayList<>(filteredStudentList));
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
            loadDataFromFirebase(); // Reload all students after deleting
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
                    adapter.updateStudentList(new ArrayList<>(studentList));
                    Toast.makeText(StudentManagementActivity.this, "All students deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentManagementActivity.this, "Failed to delete students", Toast.LENGTH_SHORT).show();
                });
    }
}

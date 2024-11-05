package com.tdtu.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.students.Student;
import com.tdtu.studentmanagement.students.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudentManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Student> studentList;

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

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo dữ liệu mẫu cho Student
        studentList = getSampleStudents();

        // Thiết lập adapter cho RecyclerView
        adapter = new RecyclerViewAdapter(this, studentList);
        recyclerView.setAdapter(adapter);
    }

    // Phương thức tạo dữ liệu mẫu cho sinh viên
    private List<Student> getSampleStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Nguyen Van A", 20, "0123456789", "vana@example.com", "123 Main St", null, null));
        students.add(new Student(2, "Tran Thi B", 22, "0987654321", "thib@example.com", "456 Secondary St", null, null));
        students.add(new Student(3, "Le Quoc C", 21, "0345678912", "quocc@example.com", "789 Tertiary St", null, null));
        return students;
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
            // Mở Activity để thêm người dùng mới
            Intent intent = new Intent(StudentManagementActivity.this, AddStudentActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.miDeleteAll) {
            // Thực hiện hành động xóa tất cả
            deleteAllUsers();
            return true;

        } else if (id == R.id.miAbout) {
            // Thực hiện hành động sắp xếp danh sách
            sortUserList();
            return true;

        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllUsers() {
        // Code để xóa tất cả người dùng trong Firebase hoặc danh sách hiển thị
    }

    private void sortUserList() {
        // Code để sắp xếp danh sách người dùng
    }
}

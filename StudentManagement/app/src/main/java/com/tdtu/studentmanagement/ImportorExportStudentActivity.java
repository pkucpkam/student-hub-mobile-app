package com.tdtu.studentmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tdtu.studentmanagement.students.Student;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImportorExportStudentActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Button btnExport, btnImport;
    private static final int REQUEST_WRITE_PERMISSION = 100;
    private static final int REQUEST_READ_PERMISSION = 101;
    private static final int PICK_FILE_REQUEST_CODE = 102;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importor_export_student);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        }

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students");

        // Initialize the export button
        btnExport = findViewById(R.id.btnExport);
        btnExport.setOnClickListener(v -> exportStudentDataToExcel());

        // Initialize the import button
        btnImport = findViewById(R.id.btnImport);
        btnImport.setOnClickListener(v -> openFilePicker());
    }

    // Method to export student data to Excel
    private void exportStudentDataToExcel() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Student> studentList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null) {
                        studentList.add(student);
                    }
                }

                createExcelFile(studentList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImportorExportStudentActivity.this, "Failed to fetch student data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to create an Excel file from student data
    private void createExcelFile(List<Student> studentList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Student ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Age");
        headerRow.createCell(3).setCellValue("Phone Number");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("Address");
        headerRow.createCell(6).setCellValue("Status");
        headerRow.createCell(7).setCellValue("Grade");
        headerRow.createCell(8).setCellValue("Student Class");

        int rowIndex = 1;
        for (Student student : studentList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(student.getStudentId());
            row.createCell(1).setCellValue(student.getName());
            row.createCell(2).setCellValue(student.getAge());
            row.createCell(3).setCellValue(student.getPhoneNumber());
            row.createCell(4).setCellValue(student.getEmail());
            row.createCell(5).setCellValue(student.getAddress());
            row.createCell(6).setCellValue(student.getStatus());
            row.createCell(7).setCellValue(student.getGrade());
            row.createCell(8).setCellValue(student.getStudentClass());
        }

        // Save the file
        try {
            File file;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = new File(getExternalFilesDir(null), "Students_Data.xlsx");
            } else {
                file = new File(Environment.getExternalStorageDirectory(), "Students_Data.xlsx");
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            Toast.makeText(ImportorExportStudentActivity.this, "Export successful. File saved at " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(ImportorExportStudentActivity.this, "Failed to save Excel file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Open file picker to select an Excel file
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    // Handle the result of file picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                importStudentDataFromExcel(uri);
            }
        }
    }

    // Method to import student data from Excel
    private void importStudentDataFromExcel(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<Student> studentList = new ArrayList<>();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                Student student = new Student();
                student.setStudentId(row.getCell(0).getStringCellValue());
                student.setName(row.getCell(1).getStringCellValue());
                student.setAge((int) row.getCell(2).getNumericCellValue());
                student.setPhoneNumber(row.getCell(3).getStringCellValue());
                student.setEmail(row.getCell(4).getStringCellValue());
                student.setAddress(row.getCell(5).getStringCellValue());
                student.setStatus(row.getCell(6).getStringCellValue());
                student.setGrade((float) row.getCell(7).getNumericCellValue());
                student.setStudentClass(row.getCell(8).getStringCellValue());

                studentList.add(student);
            }

            workbook.close();
            inputStream.close();

            storeStudentDataInFirebase(studentList);
            Toast.makeText(this, "Import Successful!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to import data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Store student data in Firebase
    private void storeStudentDataInFirebase(List<Student> studentList) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Students");
        for (Student student : studentList) {
            database.push().setValue(student); // Add student to Firebase
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            exportStudentDataToExcel();
        } else {
            Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == REQUEST_READ_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        } else {
            Toast.makeText(this, "Permission denied to read from storage", Toast.LENGTH_SHORT).show();
        }
    }
}

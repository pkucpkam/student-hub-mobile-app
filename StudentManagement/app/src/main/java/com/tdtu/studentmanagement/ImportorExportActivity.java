package com.tdtu.studentmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
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
import com.tdtu.studentmanagement.certificates.Certificate;
import com.tdtu.studentmanagement.students.Student;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImportorExportActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Button btnExportStudent, btnImportStudent, btnImportCertificate, btnExportCertificate;
    private static final int REQUEST_WRITE_PERMISSION = 100;
    private static final int PICK_FILE_REQUEST_CODE = 102;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importor_export);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students");

        btnExportStudent = findViewById(R.id.btnExportStudent);
        btnExportStudent.setOnClickListener(v -> exportStudentDataToExcel());

        btnImportStudent = findViewById(R.id.btnImportStudent);
        btnImportStudent.setOnClickListener(v -> openFilePicker());

        btnExportCertificate = findViewById(R.id.btnExportCertificate);
        btnExportCertificate.setOnClickListener(v -> exportCertificateDataToExcel());

        btnImportCertificate = findViewById(R.id.btnImportCertificate);
        btnImportCertificate.setOnClickListener(v -> openCertificateFilePicker());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Kết thúc Activity để trở về trang trước
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Toast.makeText(ImportorExportActivity.this, "Failed to fetch student data", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(ImportorExportActivity.this, "Export successful. File saved at " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(ImportorExportActivity.this, "Failed to save Excel file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Open file picker to select an Excel file
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                importStudentDataFromExcel(uri);
            }
        } else if (requestCode == PICK_FILE_REQUEST_CODE + 1 && resultCode == RESULT_OK) { // Import Certificate
            Uri uri = data.getData();
            if (uri != null) {
                importCertificateDataFromExcel(uri);
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

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) { // Bắt đầu từ dòng 1, bỏ qua tiêu đề
                Row row = sheet.getRow(i);
                Student student = new Student();

                // Đọc từng cột với kiểm tra kiểu dữ liệu
                if (row.getCell(0) != null) {

                    student.setName(getStringCellValue(row.getCell(0))); // Name (String)
                }

                if (row.getCell(1) != null) {
                    student.setAge((int) getNumericCellValue(row.getCell(1))); // Age (Number)
                }

                if (row.getCell(2) != null) {
                    student.setPhoneNumber(getStringCellValue(row.getCell(2))); // Phone Number (String)
                }

                if (row.getCell(3) != null) {
                    student.setEmail(getStringCellValue(row.getCell(3))); // Email (String)
                }

                if (row.getCell(4) != null) {
                    student.setAddress(getStringCellValue(row.getCell(4))); // Address (String)
                }

                if (row.getCell(5) != null) {
                    student.setStatus(getStringCellValue(row.getCell(5))); // Status (String)
                }

                if (row.getCell(6) != null) {
                    student.setGrade((float) getNumericCellValue(row.getCell(6))); // Grade (Number)
                }

                if (row.getCell(7) != null) {
                    student.setStudentClass(getStringCellValue(row.getCell(7))); // Student Class (String)
                }

                // Lấy thời gian hiện tại làm createdAt và updatedAt

                String formattedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                student.setCreatedAt(formattedTime);
                student.setUpdatedAt(formattedTime);

                Log.d("ExcelImport", student.toString());
                studentList.add(student);
            }

            workbook.close();
            inputStream.close();

            // Lưu dữ liệu vào Firebase
            storeStudentDataInFirebase(studentList);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to import data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Helper method to safely get string value
    private String getStringCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue()); // Convert numeric to string
        } else {
            return ""; // Default empty string if cell is blank
        }
    }

    // Helper method to safely get numeric value
    private double getNumericCellValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue()); // Convert string to number
            } catch (NumberFormatException e) {
                Log.e("ExcelImport", "Invalid numeric value in cell");
                return 0; // Default value if conversion fails
            }
        } else {
            return 0; // Default value if cell is blank
        }
    }

    // Store student data in Firebase
    private void storeStudentDataInFirebase(List<Student> studentList) {
        DatabaseReference database = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Students");

        for (Student student : studentList) {
            // Sử dụng push() để Firebase tự động tạo ID duy nhất
            DatabaseReference newStudentRef = database.push();
            String generatedId = newStudentRef.getKey(); // Lấy ID do Firebase tạo
            student.setStudentId(generatedId); // Gán ID tự động vào student

            Log.d("FirebaseImport", "Generated ID: " + generatedId);

            // Lưu sinh viên vào Firebase
            newStudentRef.setValue(student)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("FirebaseImport", "Successfully added: " + student.getName());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseImport", "Failed to add: " + student.getName(), e);
                    });
        }
    }



//    Certificate
// Method to export certificate data to Excel
private void exportCertificateDataToExcel() {
    DatabaseReference certificateRef = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Certificates");

    certificateRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Certificate> certificateList = new ArrayList<>();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Certificate certificate = snapshot.getValue(Certificate.class);
                if (certificate != null) {
                    certificateList.add(certificate);
                }
            }
            createCertificateExcelFile(certificateList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(ImportorExportActivity.this, "Failed to fetch certificate data", Toast.LENGTH_SHORT).show();
        }
    });
}

    private void createCertificateExcelFile(List<Certificate> certificateList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Certificates");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Certificate ID");
        headerRow.createCell(1).setCellValue("Student ID");
        headerRow.createCell(2).setCellValue("Certificate Name");
        headerRow.createCell(3).setCellValue("Issue Date");
        headerRow.createCell(4).setCellValue("Expiry Date");
        headerRow.createCell(5).setCellValue("Created At");
        headerRow.createCell(6).setCellValue("Updated At");

        int rowIndex = 1;
        for (Certificate certificate : certificateList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(certificate.getCertificateId());
            row.createCell(1).setCellValue(certificate.getStudentId());
            row.createCell(2).setCellValue(certificate.getCertificateName());
            row.createCell(3).setCellValue(certificate.getIssueDate());
            row.createCell(4).setCellValue(certificate.getExpiryDate());
            row.createCell(5).setCellValue(certificate.getCreatedAt());
            row.createCell(6).setCellValue(certificate.getUpdatedAt());
        }

        // Save the file
        try {
            File file;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = new File(getExternalFilesDir(null), "Certificates_Data.xlsx");
            } else {
                file = new File(Environment.getExternalStorageDirectory(), "Certificates_Data.xlsx");
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            Toast.makeText(ImportorExportActivity.this, "Export successful. File saved at " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(ImportorExportActivity.this, "Failed to save Excel file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void openCertificateFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE + 1); // Sử dụng mã khác để phân biệt với Student
    }

    private void importCertificateDataFromExcel(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<Certificate> certificateList = new ArrayList<>();

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) { // Bỏ qua dòng tiêu đề
                Row row = sheet.getRow(i);
                Certificate certificate = new Certificate();

                if (row.getCell(0) != null) {
                    certificate.setStudentId(getStringCellValue(row.getCell(0))); // Student ID
                }

                if (row.getCell(1) != null) {
                    certificate.setCertificateName(getStringCellValue(row.getCell(1))); // Certificate Name
                }

                if (row.getCell(2) != null) {
                    certificate.setIssueDate(getStringCellValue(row.getCell(2))); // Issue Date
                }

                if (row.getCell(3) != null) {
                    certificate.setExpiryDate(getStringCellValue(row.getCell(3))); // Expiry Date
                }

                // Tạo certificateId tự động và thêm thời gian hiện tại
                String formattedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                certificate.setCreatedAt(formattedTime);
                certificate.setUpdatedAt(formattedTime);

                Log.d("ExcelImport", certificate.toString());
                certificateList.add(certificate);
            }

            workbook.close();
            inputStream.close();

            // Lưu dữ liệu vào Firebase
            storeCertificateDataInFirebase(certificateList);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to import data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void storeCertificateDataInFirebase(List<Certificate> certificateList) {
        DatabaseReference certificateRef = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Certificates");

        for (Certificate certificate : certificateList) {
            // Tạo ID tự động
            DatabaseReference newCertificateRef = certificateRef.push();
            String generatedId = newCertificateRef.getKey(); // Lấy ID do Firebase tạo
            certificate.setCertificateId(generatedId); // Gán ID tự động vào Certificate

            // Lưu chứng chỉ vào Firebase
            newCertificateRef.setValue(certificate)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ImportorExportActivity.this, "Import successfully", Toast.LENGTH_SHORT).show();
                        Log.d("FirebaseImport", "Successfully added: " + certificate.getCertificateName());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirebaseImport", "Failed to add: " + certificate.getCertificateName(), e);
                    });
        }
    }


}

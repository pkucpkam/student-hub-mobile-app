package com.tdtu.studentmanagement.students;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvStudentName;
    TextView tvStudentPhone;
    TextView tvStudentEmail;
    TextView tvStudentAddress;
    Button btnDeleteStudent, btnEditStudent;

    public ViewHolder(View itemView) {
        super(itemView);
        tvStudentName = itemView.findViewById(R.id.tvStudentName);
        tvStudentPhone = itemView.findViewById(R.id.tvStudentPhone);
        tvStudentEmail = itemView.findViewById(R.id.tvStudentEmail);
        tvStudentAddress = itemView.findViewById(R.id.tvStudentAddress);
        btnEditStudent = itemView.findViewById(R.id.btnEditStudent);
        btnDeleteStudent = itemView.findViewById(R.id.btnDeleteStudent);
    }

    public void bind(Student student) {
        tvStudentName.setText(student.getName());
        tvStudentPhone.setText(student.getPhoneNumber());
        tvStudentEmail.setText(student.getEmail());
        tvStudentAddress.setText(student.getAddress());
    }
}


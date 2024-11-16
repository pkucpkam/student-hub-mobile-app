package com.tdtu.studentmanagement.students;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tdtu.studentmanagement.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvStudentName;
    TextView tvStudentAge;
    TextView tvStudentGrade;
    TextView tvStudentPhone;
    TextView tvStudentEmail;
    TextView tvStudentAddress;
    Button btnDeleteStudent, btnDetailStudent;

    public ViewHolder(View itemView) {
        super(itemView);
        tvStudentName = itemView.findViewById(R.id.tvStudentName);
        tvStudentAge = itemView.findViewById(R.id.tvStudentAge);
        tvStudentGrade = itemView.findViewById(R.id.tvStudentGrade);
        tvStudentPhone = itemView.findViewById(R.id.tvStudentPhone);
        tvStudentEmail = itemView.findViewById(R.id.tvStudentEmail);
        tvStudentAddress = itemView.findViewById(R.id.tvStudentAddress);
        btnDetailStudent = itemView.findViewById(R.id.btnDetailStudent);
        btnDeleteStudent = itemView.findViewById(R.id.btnDeleteStudent);
    }

    public void bind(Student student) {
        tvStudentName.setText(student.getName());
        tvStudentAge.setText("Age: " + student.getAge());
        tvStudentGrade.setText("Grade: " + student.getGrade());
        tvStudentPhone.setText("Phone: " + student.getPhoneNumber());
        tvStudentEmail.setText("Email: " + student.getEmail());
        tvStudentAddress.setText("Address: " + student.getAddress());
    }

}

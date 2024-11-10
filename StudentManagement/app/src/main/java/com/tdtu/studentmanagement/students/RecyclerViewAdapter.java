package com.tdtu.studentmanagement.students;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.EditStudentInformationActivity;
import com.tdtu.studentmanagement.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<Student> studentList;

    public RecyclerViewAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.bind(student);

        holder.btnDeleteStudent.setOnClickListener(v -> {
            studentList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, studentList.size());
        });

        holder.btnEditStudent.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditStudentInformationActivity.class);

            // Truyền thông tin sinh viên qua Intent
            intent.putExtra("studentId", student.getStudentId());
            intent.putExtra("name", student.getName());
            intent.putExtra("age", student.getAge());
            intent.putExtra("phoneNumber", student.getPhoneNumber());
            intent.putExtra("email", student.getEmail());
            intent.putExtra("address", student.getAddress());

            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }

    // Cập nhật dữ liệu trong adapter
    public void updateStudentList(List<Student> newList) {
        this.studentList = newList;
        notifyDataSetChanged();
    }
}


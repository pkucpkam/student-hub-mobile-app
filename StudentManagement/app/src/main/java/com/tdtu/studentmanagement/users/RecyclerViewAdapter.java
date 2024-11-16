package com.tdtu.studentmanagement.users;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tdtu.studentmanagement.R;
import com.tdtu.studentmanagement.UserMainActivity;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<User> userList;
    private DatabaseReference databaseReference;

    public RecyclerViewAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    // RecyclerViewAdapter.java
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        // Hiển thị thông tin cơ bản
        holder.tvUserName.setText(user.getName());
        holder.tvUserPhone.setText("Phone: " + user.getPhoneNumber());
        holder.tvUserRole.setText("Role: " + user.getRole());

        // Xử lý khi người dùng nhấn vào nút "Detail"
        holder.btnDetailUser.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserMainActivity.class);

            // Truyền dữ liệu của người dùng qua Intent
            intent.putExtra("userId", user.getUserId());
            intent.putExtra("username", user.getName());
            intent.putExtra("role", user.getRole());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("age", user.getAge());
            intent.putExtra("phoneNumber", user.getPhoneNumber());
            intent.putExtra("status", user.getStatus());
            intent.putExtra("createdAt", user.getCreatedAt());
            intent.putExtra("updatedAt", user.getUpdatedAt());

            // Chuyển tới UserMainActivity
            context.startActivity(intent);
        });

        holder.btnDeleteUser.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this user?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteUser(user, position))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deleteUser(User user, int position) {
        // Cập nhật trạng thái của người dùng thành "locked" trong Firebase Realtime Database
        databaseReference.child(user.getUserId()).child("status").setValue("Locked")
                .addOnSuccessListener(aVoid -> {
                    // Thông báo trạng thái đã được cập nhật
                    Toast.makeText(context, "Delete user successfully'", Toast.LENGTH_SHORT).show();

                    // Nếu cần cập nhật giao diện danh sách người dùng
                    userList.get(position).setStatus("locked");
                    notifyItemChanged(position);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to update user status", Toast.LENGTH_SHORT).show()
                );
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Phương thức để cập nhật danh sách user
    public void setUsers(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    // Phương thức để thực hiện tìm kiếm và lọc danh sách người dùng
    public void updateData(List<User> filteredUsers) {
        this.userList = filteredUsers;
        notifyDataSetChanged();
    }
}

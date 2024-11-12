package com.tdtu.studentmanagement.users;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvUserName.setText(user.getName() != null ? user.getName() : "No Name");
        holder.tvUserPhone.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "No Phone");
        holder.tvUserStatus.setText(user.getStatus() != null ? user.getStatus() : "No Status");

        // Xử lý khi người dùng nhấn vào nút Edit
        holder.btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserMainActivity.class);
            intent.putExtra("USER_ID", user.getUserId());
            context.startActivity(intent);
        });

        // Xử lý khi người dùng nhấn vào nút Delete
        holder.btnDelete.setOnClickListener(view -> deleteUser(user, position));
    }

    private void deleteUser(User user, int position) {
        // Xóa người dùng từ Firebase Realtime Database
        databaseReference.child(user.getUserId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Xóa người dùng khỏi danh sách và cập nhật RecyclerView
                    userList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, userList.size());
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();

                    // Nếu cần xóa người dùng khỏi Firebase Authentication
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null && currentUser.getUid().equals(user.getUserId())) {
                        currentUser.delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "User account deleted from Authentication", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to delete from Authentication", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show());
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

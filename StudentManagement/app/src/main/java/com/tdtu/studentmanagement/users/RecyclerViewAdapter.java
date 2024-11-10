package com.tdtu.studentmanagement.users;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.EditUserInformationActivity;
import com.tdtu.studentmanagement.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<User> userList;

    public RecyclerViewAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
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

        // Xử lý khi nhấn giữ vào item để hiện context menu (nếu có)
        holder.itemView.setOnLongClickListener(view -> {
            view.showContextMenu();
            return true;
        });

        // Xử lý khi người dùng nhấn vào nút Edit
        holder.btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditUserInformationActivity.class);
            intent.putExtra("USER_ID", user.getUserId()); // Chuyển ID người dùng để chỉnh sửa
            context.startActivity(intent);
        });
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

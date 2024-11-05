package com.tdtu.studentmanagement.users;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.EditUserInformationActivity;
import com.tdtu.studentmanagement.MainActivity;
import com.tdtu.studentmanagement.R;
import com.tdtu.studentmanagement.UserManagementActivity;

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
        holder.tvUserStatus.setText(user.getStatus() != null ? user.getStatus() :  "No Role" );

        // Xử lý khi nhấn giữ vào item để hiện context menu (nếu có)
        holder.itemView.setOnLongClickListener(view -> {
            view.showContextMenu();
            return true;
        });

        // Xử lý logic khác nếu cần thêm
        holder.btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditUserInformationActivity.class);
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
}


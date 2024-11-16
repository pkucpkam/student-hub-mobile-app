package com.tdtu.studentmanagement.users;

import com.tdtu.studentmanagement.R;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvUserName, tvUserPhone, tvUserRole;
    Button btnDeleteUser, btnDetailUser;

    @SuppressLint("UseSwitchCompatOrMaterialCode")

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvUserName = itemView.findViewById(R.id.tvUserName);
        tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
        tvUserRole = itemView.findViewById(R.id.tvUserRole);
        btnDetailUser = itemView.findViewById(R.id.btnDetailUser);
        btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
    }
}


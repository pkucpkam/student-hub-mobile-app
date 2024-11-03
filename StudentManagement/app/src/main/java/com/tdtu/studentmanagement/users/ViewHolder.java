package com.tdtu.studentmanagement.users;

import com.tdtu.studentmanagement.R;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvUserName, tvUserPhone, tvUserStatus;

    @SuppressLint("UseSwitchCompatOrMaterialCode")

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvUserName = itemView.findViewById(R.id.tvUserName);
        tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
        tvUserStatus = itemView.findViewById(R.id.tvUserStatus);
    }
}


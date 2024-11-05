package com.tdtu.studentmanagement.history;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvUsername, tvRole, tvLoginTimestamp, tvLogoutTimestamp;

    public ViewHolder(View itemView) {
        super(itemView);
        tvUsername = itemView.findViewById(R.id.tvUsername);
        tvRole = itemView.findViewById(R.id.tvRole);
        tvLoginTimestamp = itemView.findViewById(R.id.tvLoginTimestamp);
        tvLogoutTimestamp = itemView.findViewById(R.id.tvLogoutTimestamp);
    }

    public void bind(LoginHistory loginHistory) {
        tvUsername.setText("Username: " + loginHistory.getUsername());
        tvRole.setText("Role: " + loginHistory.getRole());
        tvLoginTimestamp.setText("Login time: " + loginHistory.getLoginTimestamp().toString());

        if (loginHistory.getLogoutTimestamp() != null) {
            tvLogoutTimestamp.setText("Logout time: " + loginHistory.getLogoutTimestamp().toString());
        } else {
            tvLogoutTimestamp.setText("Logout time: Active");
        }
    }
}

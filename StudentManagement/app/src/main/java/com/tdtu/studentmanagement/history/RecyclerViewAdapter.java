package com.tdtu.studentmanagement.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<LoginHistory> loginHistoryList;

    public RecyclerViewAdapter(Context context, List<LoginHistory> loginHistoryList) {
        this.context = context;
        this.loginHistoryList = loginHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_login_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoginHistory loginHistory = loginHistoryList.get(position);
        holder.bind(loginHistory);
    }

    @Override
    public int getItemCount() {
        return loginHistoryList.size();
    }
}


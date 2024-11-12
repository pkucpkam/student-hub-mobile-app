package com.tdtu.studentmanagement.certificates;

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
    private List<Certificate> certificateList;

    public RecyclerViewAdapter(Context context, List<Certificate> certificateList) {
        this.context = context;
        this.certificateList = certificateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_certificate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Certificate certificate = certificateList.get(position);
        holder.bind(certificate);
    }

    @Override
    public int getItemCount() {
        return certificateList.size();
    }

    // Phương thức cập nhật danh sách chứng chỉ
    public void updateCertificateList(List<Certificate> newCertificateList) {
        this.certificateList = newCertificateList;
        notifyDataSetChanged();
    }
}

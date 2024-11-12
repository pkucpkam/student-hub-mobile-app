package com.tdtu.studentmanagement.certificates;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.EditCertificateActivity;
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

        // Lắng nghe sự kiện nút Delete
        holder.btnDeleteCertificate.setOnClickListener(v -> {
            certificateList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, certificateList.size());
        });

        // Lắng nghe sự kiện nút Edit
        holder.btnEditCertificate.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditCertificateActivity.class);

            // Truyền thông tin chứng chỉ qua Intent
            intent.putExtra("certificate_id", certificate.getCertificate_id());
            intent.putExtra("student_id", certificate.getStudent_id());
            intent.putExtra("certificate_name", certificate.getCertificate_name());
            intent.putExtra("issue_date", certificate.getIssue_date());
            intent.putExtra("expiry_date", certificate.getExpiry_date());
            intent.putExtra("created_at", certificate.getCreated_at());
            intent.putExtra("updated_at", certificate.getUpdated_at());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return certificateList.size();
    }

    // Cập nhật danh sách chứng chỉ trong adapter
    public void updateCertificateList(List<Certificate> newCertificateList) {
        this.certificateList = newCertificateList;
        notifyDataSetChanged();
    }
}

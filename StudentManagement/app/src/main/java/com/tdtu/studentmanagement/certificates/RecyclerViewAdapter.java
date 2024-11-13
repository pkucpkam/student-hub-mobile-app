package com.tdtu.studentmanagement.certificates;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tdtu.studentmanagement.DetailCertificateActivity;
import com.tdtu.studentmanagement.EditCertificateActivity;
import com.tdtu.studentmanagement.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<Certificate> certificateList;
    private String role;
    private DatabaseReference databaseReference;

    public RecyclerViewAdapter(Context context, List<Certificate> certificateList, String role) {
        this.context = context;
        this.certificateList = certificateList;
        this.role = role;
        this.databaseReference = FirebaseDatabase.getInstance("https://midterm-project-b5158-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Certificates");
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

        // Kiểm tra role và ẩn nút xóa nếu role là employee
        if ("employee".equals(role)) {
            holder.btnDeleteCertificate.setVisibility(View.GONE);
        } else {
            holder.btnDeleteCertificate.setVisibility(View.VISIBLE);
        }

        holder.btnDeleteCertificate.setOnClickListener(v -> deleteCertificate(certificate, position));

        holder.btnDetailCertificate.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailCertificateActivity.class);

            // Truyền thông tin chứng chỉ qua Intent
            intent.putExtra("certificate_id", certificate.getCertificateId());
            intent.putExtra("student_id", certificate.getStudentId());
            intent.putExtra("certificate_name", certificate.getCertificateName());
            intent.putExtra("issue_date", certificate.getIssueDate());
            intent.putExtra("expiry_date", certificate.getExpiryDate());
            intent.putExtra("created_at", certificate.getCreatedAt());
            intent.putExtra("updated_at", certificate.getUpdatedAt());

            context.startActivity(intent);
        });

        holder.btnDeleteCertificate.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this certificate?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteCertificate(certificate, position))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return certificateList.size();
    }

    // Phương thức xóa chứng chỉ khỏi Firebase
    private void deleteCertificate(Certificate certificate, int position) {
        databaseReference.child(certificate.getCertificateId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Xóa thành công, cập nhật danh sách và thông báo
                    certificateList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, certificateList.size());
                    Toast.makeText(context, "Certificate deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Thông báo lỗi nếu xóa thất bại
                    Toast.makeText(context, "Failed to delete certificate", Toast.LENGTH_SHORT).show();
                });
    }

    // Cập nhật danh sách chứng chỉ trong adapter
    public void updateCertificateList(List<Certificate> newCertificateList) {
        this.certificateList = newCertificateList;
        notifyDataSetChanged();
    }
}

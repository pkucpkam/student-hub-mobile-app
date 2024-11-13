package com.tdtu.studentmanagement.certificates;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvCertificateName;
    TextView tvIssueDate;
    TextView tvExpiryDate;
    Button btnDetailCertificate, btnDeleteCertificate;

    public ViewHolder(View itemView) {
        super(itemView);
        tvCertificateName = itemView.findViewById(R.id.tvCertificateName);
        tvIssueDate = itemView.findViewById(R.id.tvIssueDate);
        tvExpiryDate = itemView.findViewById(R.id.tvExpiryDate);
        btnDetailCertificate = itemView.findViewById(R.id.btnDetailCertificate);
        btnDeleteCertificate = itemView.findViewById(R.id.btnDeleteCertificate);
    }

    public void bind(Certificate certificate) {
        tvCertificateName.setText(certificate.getCertificateName());
        tvIssueDate.setText("Date of issue: " + certificate.getIssueDate());
        tvExpiryDate.setText("Expiration date: " + certificate.getExpiryDate());
    }
}

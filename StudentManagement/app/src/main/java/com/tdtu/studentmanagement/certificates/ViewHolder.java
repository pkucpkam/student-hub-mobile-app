package com.tdtu.studentmanagement.certificates;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.studentmanagement.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvCertificateName;
    TextView tvIssueDate;
    TextView tvExpiryDate;

    public ViewHolder(View itemView) {
        super(itemView);
        tvCertificateName = itemView.findViewById(R.id.tvCertificateName);
        tvIssueDate = itemView.findViewById(R.id.tvIssueDate);
        tvExpiryDate = itemView.findViewById(R.id.tvExpiryDate);
    }

    public void bind(Certificate certificate) {
        tvCertificateName.setText(certificate.getCertificateName());
        tvIssueDate.setText("Ngày cấp: " + certificate.getIssueDate().toString());
        tvExpiryDate.setText("Ngày hết hạn: " + certificate.getExpiryDate().toString());
    }
}

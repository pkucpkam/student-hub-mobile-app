package com.tdtu.studentmanagement.certificates;

import java.sql.Date;
import java.sql.Timestamp;

public class Certificate {
    private int certificateId;
    private int studentId;
    private String certificateName;
    private Date issueDate;
    private Date expiryDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Certificate(int certificateId, int studentId, String certificateName, Date issueDate, Date expiryDate,
                       Timestamp createdAt, Timestamp updatedAt) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.certificateName = certificateName;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt != null ? createdAt : new Timestamp(System.currentTimeMillis());
        this.updatedAt = updatedAt != null ? updatedAt : new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}


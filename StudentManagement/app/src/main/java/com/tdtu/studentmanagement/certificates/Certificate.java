package com.tdtu.studentmanagement.certificates;

import java.text.SimpleDateFormat;

public class Certificate {
    private String certificateId;
    private String studentId;
    private String certificateName;
    private String issueDate;
    private String expiryDate;
    private String createdAt;
    private String updatedAt;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Certificate(String certificateId, String studentId, String certificateName, String issueDate, String expiryDate, String createdAt, String updatedAt) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.certificateName = certificateName;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Certificate() {
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "certificateId='" + certificateId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}

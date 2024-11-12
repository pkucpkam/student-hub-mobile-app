package com.tdtu.studentmanagement.certificates;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Certificate {
    private int certificate_id;
    private int student_id;
    private String certificate_name;
    private String issue_date;
    private String expiry_date;
    private String created_at;
    private String updated_at;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Certificate(int certificate_id, int student_id, String certificate_name, String issue_date, String expiry_date, String created_at, String updated_at) {
        this.certificate_id = certificate_id;
        this.student_id = student_id;
        this.certificate_name = certificate_name;
        this.issue_date = issue_date;
        this.expiry_date = expiry_date;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Certificate() {
    }

    public int getCertificate_id() {
        return certificate_id;
    }

    public void setCertificate_id(int certificate_id) {
        this.certificate_id = certificate_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getCertificate_name() {
        return certificate_name;
    }

    public void setCertificate_name(String certificate_name) {
        this.certificate_name = certificate_name;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(String issue_date) {
        this.issue_date = issue_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "certificate_id=" + certificate_id +
                ", student_id=" + student_id +
                ", certificate_name='" + certificate_name + '\'' +
                ", issue_date=" + issue_date +
                ", expiry_date=" + expiry_date +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
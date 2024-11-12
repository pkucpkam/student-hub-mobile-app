package com.tdtu.studentmanagement.students;

public class Student {
    private String studentId;
    private String name;
    private int age;
    private String phoneNumber;
    private String email;
    private String address;
    private String createdAt;
    private String updatedAt;
    private String status;

    public Student(String studentId, String name, int age, String phoneNumber, String email, String address,
                   String createdAt, String updatedAt, String status) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.createdAt = createdAt != null ? createdAt : String.valueOf(System.currentTimeMillis());
        this.updatedAt = updatedAt != null ? updatedAt : String.valueOf(System.currentTimeMillis());
        this.status = status;
    }

    public Student() {
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

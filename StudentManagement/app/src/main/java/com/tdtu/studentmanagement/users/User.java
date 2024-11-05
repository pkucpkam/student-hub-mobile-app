package com.tdtu.studentmanagement.users;

import java.sql.Timestamp;

public class User {
    private String userId;
    private String username;
    private String password;
    private String role;
    private String name;
    private int age;
    private String phoneNumber;
    private String status;
    private String profilePicture;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public enum Status {
        NORMAL,
        LOCKED
    }

    public User(String userId, String username, String password, String role, String name, int age,
                String phoneNumber, String status, String profilePicture, Timestamp createdAt,
                Timestamp updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.status =  status;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt != null ? createdAt : new Timestamp(System.currentTimeMillis());
        this.updatedAt = updatedAt != null ? updatedAt : new Timestamp(System.currentTimeMillis());
    }

    public User() {
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", profilePicture='" + profilePicture + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}


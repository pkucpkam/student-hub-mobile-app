package com.tdtu.studentmanagement.history;

import com.google.firebase.database.PropertyName;

public class LoginHistory {
    private String loginId;
    private String userId;
    private String username;
    private String role;
    private String loginTimestamp;

    public LoginHistory() {
    }

    public LoginHistory(String loginId, String userId, String username, String role, String loginTimestamp) {
        this.loginId = loginId;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.loginTimestamp = loginTimestamp;
    }

    // Getters and Setters
    @PropertyName("loginId")
    public String getLoginId() {
        return loginId;
    }

    @PropertyName("loginId")
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @PropertyName("userId")
    public String getUserId() {
        return userId;
    }

    @PropertyName("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @PropertyName("login_timestamp")
    public String getLoginTimestamp() {
        return loginTimestamp;
    }

    @PropertyName("login_timestamp")
    public void setLoginTimestamp(String loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }


}

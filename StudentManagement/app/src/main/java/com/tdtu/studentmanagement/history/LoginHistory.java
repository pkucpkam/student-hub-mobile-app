package com.tdtu.studentmanagement.history;

import com.google.firebase.database.PropertyName;

public class LoginHistory {
    private int login_id;
    private int user_id;
    private String username;
    private String role;
    private String loginTimestamp;
    private String logoutTimestamp;

    public LoginHistory() {
    }

    public LoginHistory(int loginId, int userId, String username, String role, String loginTimestamp, String logoutTimestamp) {
        this.login_id = loginId;
        this.user_id = userId;
        this.username = username;
        this.role = role;
        this.loginTimestamp = loginTimestamp;
        this.logoutTimestamp = logoutTimestamp;
    }

    // Getters and Setters
    @PropertyName("login_id")
    public int getLoginId() {
        return login_id;
    }

    @PropertyName("login_id")
    public void setLoginId(int loginId) {
        this.login_id = loginId;
    }

    @PropertyName("user_id")
    public int getUserId() {
        return user_id;
    }

    @PropertyName("user_id")
    public void setUserId(int userId) {
        this.user_id = userId;
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

    @PropertyName("logout_timestamp")
    public String getLogoutTimestamp() {
        return logoutTimestamp;
    }

    @PropertyName("logout_timestamp")
    public void setLogoutTimestamp(String logoutTimestamp) {
        this.logoutTimestamp = logoutTimestamp;
    }
}

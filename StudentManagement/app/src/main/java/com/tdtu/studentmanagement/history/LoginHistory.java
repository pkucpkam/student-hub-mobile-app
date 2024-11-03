package com.tdtu.studentmanagement.history;

import java.sql.Timestamp;

public class LoginHistory {
    private int loginId;
    private int userId;
    private String username;
    private String role;
    private Timestamp loginTimestamp;
    private Timestamp logoutTimestamp;

    public LoginHistory(int loginId, int userId, String username, String role, Timestamp loginTimestamp, Timestamp logoutTimestamp) {
        this.loginId = loginId;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.loginTimestamp = loginTimestamp != null ? loginTimestamp : new Timestamp(System.currentTimeMillis());
        this.logoutTimestamp = logoutTimestamp;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public Timestamp getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(Timestamp loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    public Timestamp getLogoutTimestamp() {
        return logoutTimestamp;
    }

    public void setLogoutTimestamp(Timestamp logoutTimestamp) {
        this.logoutTimestamp = logoutTimestamp;
    }
}

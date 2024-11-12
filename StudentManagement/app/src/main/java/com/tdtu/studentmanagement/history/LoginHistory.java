package com.tdtu.studentmanagement.history;

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
        this.loginId = loginId;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.loginTimestamp = loginTimestamp;
        this.logoutTimestamp = logoutTimestamp;
    }

    // Getters and Setters
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

    public String getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(String loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    public String getLogoutTimestamp() {
        return logoutTimestamp;
    }

    public void setLogoutTimestamp(String logoutTimestamp) {
        this.logoutTimestamp = logoutTimestamp;
    }
}

package com.getcoupon.bmob;

public class User {
    private String alreadylogin; // 已登录
    private String autoLogin; // 自动登录
    private String username; // 账号
    private String password; // 密码
    private long relationID; // 关系ID

    public void setAlreadylogin(String alreadylogin) {
        this.alreadylogin = alreadylogin;
    }

    public void setAutoLogin(String autoLogin) {
        this.autoLogin = autoLogin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRelationID(long relationID) {
        this.relationID = relationID;
    }

    public String getAlreadylogin() {
        return alreadylogin;
    }

    public String getAutoLogin() {
        return autoLogin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getRelationID() {
        return relationID;
    }
}
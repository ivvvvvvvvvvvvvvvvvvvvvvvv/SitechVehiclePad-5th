package com.sitechdev.vehicle.pad.module.login.bean;

import java.io.Serializable;

public class LoginRequestBean implements Serializable {
    public String grantType;
    public String username;
    public String password;

    public String refreshToken;
    public String code;

    /**
     * 账号密码登录
     */
    public LoginRequestBean(String grantType, String username, String password) {
        this.grantType = grantType;
        this.username = username;
        this.password = password;
    }

    /**
     * 二维码登录
     */
    public LoginRequestBean(String grantType, String code) {
        this.grantType = grantType;
        this.code = code;
    }

    /**
     * 自动登录，刷新用户身份
     */
    public LoginRequestBean(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

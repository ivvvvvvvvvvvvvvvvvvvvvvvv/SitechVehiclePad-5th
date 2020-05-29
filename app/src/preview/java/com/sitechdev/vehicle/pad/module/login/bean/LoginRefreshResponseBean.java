package com.sitechdev.vehicle.pad.module.login.bean;


import java.io.Serializable;

/**
 * 自动登录token信息
 */
public class LoginRefreshResponseBean implements Serializable {
    public String code;
    public String message;
    public LoginUserBean.CredentialBean data;
}

package com.sitechdev.vehicle.pad.module.login.bean;

import com.sitechdev.vehicle.pad.module.login.bean.LoginUserBean;

import java.io.Serializable;

public class LoginResponseBean implements Serializable {
    public String code;
    public String message;
    public LoginUserBean data;
}

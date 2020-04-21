package com.sitechdev.vehicle.pad.module.login.bean;

import java.io.Serializable;

public class LoginUserBean implements Serializable {

    public CredentialBean credential;
    public String nickName;
    public String avatarUrl;
    public String userId;

    public static class CredentialBean implements Serializable {
        public String accessToken;
        public String refreshToken;
        public String expiresIn;
    }
}

package com.sitechdev.vehicle.pad.bean;

import com.sitechdev.net.bean.BaseBean;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：User
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 20:49
 * 修改时间：
 * 备注：
 */
public class UserBean extends BaseBean {

    private static final long serialVersionUID = -6420469L;
    /**
     * userId : 100430100067
     * mobile : 15010279557
     * mobileVerified : true
     * email :
     * emailVerified : false
     * securityInit : false
     * faceInit : true
     * vehicleInit : false
     * authInit : false
     * langKey : zh_CN
     * avatarUrl :
     * nickName :
     * cardType : 0
     * cardId :
     * realName :
     * gender :
     * dateBirth : 0
     * postCode :
     * addressLine1 :
     * addressCity :
     * addressState :
     * addressCountry : CN
     * education :
     * industry :
     * job :
     * referralCode :
     * vehicleFaceInit : false
     * source : native_app
     * credential : {"accessToken":"VkhNFxLyk1bDnGivWmhKGhN209qlOzeErwjEvki3eNDGdO9aaqFuhUrnXjjnB8c2aYBJ/42cwFnC/q3b1Zj8Fg==","refreshToken":"VkhNFxLyk1Y7+uOUuIikRxN209qlOzeEzfexGSRJi01Vnv//9TFSW4OJ5Fm3IvyqF5j1Kqe9oYiWc2IDIpjOeKo3LbXHwiw+8WbLJBc5zOm6+EovoGONpAcrxFzYo1xh9HY6oAhmfi4=","expiresIn":299,"cmdTokenDTO":{"cmdToken":"VkhNFxLyk1Y7+uOUuIikRxN209qlOzeEdyrguCefAVNlyYO5Uslt1HA5BRSTRI/zPkXUEepFPn3DobrZLcuBZw==","cmdExpiresIn":7199}}
     */

    private String userId;
    private String mobile;
    private boolean mobileVerified;
    private String email;
    private boolean emailVerified;
    private boolean securityInit;//车控密码
    private boolean passwordInit;
    private boolean faceInit;//刷脸登录
    private boolean vehicleInit;
    private boolean authInit;//实名认证
    private String langKey;
    private String avatarUrl;
    private String nickName;
    private String cardType;
    private String cardId;
    private String realName;
    private String gender;
    private String dateBirth;
    private String postCode;
    private String addressLine1;
    private String addressCity;
    private String addressState;
    private String addressStateDesc;
    private String addressCityDesc;
    private String addressCountry;
    private String education;
    private String industry;
    private String job;
    private String referralCode;
    private boolean vehicleFaceInit;//车控刷脸
    private String source;
    private String introduction;
    private String level;
    private CredentialBean credential;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isSecurityInit() {
        return securityInit;
    }

    public void setSecurityInit(boolean securityInit) {
        this.securityInit = securityInit;
    }

    public boolean isFaceInit() {
        return faceInit;
    }

    public void setFaceInit(boolean faceInit) {
        this.faceInit = faceInit;
    }

    public boolean isVehicleInit() {
        return vehicleInit;
    }

    public void setVehicleInit(boolean vehicleInit) {
        this.vehicleInit = vehicleInit;
    }

    public boolean isAuthInit() {
        return authInit;
    }

    public void setAuthInit(boolean authInit) {
        this.authInit = authInit;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public boolean isVehicleFaceInit() {
        return vehicleFaceInit;
    }

    public void setVehicleFaceInit(boolean vehicleFaceInit) {
        this.vehicleFaceInit = vehicleFaceInit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CredentialBean getCredential() {
        if (credential == null) {
            credential = new CredentialBean();
        }
        return credential;
    }

    public void setCredential(CredentialBean credential) {
        this.credential = credential;
    }

    public boolean isPasswordInit() {
        return passwordInit;
    }

    public void setPasswordInit(boolean passwordInit) {
        this.passwordInit = passwordInit;
    }

    public String getAddressStateDesc() {
        return addressStateDesc;
    }

    public void setAddressStateDesc(String addressStateDesc) {
        this.addressStateDesc = addressStateDesc;
    }

    public String getAddressCityDesc() {
        return addressCityDesc;
    }

    public void setAddressCityDesc(String addressCityDesc) {
        this.addressCityDesc = addressCityDesc;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public static class CredentialBean {
        /**
         * accessToken : VkhNFxLyk1bDnGivWmhKGhN209qlOzeErwjEvki3eNDGdO9aaqFuhUrnXjjnB8c2aYBJ/42cwFnC/q3b1Zj8Fg==
         * refreshToken : VkhNFxLyk1Y7+uOUuIikRxN209qlOzeEzfexGSRJi01Vnv//9TFSW4OJ5Fm3IvyqF5j1Kqe9oYiWc2IDIpjOeKo3LbXHwiw+8WbLJBc5zOm6+EovoGONpAcrxFzYo1xh9HY6oAhmfi4=
         * expiresIn : 299
         * cmdTokenDTO : {"cmdToken":"VkhNFxLyk1Y7+uOUuIikRxN209qlOzeEdyrguCefAVNlyYO5Uslt1HA5BRSTRI/zPkXUEepFPn3DobrZLcuBZw==","cmdExpiresIn":7199}
         */

        private String accessToken;
        private String refreshToken;
        private String expiresIn;
        private CmdBean cmdTokenDTO;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(String expiresIn) {
            this.expiresIn = expiresIn;
        }

        public CmdBean getCmdTokenDTO() {
            return cmdTokenDTO;
        }

        public void setCmdTokenDTO(CmdBean cmdTokenDTO) {
            this.cmdTokenDTO = cmdTokenDTO;
        }
    }
}

package com.sitechdev.vehicle.pad.module.login.bean;

import java.io.Serializable;

public class LoginUserBean implements Serializable {

    /**
     * source : native_app
     * appversion : null
     * devOsType : null
     * deviceId : null
     * deviceName : null
     * loginType : null
     * ip : null
     * accept : null
     * referralCode :
     * deviceType : null
     * userId : 100680010127
     * mobile : 15010279557
     * mobileVerified : true
     * email :
     * emailVerified : false
     * securityInit : true
     * passwordInit : true
     * faceInit : true
     * vehicleInit : false
     * authInit : true
     * langKey : zh_CN
     * avatarUrl : https://cdn2.sitechdev.com/forum/img20190118202546ddf44e67f4b840339bcd3132e844d73a.png
     * nickName : KR
     * cardType : 1
     * cardId : 3****************9
     * realName : *志
     * gender : M
     * dateBirth : 0
     * postCode :
     * addressLine1 :
     * addressCity : 110100
     * addressState : 110000
     * addressStateDesc : 北京
     * addressCityDesc : 北京
     * addressCountry : CN
     * education :
     * industry :
     * job : 新特汽车智能互联中心高级技术经理
     * vehicleFaceInit : false
     * introduction : no
     * level : special
     * dataComplete : null
     * createTime : 1579056836487
     * businessFlag : 0
     * identityType : null
     * credential : {"accessToken":"2WZTA5kjTDQRTDcUYUcvGx-RFrryMmIfTG.E9XGNal-lPN7NXdwTGu2UsKRq3k6hF1awGTJJ1z6bEtMdi9oXmDt3D-SMzxk6wkdspJUXz2zGQR8z0M3gBQesZ9vjLkMbSK21NNcpkQScX3HO9kKbE19pgA0CFdlek4NK0Os7sIHm5gd0dLQfqA__","refreshToken":"2WZTA5kjTDRhQqoRC6iM1Mvz8IpEOxS8h-1Bu9oVAj2lPN7NXdwTGu2UsKRq3k6hF1awGTJJ1z6bEtMdi9oXmCZTdQ3oKUCHhH1FjKAbYQjhodjHEbXyfojvZF2f3bcR-wz52KDiazuRF3.7tY9IjQ__","expiresIn":2678399,"clientCredential":null,"cmdTokenDTO":null}
     */

    private String source;
    private String appversion;
    private String devOsType;
    private String deviceId;
    private String deviceName;
    private String loginType;
    private String ip;
    private String accept;
    private String referralCode;
    private String deviceType;
    private String userId;
    private String mobile;
    private String points;
    private boolean mobileVerified;
    private String email;
    private boolean emailVerified;
    private boolean securityInit;
    private boolean passwordInit;
    private boolean faceInit;
    private boolean vehicleInit;
    private boolean authInit;
    private String langKey;
    private String avatarUrl;
    private String nickName;
    private int cardType;
    private String cardId;
    private String realName;
    private String gender;
    private int dateBirth;
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
    private boolean vehicleFaceInit;
    private String introduction;
    private String level;
    private String dataComplete;
    private long createTime;
    private int businessFlag;
    private String identityType;
    private CredentialBean credential;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Object getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getDevOsType() {
        return devOsType;
    }

    public void setDevOsType(String devOsType) {
        this.devOsType = devOsType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
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

    public boolean isPasswordInit() {
        return passwordInit;
    }

    public void setPasswordInit(boolean passwordInit) {
        this.passwordInit = passwordInit;
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

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
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

    public int getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(int dateBirth) {
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

    public boolean isVehicleFaceInit() {
        return vehicleFaceInit;
    }

    public void setVehicleFaceInit(boolean vehicleFaceInit) {
        this.vehicleFaceInit = vehicleFaceInit;
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

    public String getDataComplete() {
        return dataComplete;
    }

    public void setDataComplete(String dataComplete) {
        this.dataComplete = dataComplete;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getBusinessFlag() {
        return businessFlag;
    }

    public void setBusinessFlag(int businessFlag) {
        this.businessFlag = businessFlag;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public CredentialBean getCredential() {
        return credential;
    }

    public void setCredential(CredentialBean credential) {
        this.credential = credential;
    }

    public static class CredentialBean {
        /**
         * accessToken : 2WZTA5kjTDQRTDcUYUcvGx-RFrryMmIfTG.E9XGNal-lPN7NXdwTGu2UsKRq3k6hF1awGTJJ1z6bEtMdi9oXmDt3D-SMzxk6wkdspJUXz2zGQR8z0M3gBQesZ9vjLkMbSK21NNcpkQScX3HO9kKbE19pgA0CFdlek4NK0Os7sIHm5gd0dLQfqA__
         * refreshToken : 2WZTA5kjTDRhQqoRC6iM1Mvz8IpEOxS8h-1Bu9oVAj2lPN7NXdwTGu2UsKRq3k6hF1awGTJJ1z6bEtMdi9oXmCZTdQ3oKUCHhH1FjKAbYQjhodjHEbXyfojvZF2f3bcR-wz52KDiazuRF3.7tY9IjQ__
         * expiresIn : 2678399
         * clientCredential : null
         * cmdTokenDTO : null
         */

        private String accessToken;
        private String refreshToken;
        private String expiresIn;
        private String clientCredential;
        private String cmdTokenDTO;

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

        public String getClientCredential() {
            return clientCredential;
        }

        public void setClientCredential(String clientCredential) {
            this.clientCredential = clientCredential;
        }

        public String getCmdTokenDTO() {
            return cmdTokenDTO;
        }

        public void setCmdTokenDTO(String cmdTokenDTO) {
            this.cmdTokenDTO = cmdTokenDTO;
        }
    }
}

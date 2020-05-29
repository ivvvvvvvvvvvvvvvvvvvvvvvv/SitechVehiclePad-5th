package com.sitechdev.vehicle.pad.bean;

/**
 * 项目名称：Sitech
 * 类名称：UserBean
 * 类描述：
 * 创建人：shaozhi
 * 创建时间：2018/03/10 09:25
 * 修改时间：
 * 备注：
 */
public class RefreshTokenBean {

    /**
     * code : 200
     * message :
     * data : {"accessToken":"************","refreshToken":"****************","expiresIn":7200}
     */
    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * accessToken : ************
         * refreshToken : ****************
         * expiresIn : 7200
         */

        private String accessToken;
        private String refreshToken;
        private String expiresIn;

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
    }
}

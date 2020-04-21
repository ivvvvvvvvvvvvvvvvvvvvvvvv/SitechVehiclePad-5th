package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：BaseErrorBean
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/16 0016 18:04
 * 修改时间：
 * 备注：
 */
public class BaseErrorBean implements Serializable {

    /**
     * code : 10001001
     * errors : [{"domain":"loginDTO","location":"body","locationType":"mobile","message":"invalidParameter","reason":"手机号格式不正确"}]
     * message : 参数校验失败
     * response : null
     */

    private String code;
    private String message;
    private Object response;
    private String errors;

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

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public static class ErrorsBean {
        /**
         * domain : loginDTO
         * location : body
         * locationType : mobile
         * message : invalidParameter
         * reason : 手机号格式不正确
         */

        private String domain;
        private String location;
        private String locationType;
        private String message;
        private String reason;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocationType() {
            return locationType;
        }

        public void setLocationType(String locationType) {
            this.locationType = locationType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}

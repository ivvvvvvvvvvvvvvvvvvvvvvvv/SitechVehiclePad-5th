package com.sitechdev.vehicle.pad.module.member.bean;

import java.io.Serializable;

public class PointsSigninBean implements Serializable {

    /**
     * "code": "200",
     * "message": "",
     * "data":
     */
    public String code;
    public String message;
    public SigninDataBean data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public SigninDataBean getData() {
        return data;
    }

    /**
     * "integral": 1  -本次获得积分数，-1代表今天已经签过到
     * "integral1":,  -明天签到获得的积分数
     * "days":10      -已连续签到的天数
     */
    public static class SigninDataBean {
        String integral;
        String integral1;
        String days;
        String status;


        public String getStatus() {
            return status;
        }

        public String getIntegral() {
            return integral;
        }

        public String getIntegral1() {
            return integral1;
        }

        public String getDays() {
            return days;
        }
    }
}

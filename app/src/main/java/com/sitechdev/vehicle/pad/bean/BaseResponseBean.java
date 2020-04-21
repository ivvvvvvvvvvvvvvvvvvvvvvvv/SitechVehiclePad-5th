package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2019/5/29
 * </pre>
 */
public class BaseResponseBean implements Serializable {

    /**
     * code : 10032001
     * data :
     * errors : []
     * message : 账号或验证码错误
     * response : null
     */

    private String code;
    private String data;
    private String message;
    private Object response;
    private Object errors;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}

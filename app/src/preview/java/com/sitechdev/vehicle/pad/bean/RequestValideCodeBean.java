package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：RequestValideCodeBean
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/16 0016 15:22
 * 修改时间：
 * 备注：
 */
public class RequestValideCodeBean implements Serializable {

    private static final long serialVersionUID = -47L;
    /**
     * code : 200
     * message : 验证码发送成功
     * data : null
     */

    private String code;
    private String message;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

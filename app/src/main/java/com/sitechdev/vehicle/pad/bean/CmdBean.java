package com.sitechdev.vehicle.pad.bean;

import java.io.Serializable;

/**
 * 项目名称：SitechVehiclePad
 * 类名称：CmdBean
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2019/08/15 0015 20:50
 * 修改时间：
 * 备注：
 */
public class CmdBean implements Serializable {

    private String cmdToken = "";
    private String cmdExpiresIn = "";

    public String getCmdToken() {
        return cmdToken;
    }

    public void setCmdToken(String cmdToken) {
        this.cmdToken = cmdToken;
    }

    public String getCmdExpiresIn() {
        return cmdExpiresIn;
    }

    public void setCmdExpiresIn(String cmdExpiresIn) {
        this.cmdExpiresIn = cmdExpiresIn;
    }
}

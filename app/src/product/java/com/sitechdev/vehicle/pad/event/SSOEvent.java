package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 登录事件
 *
 * @author liuhe
 * @date 2019/04/22
 */
public class SSOEvent extends BaseEvent<String> {

    /**
     * 二维码登录
     */
    public static final String EB_MSG_LOGIN = "com.xtev.login";

    /**
     * 登录过期
     */
    public static final String EB_MSG_LOGIN_INVALID = "com.xtev.loginInValid";

    /**
     * 单点登录
     */
    public static final String EB_MSG_SSO = "com.xtev.sso";

    public Object mValue;

    public SSOEvent(String key, Object upgradeResponse) {
        this.eventKey = key;
        this.mValue = upgradeResponse;
    }

    public SSOEvent(String otaEventKey) {
        this.eventKey = otaEventKey;
    }
}

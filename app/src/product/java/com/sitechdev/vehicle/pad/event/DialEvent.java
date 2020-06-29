package com.sitechdev.vehicle.pad.event;

import com.sitechdev.vehicle.lib.event.BaseEvent;

/**
 * 拨号键盘操作广播
 *
 * @author liuhe
 * @date 2019/04/18
 */
public class DialEvent extends BaseEvent<String> {

    public static final String KEY_DIAL_INPUT_HIDE = "dial_input_hide";
    public static final String KEY_DIAL_INPUT_SHOW = "dial_input_show";
    /**
     * 输入的手机号
     */
    public String phoneNum;

    public DialEvent(String eventKey, String dialPadPhoneNumStr) {
        this.eventKey = eventKey;
        this.phoneNum = dialPadPhoneNumStr;
    }
}
